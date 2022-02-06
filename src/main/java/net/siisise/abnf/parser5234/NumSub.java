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
package net.siisise.abnf.parser5234;

import net.siisise.abnf.ABNF;
import net.siisise.bnf.BNF;
import net.siisise.bnf.parser.BNFBaseParser;
import net.siisise.io.FrontPacket;
import net.siisise.io.Packet;
import net.siisise.io.PacketA;
import net.siisise.lang.CodePoint;

/**
 * 内部で使用する数値用Parser。
 * 数値の抽出でだいたい共通の部分。
 *
 */
class NumSub extends BNFBaseParser<ABNF> {

    private static final ABNF hf = ABNF.bin('-');
    private static final ABNF dot = ABNF.bin('.');

    private final BNF nrule;
    private final int dig;
    private final char a, b;

    NumSub(BNF rule, BNF numrule, int dig, char a, char b) {
        super(rule);
        nrule = numrule.ix();
        this.dig = dig;
        this.a = a;
        this.b = b;
    }

    /**
     * 数値を適度に料理する
     * @param pac ABNF文でーた
     * @return 適度にABNF化した結果
     */
    @Override
    public ABNF parse(FrontPacket pac) {
        int c = pac.read();
        if (c != a && c != b) {
            pac.backWrite(c);
            return null;
        }
        int val = num(pac);

        FrontPacket r = hf.is(pac);
        if (r != null) {
            int max = num(pac);
            return ABNF.range(val, max);
        }

        r = dot.is(pac);
        if (r == null) {
            return ABNF.bin(val);
        }
        Packet data = new PacketA();
        data.write(CodePoint.utf8(val));
        do {
            data.write(CodePoint.utf8(num(pac)));
            r = dot.is(pac);
        } while (r != null);
        return ABNF.bin(str(data));
    }

    private int num(FrontPacket pac) {
        FrontPacket num = nrule.is(pac);
        return Integer.parseInt(str(num), dig);
    }
}
