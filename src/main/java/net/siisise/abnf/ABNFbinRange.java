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
package net.siisise.abnf;

import net.siisise.block.ReadableBlock;
import net.siisise.bnf.BNFReg;

/**
 * バイト的な範囲.
 * ABNFの書き方によっては文字コード以外にバイト列のこともあるので用意する。
 */
public class ABNFbinRange extends IsABNF {

    private int min, max;

    ABNFbinRange(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public ReadableBlock is(ReadableBlock src) {
        if (src.length() < 1) {
            return null;
        }
        int b = src.read();
        if (min <= b && b <= max) {
            return ReadableBlock.wrap(new byte[]{(byte) b});
        }
        src.back(1);
        return null;
    }

    @Override
    public ABNF copy(BNFReg<ABNF> reg) {
        return new ABNFbinRange(min, max);
    }

    public ABNF or(int min, int max) {
        return or1(new ABNFbinRange(min, max));
    }

    public ABNF or1(int min, int max) {
        return or1(new ABNFbinRange(min, max));
    }

    @Override
    public String toJava() {
        String minHex = toJavaCh((byte) min);
        String maxHex = toJavaCh((byte) max);
        return "ABNF.binRange(" + minHex + ", " + maxHex + ")";
    }

}
