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

import java.util.ArrayList;
import java.util.List;
import net.siisise.bnf.BNF;
import net.siisise.io.Packet;
import net.siisise.io.PacketA;
import net.siisise.lang.CodePoint;
import net.siisise.pac.ReadableBlock;

/**
 * 文字が含まれるかどうかのMapだかListだか
 * 大文字小文字は別物のバイナリ表記寄り
 */
public class ABNFmap extends IsABNF {

    private final List<Integer> map = new ArrayList<>();

    public ABNFmap() {
        name = "略";
    }

    public ABNFmap(String val) {
        ReadableBlock pac = ReadableBlock.wrap(val);
        while (pac.size() > 0) {
            int ch = CodePoint.utf8(pac);
            if (!map.contains(ch)) {
                map.add(ch);
            }
        }
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
        if (map.contains(ch)) {
            return new PacketA(bin8);
        }
        pac.seek(of);
        return null;
    }

    /**
     * 複製する.
     * @param reg 複製先
     * @return 複製
     */
    @Override
    public ABNF copy(ABNFReg reg) {
        ABNFmap nm = new ABNFmap();
        nm.map.addAll(map);
        return nm;
    }

    @Override
    public ABNF or(BNF... abnf) {
        List<Integer> tmap = new ArrayList<>();
        List<BNF> xabnf = new ArrayList<>();
        boolean n = true;
        for (BNF a : abnf) {
            if (n && a instanceof ABNFbin) {
                int ach = ((ABNFbin) a).ch();
                if (ach >= 0 && !map.contains(ach)) {
                    tmap.add(((ABNFbin) a).ch());
                }
            } else {
                n = false;
                xabnf.add(a);
            }
        }
        if (n) {
            ABNFmap nm = new ABNFmap();
            nm.map.addAll(map);
            nm.map.addAll(tmap);
            if (xabnf.isEmpty()) {
                return nm;
            }
            xabnf.add(0, nm);
            return new ABNFor(xabnf.toArray(new BNF[0]));
        }
        return super.or(abnf);
    }

}
