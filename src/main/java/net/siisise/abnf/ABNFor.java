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
import net.siisise.bnf.parser.BNFParser;
import net.siisise.lang.CodePoint;

/**
 * 最長一致
 */
public class ABNFor extends FindABNF {

    private BNF[] list;

    public ABNFor(BNF... abnfs) {
        list = abnfs;
        name = toName(abnfs);
    }

    public ABNFor(String n, BNF... abnfs) {
        name = n;
        list = abnfs;
    }

    /**
     * text として
     * ABNFmap 推奨
     *
     * @param chlist 文字の一覧として
     */
    public ABNFor(String chlist) {
        ReadableBlock src = ReadableBlock.wrap(chlist);
        List<ABNF> abnfs = new ArrayList<>();
        while (src.length() > 0) {
            abnfs.add(new ABNFtext(CodePoint.utf8(src)));
        }
        list = abnfs.toArray(new ABNF[abnfs.size()]);
        name = toName(list);
    }

    public ABNFor(String name, String list) {
        this(list);
        this.name = name;
    }

    private static String toName(BNF[] abnfs) {
        StringBuilder sb = new StringBuilder();
        //if ( list.length > 1) {
        sb.append("( ");
        //}
        for (BNF v : abnfs) {
            String n = v.getName();
            if (v instanceof ABNFor && n.startsWith("( ") && n.endsWith(" )")) {
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
     * 複製する.
     * @param reg 複製先
     * @return 複製
     */
    @Override
    public ABNFor copy(ABNFReg reg) {
        BNF[] l = new BNF[list.length];

        for (int i = 0; i < list.length; i++) {
            l[i] = list[i].copy(reg);
        }
        return new ABNFor(name, l);
    }

    public void add(ABNF... val) {
        ABNF[] n = new ABNF[list.length + val.length];
        System.arraycopy(list, 0, n, 0, list.length);
        System.arraycopy(val, 0, n, list.length, val.length);
//        if (name.contains("(")) {
            name = toName(n);
//        }
        list = n;
    }

    @Override
    public <X,N> C<X> buildFind(ReadableBlock pac, N ns, BNFParser<? extends X>... parsers) {
        ABNF.C<X> ret = null;
        long bp = pac.backLength();
        long o = bp;
        for (BNF sub : list) {
            C<X> subret = sub.find(pac, ns, parsers);
            if (subret != null) {
                long len = subret.sub.length();
                if (ret == null || ret.sub.length() < len) {
                    ret = subret;
                    o = bp + len; // pac.backLength();
                }
                pac.seek(bp);
            }
        }
        if (ret != null) {
            pac.seek(o);
        }
        return ret;
    }
}
