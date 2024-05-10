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

/**
 *
 */
public class BNFbinRange extends IsBNF {
    
    private int min, max;
    
    BNFbinRange(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public ReadableBlock is(ReadableBlock src) {
        int v = src.read();
        if ( min <= v && v <= max ) {
            ReadableBlock.wrap(new byte[] {(byte)v});
        }
        src.back(1);
        return null;
    }

    @Override
    public BNF copy(BNFReg reg) {
        return new BNFbinRange(min, max);
    }

    @Override
    public String toJava() {
        
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
