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
package net.siisise.abnf.parser7405;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.bnf.BNF;
import net.siisise.bnf.parser.BNFBuildParser;

public class IS extends BNFBuildParser<ABNF, String> {

    public IS(ABNF rule, ABNFReg base) {
        super(rule, base, "quoted-string");
    }

    @Override
    protected ABNF build(BNF.C<String> src) {
        return ABNF.text(src.get("quoted-string").get(0));
    }

}
