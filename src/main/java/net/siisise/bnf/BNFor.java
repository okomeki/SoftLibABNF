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
package net.siisise.bnf;

import net.siisise.block.ReadableBlock;
import net.siisise.bnf.parser.BNFParser;

/**
 * or 慎重に最長一致検索するので低速になりがち.
 */
public class BNFor extends FindBNF<BNF> {

    protected final BNF[] list;

    public BNFor(BNF... bnfs) {
        list = bnfs;
        name = toName(bnfs);
    }

    public BNFor(String n, BNF... bnfs) {
        name = n;
        list = bnfs;
    }

    static String toName(BNF[] abnfs) {
        StringBuilder sb = new StringBuilder();
        //if ( list.length > 1) {
        sb.append("( ");
        //}
        for (BNF v : abnfs) {
            String n = v.getName();
            if (v instanceof BNFor && n.startsWith("( ") && n.endsWith(" )")) {
                n = n.substring(2, n.length() - 2);
                sb.append(n);
            } else {
                sb.append(v.getName());
            }
            sb.append(" / ");
        }
        sb.delete(sb.length() - 3, sb.length());
        //if ( list.length > 1 ) {
        sb.append(" )");
        //}
        return sb.toString();
    }

    /**
     *
     * @param <X> return object type
     * @param pac データ
     * @param ns user name space
     * @param parsers サブ要素パーサ
     * @return 解析結果
     */
    @Override
    protected <X> Match<X> buildFind(ReadableBlock pac, Object ns, BNFParser<? extends X>... parsers) {
        for (BNF sub : list) {
            Match<X> subret = sub.find(pac, ns, parsers);
            if (subret != null) {
                return subret;
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
        return new BNFor(name, l);
    }

    @Override
    public String toJava() {
        StringBuilder src = new StringBuilder();
        src.append(list[0].toJava());
        if ( list.length > 1 ) {
            src.append(".or(");
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
