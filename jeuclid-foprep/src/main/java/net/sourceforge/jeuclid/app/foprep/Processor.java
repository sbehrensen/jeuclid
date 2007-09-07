/*
 * Copyright 2007 - 2007 JEuclid, http://jeuclid.sf.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* $Id$ */

package net.sourceforge.jeuclid.app.foprep;

import java.awt.Dimension;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;

import net.sourceforge.jeuclid.DOMBuilder;
import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.context.LayoutContextImpl;
import net.sourceforge.jeuclid.elements.AbstractJEuclidElement;
import net.sourceforge.jeuclid.elements.generic.DocumentElement;
import net.sourceforge.jeuclid.elements.generic.MathImpl;
import net.sourceforge.jeuclid.layout.JEuclidView;
import net.sourceforge.jeuclid.parser.Parser;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Contains the actual processing routines.
 * <p>
 * To use this class obtain an instance of the Processor singleton instance.
 * Then use the {@link #process(Source, Result)} function to process your
 * Document.
 * <p>
 * This will replace all occurrences of MathML within fo:instream tags by the
 * equivalent SVG code. It will also add a baseline-shift attribute so that
 * the formula is in line with the rest of the text.
 * 
 * @author Max Berger
 * @version $Revision$
 */
public final class Processor {

    private static Processor processor;

    /**
     * Logger for this class
     */
    private static final Log LOGGER = LogFactory.getLog(Processor.class);

    private final Transformer transformer;

    private Processor() throws TransformerException {
        this.transformer = TransformerFactory.newInstance().newTransformer();
    }

    /**
     * Retrieve the processor singleton object.
     * 
     * @return the Processor.
     * @throws TransformerException
     *             an error occurred creating the necessary Transformer
     *             instance.
     */
    public static synchronized Processor getProcessor()
            throws TransformerException {
        if (Processor.processor == null) {
            Processor.processor = new Processor();
        }
        return Processor.processor;
    }

    /**
     * Pre-process a .fo file.
     * 
     * @param inputSource
     *            Input File
     * @param result
     *            Output File
     * @param context
     *            LayoutContext.
     * @throws TransformerException
     *             an error occurred during the processing.
     */
    public void process(final Source inputSource, final Result result,
            final LayoutContext context) throws TransformerException {
        Processor.LOGGER.info("Processing " + inputSource + " to " + result);
        try {
            final Node doc = Parser.getParser().parse(inputSource);

            this.processSubtree(doc, context);

            final DOMSource source = new DOMSource(doc);

            this.transformer.transform(source, result);

        } catch (final IOException e) {
            throw new TransformerException("IOException", e);
        } catch (final SAXException e) {
            throw new TransformerException("SAXException", e);
        } catch (final ParserConfigurationException e) {
            throw new TransformerException("ParserConfigurationException", e);
        }
    }

    /**
     * Pre-process a .fo file.
     * 
     * @param inputSource
     *            Input File
     * @param result
     *            Output File
     * @throws TransformerException
     *             an error occurred during the processing.
     */
    public void process(final Source inputSource, final Result result)
            throws TransformerException {
        this.process(inputSource, result, LayoutContextImpl
                .getDefaultLayoutContext());
    }

    private void processSubtree(final Node node, final LayoutContext context) {
        if (AbstractJEuclidElement.URI.equals(node.getNamespaceURI())
                && MathImpl.ELEMENT.equals(node.getLocalName())) {

            final DocumentElement doc = DOMBuilder.getDOMBuilder()
                    .createJeuclidDom(node);

            final SVGGraphics2D svgGenerator = this.createSVGGenerator();
            final JEuclidView view = new JEuclidView(doc, context,
                    svgGenerator);
            final int ascent = (int) Math.ceil(view.getAscentHeight());
            final int descent = (int) Math.ceil(view.getDescentHeight());
            final int height = ascent + descent;
            final Dimension size = new Dimension((int) Math.ceil(view
                    .getWidth()), height);
            svgGenerator.setSVGCanvasSize(size);
            view.draw(svgGenerator, 0, ascent);
            final float baselinePercent = -((float) descent / (float) height) * 100f;

            final Node parent = node.getParentNode();
            if ("http://www.w3.org/1999/XSL/Format".equals(parent
                    .getNamespaceURI())
                    && "instream-foreign-object"
                            .equals(parent.getLocalName())) {
                final Element pElement = (Element) parent;
                pElement.setAttribute("alignment-adjust", baselinePercent
                        + "%");
            }
            this.safeReplaceChild(parent, node, svgGenerator.getRoot());
        } else {
            this.processChildren(node, context);
        }
    }

    private SVGGraphics2D createSVGGenerator() {
        final DOMImplementation domImpl = GenericDOMImplementation
                .getDOMImplementation();

        final Document document = domImpl.createDocument(null,
                net.sourceforge.jeuclid.converter.Converter.EXTENSION_SVG,
                null);
        final SVGGeneratorContext svgContext = SVGGeneratorContext
                .createDefault(document);
        svgContext.setComment("Converted from MathML using JEuclid");
        final SVGGraphics2D svgGenerator = new SVGGraphics2D(svgContext, true);

        return svgGenerator;
    }

    private void safeReplaceChild(final Node parent, final Node oldChild,
            final Node newChild) {
        try {
            final DOMSource source = new DOMSource(newChild);
            final DOMResult result = new DOMResult(parent);

            this.transformer.transform(source, result);
        } catch (final TransformerException e) {
            Processor.LOGGER.warn("TranformerException: " + e.getMessage());
        }
        parent.removeChild(oldChild);
    }

    private void processChildren(final Node node, final LayoutContext context) {
        final NodeList childList = node.getChildNodes();
        if (childList != null) {
            for (int i = 0; i < childList.getLength(); i++) {
                final Node child = childList.item(i);
                this.processSubtree(child, context);
            }
        }
    }

}
