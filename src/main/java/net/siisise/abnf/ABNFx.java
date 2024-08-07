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
package net.siisise.abnf;

import net.siisise.block.ReadableBlock;
import net.siisise.bnf.BNF;
import net.siisise.bnf.BNFReg;
import net.siisise.bnf.parser.BNFParser;

/**
 * 繰り返し.
 * a*b
 * ループの結果は最長だけでなく、各長さで判定したいこともあり
 *
 */
public class ABNFx extends FindABNF {

    // a 初期値: 0
    // b 初期値: -1
    private final int a, b;
    private final BNF bnf;

    ABNFx(int a, int b, AbstractABNF abnf) {
        this.a = a;
        this.b = b;
        this.bnf = abnf;
        StringBuilder sb = new StringBuilder();
        if (a == 0 && b == 1) {
            String n = abnf.getName();
            if ((abnf instanceof ABNFor || abnf instanceof ABNFpl)
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
    public ABNFx copy(BNFReg<ABNF> reg) {
        return new ABNFx(a, b, (AbstractABNF) bnf.copy(reg));
    }

    /**
     * find 本体.
     * @param <X> 戻り型
     * @param pac 元データ
     * @param ns user name space ユーザ名前空間
     * @param parsers サブ要素のパーサ
     * @return サブ要素を含む解析結果
     */
    @Override
    public <X> Match<X> buildFind(ReadableBlock pac, Object ns, BNFParser<? extends X>... parsers) {
        Match<X> ret = new Match<>(pac);
        for (int i = 0; b == -1 || i < b; i++) {
            Match sub = bnf.find(pac, ns, parsers);
            if (sub == null) {
                if (i < a) {
                    ret.end(pac);
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
            return bnf.toJava() + ".c()";
        } else if ( a == 1 && b < 0 ) {
            return bnf.toJava() + ".ix()";
        } else if ( a == b ) {
            return bnf.toJava() + ".x("+a+")";
        } else if ( a != 0 || b >= 0 ) {
            return bnf.toJava() + ".x("+a+","+b+")";
        }
        return bnf.toJava() + ".x()";
    }
}
