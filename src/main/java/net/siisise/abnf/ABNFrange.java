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

import java.util.Arrays;
import net.siisise.block.ReadableBlock;
import net.siisise.bnf.BNF;
import net.siisise.bnf.BNFReg;
import net.siisise.lang.CodePoint;

/**
 * 文字範囲.
 */
public class ABNFrange extends IsABNF {

    private final int min;
    private final int max;

    /**
     * UTF-8 バイト列ではなく文字としてのコードで比較する.
     * 
     * @param min デコード後の最小
     * @param max デコード後の最大
     */
    public ABNFrange(int min, int max) {
        this.min = min;
        this.max = max;
        name = uhex(min) + "-" + uhex(max).substring(2);
    }

    @Override
    public ABNFrange copy(BNFReg<ABNF> reg) {
        return new ABNFrange(min, max);
    }

    @Override
    public ReadableBlock is(ReadableBlock rb) {
        if (rb.length() == 0) {
            return null;
        }
        long of = rb.backLength();
        int ch = CodePoint.utf8(rb);
        if (ch < 0) {
            return null;
        }
        if (ch >= min && ch <= max) {
            return rb.sub(of, rb.backLength() - of);
        }
        rb.seek(of);
        return null;
    }
    
    /**
     * 複数連続指定する.
     * 複数指定することもあるので.
     * 1文字想定なので 内部ではor1を使う.
     * @param min デコード後の最小
     * @param max デコード後の最大
     * @return 範囲を合成したもの
     */
    public orEx or(int min, int max) {
        return or1(min, max);
    }

    public orEx or1(int min, int max) {
        return new orEx(new BNF[] { this, new ABNFrange(min, max)});
    }

    public static class orEx extends ABNFor1 {
        orEx(BNF[] bnfs) {
            super(bnfs);
        }

        public ABNFor1 or(int min, int max) {
            return or1(min, max);
        }

        public ABNFor1 or1(int min, int max) {
            ABNF b = new ABNFrange(min, max);
            BNF[] nl = Arrays.copyOf(list,list.length + 1);
            nl[list.length] = new ABNFrange(min, max);
            list = nl;
            return this;
        }
    }
    
    public ABNF or(int... ranges) {
        BNF[] rs = new BNF[ranges.length / 2 + 1];
        rs[0] = this;
        for ( int i = 0; i < ranges.length; i+= 2) {
            rs[i/2 + 1] = new ABNFrange(ranges[i], ranges[i+1]);
        }
        return new ABNFor1(rs);
    }

    @Override
    public String toJava() {
        return "ABNF.range(0x" + Integer.toHexString(min) +", 0x" + Integer.toHexString(max) + ")";
    }
}
