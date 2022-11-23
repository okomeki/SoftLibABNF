/*
 * Copyright 2022 Siisise Net.
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
package net.siisise.ebnf;

import net.siisise.bnf.BNFCC;
import net.siisise.ebnf.parser.EBNFISO14977;

/**
 *
 */
public class EBNFReg extends BNFCC<EBNF> {
    
    public EBNFReg() {
        super(null, EBNFISO14977.REG, "syntax","syntax-rule","meta-identifier","definitions-list");
        rn = EBNFISO14977.metaIdentifier;
    }

    public EBNF syntax(String syntax) {
        
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public EBNF ref(String rulename) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
