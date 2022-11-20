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
package net.siisise.abnf;

import java.util.ArrayList;
import java.util.List;
import net.siisise.block.ReadableBlock;
import net.siisise.bnf.BNF;
import net.siisise.bnf.parser.BNFParser;
import net.siisise.lang.CodePoint;

/**
 * 最初に一致したものを返す簡単な方.
 * 最長一致が不要な場合に高速になることがある.
 */
public class ABNFor1 extends ABNFor {
    
    /**
     * 入り口
     */
    ABNFor1(BNF[] list) {
        super(toName(list), list);
    }
    
    ABNFor1(String n, BNF... abnfs) {
        super(n,abnfs);
    }

    /**
     * text として
     * ABNFmap 推奨
     *
     * @param chlist 文字の一覧として
     */
    public ABNFor1(String chlist) {
        this(null, chlist);
    }

    public ABNFor1(String name, String chlist) {
        super();
        ReadableBlock src = ReadableBlock.wrap(chlist);
        List<BNF> abnfs = new ArrayList<>();
        while (src.length() > 0) {
            abnfs.add(new ABNFtext(CodePoint.utf8(src)));
        }
        list = abnfs.toArray(new BNF[abnfs.size()]);
        if ( name != null ) {
            this.name = name;
        } else {
            this.name = ABNFor.toName(list);
        }
    }

    /**
     * 複製する.
     * @param reg 複製先
     * @return 複製
     */
    @Override
    public ABNFor1 copy(ABNFReg reg) {
        BNF[] l = new BNF[list.length];

        for (int i = 0; i < list.length; i++) {
            l[i] = list[i].copy(reg);
        }
        return new ABNFor1(name, l);
    }

    /**
     * 先頭一致検索.
     * 候補から最初に一致したものを返す.
     * @param <X> 戻り型
     * @param pac データ
     * @param ns 名前空間
     * @param parsers sub parser
     * @return 最長の結果.
     */
    @Override
    public <X> Match<X> buildFind(ReadableBlock pac, Object ns, BNFParser<? extends X>... parsers) {
        for (BNF sub : list) {
            Match<X> ret = sub.find(pac, ns, parsers);
            if (ret != null) {
                return ret;
            }
        }
        return null;
    }
}
