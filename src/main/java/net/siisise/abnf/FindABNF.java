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

import net.siisise.bnf.parser.BNFParser;
import net.siisise.io.FrontPacket;
import net.siisise.io.Packet;

/**
 * findの分離
 * サブ要素を持つ方
 */
public abstract class FindABNF extends AbstractABNF {

    @Override
    public Packet is(FrontPacket src) {
        return is(src, null);
    }
    
    @Override
    public <N> Packet is(FrontPacket src, N ns) {
        C ret = find(src, ns);
        if (ret == null) {
            return null;
        }
        return ret.ret;
    }
    
    public <X> C<X> find(FrontPacket pac, BNFParser<? extends X>... parsers) {
        return find(pac, null, parsers);
    }

    /**
     * 詰め方の工夫をするターン
     *
     * @param <X> 戻り型
     * @param <N> name space type
     * @param pac 解析対象
     * @param ns name space
     * @param parsers サブ要素のパーサー
     * @return サブ要素を含む解析結果
     */
    @Override
    public <X,N> C<X> find(FrontPacket pac, N ns, BNFParser<? extends X>... parsers) {
        BNFParser<? extends X> mp = matchParser(parsers);
        C<X> ret = buildFind(pac, ns, mp == null ? parsers : new BNFParser[0]);
        return ret != null ? subBuild(ret, ns, mp) : null;
    }

    /**
     * find本体
     *
     * @param <X> 戻り型
     * @param <N> user name space type 名前空間型
     * @param pac データ
     * @param ns user name space ユーザ名前空間
     * @param parsers サブ要素のパーサ
     * @return サブ要素を含む解析結果
     */
    abstract protected <X,N> C<X> buildFind(FrontPacket pac, N ns, BNFParser<? extends X>... parsers);
}
