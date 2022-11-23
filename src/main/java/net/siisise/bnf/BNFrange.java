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
package net.siisise.bnf;

import net.siisise.block.ReadableBlock;
import net.siisise.lang.CodePoint;

/**
 *
 */
public class BNFrange extends IsBNF<BNF> {
    private final int min;
    private final int max;

    public BNFrange(int min, int max) {
        this.min = min;
        this.max = max;
        name = hex(min) + "-" + hex(max).substring(2);
    }

    @Override
    public BNFrange copy(BNFReg<BNF> reg) {
        return new BNFrange(min, max);
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

}
