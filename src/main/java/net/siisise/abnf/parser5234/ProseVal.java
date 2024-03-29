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
import net.siisise.block.ReadableBlock;
import net.siisise.bnf.BNF;
import net.siisise.bnf.BNFReg;
import net.siisise.bnf.parser.BNFBaseParser;

/**
 * コメント的なもの
 * 対応していないので止まりたい
 */
public class ProseVal extends BNFBaseParser<ABNF> {

    public ProseVal(BNF abnf, BNFReg base) {
        super(abnf);
    }

    @Override
    public ABNF parse(ReadableBlock pac) {
        ReadableBlock p = rule.is(pac);
        if ( p == null ) {
            return null;
        }
        throw new UnsupportedOperationException("Not supported yet." + strd(pac)); //To change body of generated methods, choose Tools | Templates.
    }
}
