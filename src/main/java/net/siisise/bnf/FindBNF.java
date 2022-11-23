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

import net.siisise.block.ReadableBlock;
import net.siisise.bnf.parser.BNFParser;

/**
 * isとfindの実装が必要だが
 * find側の実装だけで済ませたい処理.
 * @param <B> BNFの型
 */
public abstract class FindBNF<B extends BNF> extends AbstractBNF<B> {

    /**
     * 単純な比較をfind で処理する.
     * @param src source 解析対象
     * @param ns user name space 未使用時はnull可能
     * @return 一致した場合
     */
    @Override
    public ReadableBlock is(ReadableBlock src, Object ns) {
        Match ret = find(src,ns);
        if (ret == null) {
            return null;
        }
        return ret.sub;
    }
    
    /**
     * 単純な比較をfind で処理する.
     * @param src source 解析対象
     * @return 一致部分
     */
    @Override
    public ReadableBlock is(ReadableBlock src) {
        return is(src, null);
    }
    
    /**
     * 詰め方の工夫をするターン
     *
     * @param <X> 戻り型参考
     * @param rb source 解析対象
     * @param ns user name space ユーザ名前空間
     * @param parsers sub parsers サブ要素パーサ群
     * @return 解析結果
     */
    @Override
    public <X> Match<X> find(ReadableBlock rb, Object ns, BNFParser<? extends X>... parsers) {
        Match<X> ret = buildFind(rb, ns, parsers);
        if ( ret != null ) {
            ret.end(rb);
        }
        return ret;
    }

    /**
     * find本体
     *
     * @param <X> 戻り型
     * @param pac データ
     * @param ns user name space ユーザ名前空間
     * @param parsers サブ要素のパーサ
     * @return サブ要素を含む解析結果
     */
    protected abstract <X> Match<X> buildFind(ReadableBlock pac, Object ns, BNFParser<? extends X>... parsers);
}
