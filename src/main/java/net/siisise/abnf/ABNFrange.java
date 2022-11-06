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
import net.siisise.io.Packet;
import net.siisise.io.PacketA;
import net.siisise.lang.CodePoint;

/**
 * 文字
 *
 */
public class ABNFrange extends IsABNF {

    private final int min;
    private final int max;

    public ABNFrange(int min, int max) {
        this.min = min;
        this.max = max;
        name = hex(min) + "-" + hex(max).substring(2);
    }

    @Override
    public ABNFrange copy(ABNFReg reg) {
        return new ABNFrange(min, max);
    }

    @Override
    public Packet is(ReadableBlock pac) {
        if (pac.length() == 0) {
            return null;
        }
        int of = pac.backSize();
        int ch = CodePoint.utf8(pac);
        if (ch < 0) {
            return null;
        }
        byte[] bin8 = CodePoint.utf8(ch);
        if (ch >= min && ch <= max) {
            return new PacketA(bin8);
        }
        pac.seek(of);
        return null;
    }
}
