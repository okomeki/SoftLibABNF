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

import java.util.List;
import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.ABNFplm;
import net.siisise.bnf.parser.BNFList;

/**
 * 並びを結合する。
 * 類似した要素の解析で、あなろぐなRFCのABNFの想定と一致しない場合もある。
 * 要素が1つの場合はABNFplを省略している。
 * RFC 5432
 */
public class Concatenation extends BNFList<ABNF, ABNF> {

    public Concatenation(ABNF rule, ABNFReg base) {
        super(rule, base, "repetition");
    }

    /**
     * 
     * @param val 子のABNF要素
     * @return 繋げた結果のABNF Concatenation
     */
    @Override
    protected ABNF build(List<ABNF> val) {
        if (val.size() == 1) {
            return val.get(0);
        }
        return new ABNFplm(val.toArray(new ABNF[val.size()]));
    }

}
