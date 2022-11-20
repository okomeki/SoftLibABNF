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
 * isとfindをis側でなんとかする系
 */
public abstract class IsBNF extends AbstractBNF<BNF> {

    /**
     * 前方一致判定.
     * ns は捨ててもいいかな.
     * @param pac data packet
     * @param ns name space
     * @return match part
     */
    @Override
    public ReadableBlock is(ReadableBlock pac, Object ns) {
        return is(pac);
    }

    @Override
    public <X> Match<X> find(ReadableBlock pac, Object ns, BNFParser<? extends X>... parsers) {
        Match n = new Match(pac);
        ReadableBlock r = is(pac, ns);
        if (r == null) {
            return null;
        }
        n.end(pac);
        return subBuild(n, ns, matchParser(parsers));
    }
}
