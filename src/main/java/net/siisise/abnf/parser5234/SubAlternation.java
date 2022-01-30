/*
 * Copyright 2021 Siisise Net.
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
package net.siisise.abnf.parser5234;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser.ABNFSelect;

/**
 * elementsとgroup のParser.
 * alternationを取り出すだけ
 * 定義のみなので消せる?
 */
public class SubAlternation extends ABNFSelect<ABNF> {
    
    /**
     * 
     * @param rule groupのABNF構文
     * @param base Parser駆動用
     */
    public SubAlternation(ABNF rule, ABNFReg base) {
        super(rule, base, "alternation");
    }
}
