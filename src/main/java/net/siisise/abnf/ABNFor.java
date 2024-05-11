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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import net.siisise.block.ByteBlock;
import net.siisise.block.ReadableBlock;
import net.siisise.bnf.BNF;
import net.siisise.bnf.BNFReg;
import net.siisise.bnf.parser.BNFParser;
import net.siisise.lang.CodePoint;

/**
 * 最長一致 longest match
 */
public class ABNFor extends FindABNF {

    protected BNF[] list;

    public ABNFor(BNF... abnfs) {
        list = abnfs;
        name = toName(abnfs);
    }

    /**
     * copy用ぐらいかな
     * @param n name
     * @param abnfs 要素
     */
    ABNFor(String n, BNF... abnfs) {
        name = n;
        list = abnfs;
    }

    /**
     * text として
     * ABNFmap 推奨
     * @deprecated ABNFor1 が最適かもしれない
     * @param chlist 文字の一覧として
     */
    @Deprecated
    public ABNFor(String chlist) {
        this(null, chlist);
    }

    public ABNFor(String name, String chlist) {
        ReadableBlock src = ReadableBlock.wrap(chlist);
        List<ABNF> abnfs = new ArrayList<>();
        while (src.length() > 0) {
            abnfs.add(new ABNFtext(CodePoint.utf8(src)));
        }
        list = abnfs.toArray(new ABNF[abnfs.size()]);
        if ( name == null ) {
            this.name = toName(list);
        } else {
            this.name = name;
        }
    }

    /**
     * 名前をつくる.
     * @param abnfs
     * @return 
     */
    static String toName(BNF[] abnfs) {
        StringBuilder sb = new StringBuilder();
        //if ( list.length > 1) {
        sb.append("( ");
        //}
        for (BNF v : abnfs) {
            String n = v.getName();
            if ((v instanceof ABNFor) && n.startsWith("( ") && n.endsWith(" )")) {
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
    public ABNFor copy(BNFReg<ABNF> reg) {
        BNF[] l = new BNF[list.length];

        for (int i = 0; i < list.length; i++) {
            l[i] = list[i].copy(reg);
        }
        return new ABNFor(name, l);
    }

    /**
     * 条件を追加する.
     * @param val 追加条件
     */
    public void add(ABNF... val) {
        ABNF[] n = new ABNF[list.length + val.length];
        System.arraycopy(list, 0, n, 0, list.length);
        System.arraycopy(val, 0, n, list.length, val.length);
//        if (name.contains("(")) {
            name = toName(n);
//        }
        list = n;
    }

    /**
     * 最長一致検索.
     * 候補から最長のものを返す.
     * @param <X> 戻り型
     * @param pac データ
     * @param ns 名前空間
     * @param parsers sub parser
     * @return 最長の結果.
     */
    @Override
    public <X> Match<X> buildFind(ReadableBlock pac, Object ns, BNFParser<? extends X>... parsers) {
        if ( pac instanceof ByteBlock ) {
            return byteFind((ByteBlock)pac, ns, parsers);
        }
        ABNF.Match<X> ret = null;
        long bp = pac.backLength();
        long o = bp;
        for (BNF sub : list) {
            Match<X> subret = sub.find(pac, ns, parsers);
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
    
    /**
     * pos を複数用意した並列版
     * @param <X>
     * @param <N>
     * @param src
     * @param ns 名前空間
     * @param parsers
     * @return 
     */
    <X> Match<X> byteFind(ByteBlock src, Object ns, BNFParser<? extends X>... parsers) {
        long bp = src.backLength();
        long sl = src.length();
        List<BNF> bnfList = Arrays.asList(list);
        List<Match> sub = bnfList.parallelStream().map(b -> b.find((ReadableBlock)src.sub(bp, sl),ns,parsers))
                .filter(x -> x != null).collect(Collectors.toList());
        if ( sub.isEmpty() ) {
            return null;
        }
        Match<X> ret = sub.stream().max((a,b) -> (int)(a.sub.length() - b.sub.length())).get();
        ret.st = bp;
        src.skip(ret.sub.length());
        return ret;
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
