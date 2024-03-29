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
 *
 */
public class BNFx extends FindBNF<BNF> {

    // a 初期値: 0
    // b 初期値: -1
    private final int a, b;
    private final BNF bnf;

    BNFx(int a, int b, AbstractBNF abnf) {
        this.a = a;
        this.b = b;
        this.bnf = abnf;
        StringBuilder sb = new StringBuilder();
        if (a == 0 && b == 1) {
            String n = abnf.getName();
            if ((abnf instanceof BNFor || abnf instanceof BNFpl)
                    && n.startsWith("( ") && n.endsWith(" )")) {
                n = n.substring(2, n.length() - 2);
            }
            name = sb.append("[ ").append(n).append(" ]").toString();
        } else {
            if (a == b) {
                sb.append(a);
            } else {
                if (a != 0) {
                    sb.append(a);
                }
                sb.append("*");
                if (b >= 0) {
                    sb.append(b);
                }
            }
            name = sb.toString() + abnf.getName();
        }
    }

    @Override
    public BNFx copy(BNFReg reg) {
        return new BNFx(a, b, (AbstractBNF) bnf.copy(reg));
    }

    @Override
    public <X> Match<X> buildFind(ReadableBlock pac, Object ns, BNFParser<? extends X>... names) {
        Match<X> ret = new Match<>(pac);
        for (int i = 0; b == -1 || i < b; i++) {
//            System.out.println(abnf+":" + strd(ret.ret)+"%"+strd(pac));
            Match sub = bnf.find(pac, ns, names);
            if (sub == null) {
                if (i < a) {
                    pac.back(pac.backLength() - ret.st);
                    return null;
                } else {
                    return ret.end(pac);
                }
            }
            mix(ret, sub);
        }
        return ret;
    }

    @Override
    public String toJava() {
        if ( a == 0  && b == 1 ) {
            return bnf.toJava() + ".c()";
        } else if ( a == 1 && b < 0 ) {
            return bnf.toJava() + ".ix()";
        } else if ( a != 0 || b >= 0 ) {
            return bnf.toJava() + ".x("+a+","+b+")";
        }
        return bnf.toJava() + ".x()";
    }
}
