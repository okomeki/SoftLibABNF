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

import net.siisise.bnf.parser.BNFParser;
import net.siisise.io.FrontPacket;
import net.siisise.io.Packet;

/**
 * isとfindの実装が必要だが
 * find側の実装だけで済ませたい処理.
 */
public abstract class FindBNF extends AbstractBNF {

    /**
     * 単純な比較をfind で処理する.
     * @param <N> user name space type
     * @param src source 解析対象
     * @param ns user name space 未使用時はnull可能
     * @return 一致した場合
     */
    @Override
    public <N> Packet is(FrontPacket src, N ns) {
        C ret = find(src,ns);
        if (ret == null) {
            return null;
        }
        return ret.ret;
    }
    
    /**
     * 単純な比較をfind で処理する.
     * @param src source 解析対象
     * @return 一致部分
     */
    @Override
    public Packet is(FrontPacket src) {
        return is(src, null);
    }
    
    /**
     *
     * @param <X> 戻り型参考
     * @param <N> user name space type 名前空間型
     * @param pac source 解析対象
     * @param ns user name space ユーザ名前空間
     * @param parsers sub parsers サブ要素パーサ群
     * @return 解析結果
     */
    @Override
    public <X,N> C<X> find(FrontPacket pac, N ns, BNFParser<? extends X>... parsers) {
        BNFParser<? extends X> mp = matchParser(parsers);
        C<X> ret = buildFind(pac, mp == null ? parsers : new BNFParser[0]);
        return ret != null ? subBuild(ret, ns, mp) : null;
    }
    
    abstract <X,N> C<X> buildFind(FrontPacket pac, N ns, BNFParser<? extends X>... parsers);
}
