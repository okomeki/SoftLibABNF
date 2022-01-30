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
import net.siisise.abnf.parser.ABNFList;

/**
 * OptionのParser.
 * 繰り返し0*1に変換する
 */
public class Option extends ABNFList<ABNF,ABNF> {

    public Option(ABNF rule, ABNFReg base) {
        super(rule, base, "alternation");
    }

    @Override
    protected ABNF build(List<ABNF> pac) {
        return pac.get(0).c();
    }

}
