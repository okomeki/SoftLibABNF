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
import net.siisise.block.ReadableBlock;
import net.siisise.bnf.BNF;
import net.siisise.bnf.parser.BNFBaseParser;
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
    private final int dig; // 基数 2,10,16
    private final char a, b;

    /**
     * 
     * @param rule
     * @param numrule
     * @param dig 基数
     * @param a 判別文字小
     * @param b 判別文字大
     */
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
    public ABNF parse(ReadableBlock pac) {
        int c = pac.read();
        if (c != a && c != b) {
            pac.back(1);
            return null;
        }
        long nl1 = pac.length();
        int v = num(pac);
        nl1 -= pac.length();
        boolean oct1 = isBin(v, nl1);

        ReadableBlock r = hf.is(pac);
        if (r != null) {
            long nl2 = pac.length();
            int max = num(pac);
            nl2 -= pac.length();
            if ( oct1 && isBin(max,nl2)) { // 特殊解釈 %x80-%xff はバイナリ
                return ABNF.binRange(v, max);
            }
            return ABNF.range(v, max);
        }

        r = dot.is(pac);
        if (r == null) {
            if ( oct1 ) {
                return ABNF.bin(new byte[] {(byte)v});
            }
            return ABNF.bin(v);
        }
        Packet data = new PacketA();
        if ( oct1 ) {
            data.write(new byte[] {(byte)v});
        } else {
            data.write(CodePoint.utf8(v));
        }
        do {
            long nlx = pac.length();
            int c2 = num(pac);
            nlx -= pac.length();
            if ( isBin(c2,nlx) ) {
                data.write(c2);
            } else {
                data.write(CodePoint.utf8(c2));
            }
            r = dot.is(pac);
        } while (r != null);
        return ABNF.bin(data.toByteArray());
    }
    
    private boolean isBin(int v, long nl) {
        return ( dig == 2 && nl == 8 ) || ( dig == 16 && nl == 2 ) || (dig == 10 && nl < 4 && v < 256);
        
    }

    private int num(ReadableBlock pac) {
        ReadableBlock num = nrule.is(pac);
        return Integer.parseInt(str(num), dig);
    }
}
