/*
 * Copyright 2002 - 2007 JEuclid, http://jeuclid.sf.net
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

package net.sourceforge.jeuclid.elements.presentation.table;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;

import net.sourceforge.jeuclid.mathml.MathMLElement;
import net.sourceforge.jeuclid.mathml.MathMLLabeledRowElement;

/**
 * This class represents the mlabeledtr tag.
 * 
 * <p>
 * TODO: add proper support for labels. They are currently silently ignored.
 * 
 * @version $Revision$
 */
public final class Mlabeledtr extends AbstractTableRow implements
        MathMLLabeledRowElement {
    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "mlabeledtr";

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor. Sets MathML Namespace.
     * 
     * @param qname
     *            Qualified name.
     * @param odoc
     *            Owner Document.
     */
    public Mlabeledtr(final String qname, final AbstractDocument odoc) {
        super(qname, odoc);
    }

    /** {@inheritDoc} */
    @Override
    protected Node newNode() {
        return new Mlabeledtr(this.nodeName, this.ownerDocument);
    }

    /** {@inheritDoc} */
    public MathMLElement getLabel() {
        return this.getMathElement(0);
    }

    /** {@inheritDoc} */
    public void setLabel(final MathMLElement label) {
        this.setMathElement(0, label);
    }

}
