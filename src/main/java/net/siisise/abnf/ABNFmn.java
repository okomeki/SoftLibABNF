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

/**
 * ABNFにはないがマイナス演算
 * bの要素がaより小さいとうまくいくのかも
 */
public class ABNFmn extends IsABNF {

    private final BNF a;
    private final BNF b;

    ABNFmn(BNF a, BNF b) {
        this.a = a;
        this.b = b;
    }

    /**
     * 複製する.
     * @param reg 複製先
     * @return 複製
     */
    @Override
    public ABNFmn copy(ABNFReg reg) {
        return new ABNFmn(a.copy(reg), b.copy(reg));
    }

    @Override
    public ReadableBlock is(ReadableBlock pac, Object ns) {
        ReadableBlock p2 = b.is(pac, ns);
        if (p2 != null) {
            pac.back(p2.length());
            return null;
        }
        return a.is(pac, ns);
    }

    @Override
    public ReadableBlock is(ReadableBlock src) {
        return is(src, null);
    }
}
