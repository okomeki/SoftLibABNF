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
import net.siisise.bnf.parser.BNFParser;

/**
 * findの分離
 * サブ要素を持たない方
 */
public abstract class IsABNF extends AbstractABNF {
    
    /**
     * 先頭一致でパースする。
     *
     * @param src source 解析対象
     * @param ns user name space 名前空間
     * @return 一致した範囲
     */
    @Override
    public ReadableBlock is(ReadableBlock src, Object ns) {
        return is(src);
    }
    
    /**
     * sub要素のない場合の軽い対応
     * @param <X> パラメータっぽい型
     * @param pac 解析データ
     * @param ns name space
     * @param parsers サブ要素のparser
     * @return 処理結果
     */
    @Override
    public <X> Match<X> find(ReadableBlock pac, Object ns, BNFParser<? extends X>... parsers) {
        Match<X> ret = new Match(pac);
        ret.sub = is(pac, ns);
        return (ret.sub == null) ? null : ret;
    }
}
