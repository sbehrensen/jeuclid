/*
 * Copyright 2009 Erhard Kuenzel
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

package cTree.cSplit;

import cTree.CElement;
import cTree.cDefence.CD_Event;

/**
 * Transports an additional String which describes how to split the active
 * CElement
 * 
 * @version $Revision$
 */
public class CS_Event extends CD_Event {

    private final String s;

    public CS_Event(final CElement cElement, final String s) {
        super(cElement);
        super.setDoDef(false);
        this.s = s;
    }

    public String getString() {
        return this.s;
    }

    public String getOperation() {
        return this.s.substring(0, 1);
    }

    public String getOperator() {
        return this.s.substring(1);
    }
}
