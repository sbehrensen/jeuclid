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
package net.sourceforge.jeuclid.mathml;

/*
 * Please note: This file was automatically generated from the source of the
 * MathML specification. Do not edit it. If there are errors or missing
 * elements, please correct the stylesheet instead.
 */

/**
 * The fn element makes explicit the fact that a more general MathML object is
 * intended to be used in the same manner as if it were a pre-defined function
 * such as sin or plus.
 * 
 * 
 */
public interface MathMLFnElement extends MathMLContentContainer {
    /**
     * A URI pointing to a definition for this function-type element. Note
     * that there is no stipulation about the form this definition may take!
     * 
     * @return value of the definitionURL attribute.
     */
    String getDefinitionURL();

    /**
     * setter for the definitionURL attribute.
     * 
     * @param definitionURL
     *            new value for definitionURL.
     * @see #getDefinitionURL()
     */
    void setDefinitionURL(String definitionURL);

    /**
     * A string describing the syntax in which the definition located at
     * definitionURL is given.
     * 
     * @return value of the encoding attribute.
     */
    String getEncoding();

    /**
     * setter for the encoding attribute.
     * 
     * @param encoding
     *            new value for encoding.
     * @see #getEncoding()
     */
    void setEncoding(String encoding);
};
