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
package net.siisise.ebnf;

import net.siisise.bnf.parser.BNFParser;
import net.siisise.block.ReadableBlock;
import net.siisise.bnf.BNFReg;

/**
 *
 */
public class EBNFx extends FindEBNF {

    // a 初期値: 0
    // b 初期値: -1
    private final int a, b;
    private final EBNF abnf;

    EBNFx(int a, int b, AbstractEBNF abnf) {
        this.a = a;
        this.b = b;
        this.abnf = abnf;
        StringBuilder sb = new StringBuilder();
        if (a == 0 && b == 1) {
            String n = abnf.getName();
            if ((abnf instanceof EBNFor || abnf instanceof EBNFpl)
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
    public EBNFx copy(BNFReg<EBNF> reg) {
        return new EBNFx(a, b, (AbstractEBNF) abnf.copy(reg));
    }

    @Override
    public <X> Match<X> buildFind(ReadableBlock pac, Object ns, BNFParser<? extends X>... names) {
        Match<X> ret = new Match<>(pac);
        for (int i = 0; b == -1 || i < b; i++) {
//            System.out.println(abnf+":" + strd(ret.ret)+"%"+strd(pac));
            Match sub = abnf.find(pac, ns, names);
            if (sub == null) {
                if (i < a) {
                    pac.back(pac.backLength() - ret.st);
                    return null;
                } else {
                    return ret;
                }
            }
            mix(ret, sub);
        }
        return ret;
    }

    @Override
    public String toJava() {
        if ( a == 0  && b == 1 ) {
            return abnf.toJava() + ".c()";
        } else if ( a == 1 && b < 0 ) {
            return abnf.toJava() + ".ix()";
        } else if ( a != 0 || b >= 0 ) {
            return abnf.toJava() + ".x("+a+","+b+")";
        }
        return abnf.toJava() + ".x()";
    }
}
