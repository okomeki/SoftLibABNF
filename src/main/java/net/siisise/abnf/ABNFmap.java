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

import java.util.ArrayList;
import java.util.List;
import net.siisise.block.ReadableBlock;
import net.siisise.bnf.BNF;
import net.siisise.bnf.BNFReg;
import net.siisise.lang.CodePoint;

/**
 * 文字が含まれるかどうかのMapだかListだか
 * 大文字小文字は別物のバイナリ表記寄り
 */
public class ABNFmap extends IsABNF {

    private final List<Integer> map = new ArrayList<>();

    public ABNFmap() {
        name = "()";
    }

    public ABNFmap(String val) {
        ReadableBlock pac = ReadableBlock.wrap(val);
        StringBuilder n = new StringBuilder();
        n.append("( ");
        while (pac.size() > 0) {
            if ( n.length() > 2) {
                n.append(" / ");
            }
            int ch = CodePoint.utf8(pac);
            if (!map.contains(ch)) {
                map.add(ch);
                if ( (ch >= 0x20 && ch <= 0x7e && ch != 0x22) ) {
                    n.append('"');
                    n.append(ch);
                    n.append('"');
                } else {
                    n.append(uhex(ch));
                }
            }
        }
        n.append(" )");
        name = n.toString();
    }

    @Override
    public ReadableBlock is(ReadableBlock pac) {
        if (pac.length() == 0) {
            return null;
        }
        int of = pac.backSize();
        int ch = CodePoint.utf8(pac);
        if (ch < 0) {
            return null;
        }
        byte[] bin8 = CodePoint.utf8(ch);
        if (map.contains(ch)) {
            return ReadableBlock.wrap(bin8);
        }
        pac.seek(of);
        return null;
    }

    /**
     * 複製する.
     * @param reg 複製先
     * @return 複製
     */
    @Override
    public ABNF copy(BNFReg<ABNF> reg) {
        ABNFmap nm = new ABNFmap();
        nm.map.addAll(map);
        return nm;
    }

    @Override
    public ABNF or(BNF... abnf) {
        List<Integer> tmap = new ArrayList<>();
        List<BNF> xabnf = new ArrayList<>();

        for (BNF a : abnf) {
            int ach;
            if (a instanceof ABNFbin && (ach = ((ABNFbin) a).ch()) >= 0) {
                if (!map.contains(ach)) {
                    tmap.add(ach);
                }
            } else {
                xabnf.add(a);
            }
        }
        if (!tmap.isEmpty()) {
            ABNFmap nm = new ABNFmap();
            nm.map.addAll(map);
            nm.map.addAll(tmap);
            if (xabnf.isEmpty()) {
                return nm;
            }
            xabnf.add(nm);
            return new ABNFor(xabnf.toArray(new BNF[0]));
        }
        return super.or(abnf);
    }

    /**
     * 順番が前後することがある.
     * 
     * @param abnf 付け
     * @return 繋いだABNF
     */
    @Override
    public ABNF or1(BNF... abnf) {
        List<Integer> tmap = new ArrayList<>();
        List<BNF> xabnf = new ArrayList<>();

        for (BNF a : abnf) {
            int ach;
            if (a instanceof ABNFbin && (ach = ((ABNFbin) a).ch()) >= 0) {
                if (!map.contains(ach)) {
                    tmap.add(ach);
                }
            } else {
                xabnf.add(a);
            }
        }
        if (!tmap.isEmpty()) {
            ABNFmap nm = new ABNFmap();
            nm.map.addAll(map);
            nm.map.addAll(tmap);
            if (xabnf.isEmpty()) {
                return nm;
            }
            xabnf.add(0,nm);
            return new ABNFor1(xabnf.toArray(new BNF[0]));
        }
        return super.or1(abnf);
    }

    /**
     * 
     * @return Javaっぽく
     */
    @Override
    public String toJava() {
        StringBuilder sb = new StringBuilder();
        sb.append("ABNF.binlist(\"");
        for ( Integer ch : map ) {
            switch (ch) { // Java なのでなんとかなる?
                case 0x08:  sb.append("\\b");   break;
                case 0x09:  sb.append("\\t");   break;
                case 0x0a:  sb.append("\\n");   break;
                case 0x0c:  sb.append("\\f");   break;
                case 0x0d:  sb.append("\\r");   break;
                case 0x22:  sb.append("\\\"");  break;
                case 0x5c:  sb.append("\\\\");  break;
                default:
                    sb.appendCodePoint(ch);
            }
        }
        sb.append("\")");
        return sb.toString();
    }
}
