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

import java.util.ArrayList;
import java.util.List;
import net.siisise.bnf.BNF;
import net.siisise.bnf.parser.BNFParser;
import net.siisise.lang.CodePoint;
import net.siisise.block.ReadableBlock;
import net.siisise.bnf.BNFReg;

/**
 *
 */
public class EBNFor extends FindEBNF {

    private BNF[] list;

    public EBNFor(BNF... ebnfs) {
        list = ebnfs;
        name = toName(ebnfs);
    }

    public EBNFor(String n, BNF... ebnfs) {
        name = n;
        list = ebnfs;
    }

    /**
     * text として
     * ABNFmap 推奨
     *
     * @param chlist 文字の一覧として
     */
    public EBNFor(String chlist) {
        ReadableBlock src = rb(chlist);
        List<BNF> abnfs = new ArrayList<>();
        while (src.length() > 0) {
            abnfs.add(new EBNFtext(CodePoint.utf8(src)));
        }
        list = abnfs.toArray(new BNF[abnfs.size()]);
        name = toName(list);
    }

    public EBNFor(String name, String list) {
        ReadableBlock p = rb(list);
        List<BNF> fs = new ArrayList<>();
        while (p.length() > 0) {
            fs.add(new EBNFtext(CodePoint.utf8(p)));
        }
        this.list = fs.toArray(new BNF[0]);
        this.name = name;
    }

    static String toName(BNF[] abnfs) {
        StringBuilder sb = new StringBuilder();
        //if ( list.length > 1) {
        sb.append("( ");
        //}
        for (BNF v : abnfs) {
            String n = v.getName();
            if (v instanceof EBNFor && n.startsWith("( ") && n.endsWith(" )")) {
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
    public EBNFor copy(BNFReg<EBNF> reg) {
        BNF[] l = new BNF[list.length];

        for (int i = 0; i < list.length; i++) {
            l[i] = list[i].copy(reg);
        }
        return new EBNFor(name, l);
    }

    public void add(BNF... val) {
        BNF[] n = new BNF[list.length + val.length];
        System.arraycopy(list, 0, n, 0, list.length);
        System.arraycopy(val, 0, n, list.length, val.length);
        if (name.contains("(")) {
            name = toName(n);
        }
        list = n;
    }

    @Override
    public <X> Match<X> buildFind(ReadableBlock pac, Object ns, BNFParser<? extends X>... parsers) {
        BNF.Match<X> ret = null;
        long bp = pac.backLength();
        long o = bp;

        for (BNF sub : list) {

            Match<X> subret = sub.find(pac, ns, parsers);
            if (subret != null) {
                long datasize = subret.sub.length();
                if (ret == null || ret.sub.length() < datasize) {
                    o = bp + datasize;
                    ret = subret;
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
