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
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser.ABNFSelect;

/**
 * 数値系まとめてParser
 */
public class NumVal extends ABNFSelect<ABNF> {

    public NumVal(ABNF rule, ABNFReg base) {
        super(rule, base, "bin-val", "dec-val", "hex-val");
    }

    public static class BinVal extends NumSub {

        public BinVal(ABNF rule, ABNFReg base) {
            super(rule, ABNF5234.BIT, 2, 'b', 'B');
        }
    }

    public static class DecVal extends NumSub {

        public DecVal(ABNF rule, ABNFReg base) {
            super(rule, ABNF5234.DIGIT, 10, 'd', 'D');
        }
    }

    public static class HexVal extends NumSub {

        public HexVal(ABNF rule, ABNFReg base) {
            super(rule, ABNF5234.HEXDIG, 16, 'x', 'X');
        }
    }
}
