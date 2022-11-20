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
import net.siisise.bnf.parser.BNFParser;

/**
 * 軽量一致検索。
 * 大抵はこちらで間に合うが繰り返しのあとに同じものがくると解析できないのでABNFplmを使えばいいよ
 * example = *( a / b ) a
 * のようなパターンは無理
 */
public class ABNFpl extends FindABNF {

    protected final BNF[] list;

    public ABNFpl(BNF... abnfs) {
        list = abnfs;
        StringBuilder names = new StringBuilder();
        for (BNF abnf : list) {
            names.append(abnf.getName());
            names.append(" ");
        }
        names.deleteCharAt(names.length() - 1);
        name = "( " + names.toString() + " )";
    }

    /**
     * 複製する
     * @param reg 複製先
     * @return 複製
     */
    @Override
    public ABNFpl copy(ABNFReg reg) {
        BNF[] cplist = new BNF[list.length];

        for (int i = 0; i < list.length; i++) {
            cplist[i] = list[i].copy(reg);
        }
        return new ABNFpl(cplist);
    }

    /**
     * 単純に積む.
     * @param <X> 返却予定の型
     * @param pac 解析対象
     * @param ns user name space
     * @param parsers サブ解析装置
     * @return サブ要素を含む解析結果
     */
    @Override
    public <X> Match<X> buildFind(ReadableBlock pac, Object ns, BNFParser<? extends X>... parsers) {
        Match<X> ret = new Match<>(pac);
        
        for (BNF sub : list) {
            Match<X> subret = sub.find(pac, ns, parsers);
            if (subret == null) {
                pac.seek(ret.st);
                return null;
            }
            mix(ret, subret);
        }
        return ret;
    }
}
