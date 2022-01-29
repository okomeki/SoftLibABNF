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

import net.siisise.bnf.parser.BNFParser;
import net.siisise.io.FrontPacket;

/**
 * or
 */
public class BNFor extends FindBNF {

    private final BNF[] list;

    public BNFor(BNF... abnfs) {
        list = abnfs;
        name = toName(abnfs);
    }

    public BNFor(String n, BNF... abnfs) {
        name = n;
        list = abnfs;
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
     * @param <N> user name space type
     * @param pac データ
     * @param ns user name space
     * @param parsers サブ要素パーサ
     * @return 解析結果
     */
    @Override
    public <X,N> C<X> buildFind(FrontPacket pac, N ns, BNFParser<? extends X>... parsers) {
        BNF.C ret = null;

        for (BNF sub : list) {

            C subret = sub.find(pac, ns, parsers);
            if (subret != null) {
                byte[] data = subret.ret.toByteArray();
                pac.backWrite(data);
                if (ret == null || ret.ret.length() < data.length) {
                    subret.ret.backWrite(data);
//                    if (ret != null) {
//                        System.out.println("+******DUUPP****+" + subret + "(" + sub.toString() + ")");
//                    }
                    ret = subret;
                }
            }
        }
        if (ret == null) {
            return null;
        }

        byte[] e = new byte[(int) ret.ret.length()];
        pac.read(e);
        return ret;
    }
}
