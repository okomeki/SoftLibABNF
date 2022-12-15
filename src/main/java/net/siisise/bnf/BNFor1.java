/*
 * Copyright 2022 okome.
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
package net.siisise.bnf;

import net.siisise.block.ReadableBlock;
import net.siisise.bnf.parser.BNFParser;

/**
 * 最初一致型
 */
public class BNFor1 extends BNFor {

    BNFor1(BNF[] list) {
        super(list);
    }

    @Override
    protected <X> Match<X> buildFind(ReadableBlock pac, Object ns, BNFParser<? extends X>... parsers) {
        for ( BNF sub : list ) {
            Match<X> ret = sub.find(pac, ns, parsers);
            if ( ret != null ) {
                return ret;
            }
        }
        return null;
    }

    @Override
    public BNF copy(BNFReg reg) {
        BNF[] l = new BNF[list.length];

        for (int i = 0; i < list.length; i++) {
            l[i] = list[i].copy(reg);
        }
        return new BNFor1(l);
    }
    
    @Override
    public String toJava() {
        StringBuilder src = new StringBuilder();
        src.append(list[0].toJava());
        if ( list.length > 1 ) {
            src.append(".or1(");
            for ( int i = 1; i < list.length - 1; i++ ) {
                src.append(list[i].toJava());
                src.append(",");
            }
            src.append(list[list.length-1].toJava());
            src.append(")");
        }
        return src.toString();
    }
}
