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
import net.siisise.io.Packet;
import net.siisise.pac.ReadableBlock;

/**
 * isとfindをis側でなんとかする系
 */
public abstract class IsBNF extends AbstractBNF<BNF> {

    /**
     *
     * @param <N> name space type
     * @param pac data packet
     * @param ns name space
     * @return match part
     */
    @Override
    public <N> Packet is(ReadableBlock pac, N ns) {
        return is(pac);
    }

    @Override
    public <X,N> C<X> find(ReadableBlock pac, N ns, BNFParser<? extends X>... parsers) {
        Packet r = is(pac,ns);
        if (r == null) {
            return null;
        }
        return subBuild(new C(r), ns, matchParser(parsers));
    }
}
