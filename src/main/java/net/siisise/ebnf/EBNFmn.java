/*
 * Copyright 2022 okome.
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

import net.siisise.bnf.BNF;
import net.siisise.io.Packet;
import net.siisise.pac.ReadableBlock;

/**
 *
 */
public class EBNFmn extends IsEBNF {

    private final BNF a;
    private final BNF b;

    EBNFmn(BNF a, BNF b) {
        this.a = a;
        this.b = b;
    }

    /**
     * 複製する.
     * @param reg 複製先
     * @return 複製
     */
    @Override
    public EBNFmn copy(EBNFReg reg) {
        return new EBNFmn(a.copy(reg), b.copy(reg));
    }

    @Override
    public <N> Packet is(ReadableBlock pac, N ns) {
        Packet p1 = a.is(pac, ns);
        if (p1 == null) {
            return null;
        }
        Packet p2 = b.is(p1, ns);
        if (p2 != null) {
            pac.back(p1.size() + p2.size());
            return null;
        }
        return p1;
    }

    @Override
    public Packet is(ReadableBlock src) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
