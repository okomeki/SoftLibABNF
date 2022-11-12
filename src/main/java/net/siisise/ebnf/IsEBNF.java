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
public abstract class IsEBNF extends AbstractEBNF {
    
    @Override
    public <N> ReadableBlock is(ReadableBlock pac, N ns) {
        return is(pac);
    }
   
    /**
     * sub要素のない場合の軽い対応
     * @param <X> パラメータっぽい型
     * @param <N>
     * @param pac 解析データ
     * @param ns name space
     * @param parsers サブ要素のparser
     * @return 処理結果
     */
    @Override
    public <X,N> C<X> find(ReadableBlock pac, N ns, BNFParser<? extends X>... parsers) {
        C n = new C(pac);
        ReadableBlock r = is(pac, ns);
        if (r == null) {
            return null;
        }
        n.end(pac);
        return subBuild(n, ns, matchParser(parsers));
    }
}
