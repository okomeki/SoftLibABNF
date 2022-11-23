/*
 * Copyright 2022 Siisise Net.
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

import net.siisise.bnf.parser.BNFParser;
import net.siisise.block.ReadableBlock;

/**
 *
 */
public abstract class FindEBNF extends AbstractEBNF {
    
    @Override
    public ReadableBlock is(ReadableBlock src) {
        return is(src, null);
    }

    @Override
    public ReadableBlock is(ReadableBlock src, Object ns) {
        Match ret = find(src, ns);
        if (ret == null) {
            return null;
        }
        return ret.sub;
    }

    /**
     * 詰め方の工夫をするターン
     *
     * @param <X> 戻り型
     * @param pac 解析対象
     * @param ns name space
     * @param parsers サブ要素のパーサー
     * @return サブ要素を含む解析結果
     */
    @Override
    public <X> Match<X> find(ReadableBlock pac, Object ns, BNFParser<? extends X>... parsers) {
        Match<X> ret = buildFind(pac, ns, parsers );
        if ( ret != null ) {
            ret.end(pac);
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
    abstract <X> Match<X> buildFind(ReadableBlock pac, Object ns, BNFParser<? extends X>... parsers);
}
