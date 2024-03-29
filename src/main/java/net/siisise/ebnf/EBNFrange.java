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

import net.siisise.lang.CodePoint;
import net.siisise.block.ReadableBlock;
import net.siisise.bnf.BNFReg;

/**
 * 文字
 * EBNFにはないかも
 */
public class EBNFrange extends IsEBNF {

    private final int min;
    private final int max;

    public EBNFrange(int min, int max) {
        this.min = min;
        this.max = max;
        name = uhex(min) + "-" + uhex(max).substring(2); // ABNF風
    }

    @Override
    public EBNFrange copy(BNFReg<EBNF> reg) {
        return new EBNFrange(min, max);
    }

    @Override
    public ReadableBlock is(ReadableBlock pac) {
        if (pac.length() == 0) {
            return null;
        }
        long of = pac.backLength();
        int ch = CodePoint.utf8(pac);
        if (ch < 0) {
            pac.seek(of);
            return null;
        }
        byte[] bin8 = CodePoint.utf8(ch);
        if (ch >= min && ch <= max) {
            return ReadableBlock.wrap(bin8);
        }
        pac.seek(of);
        return null;
    }

    @Override
    public String toJava() {
        return "EBNF.range(0x" + Integer.toHexString(min) +", 0x" + Integer.toHexString(max) + ")";
    }
}

