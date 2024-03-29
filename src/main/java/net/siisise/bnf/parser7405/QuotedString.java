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
package net.siisise.bnf.parser7405;

import net.siisise.bnf.BNF;
import net.siisise.bnf.BNFReg;
import net.siisise.bnf.parser.BNFBuildParser;
import net.siisise.io.Packet;

public class QuotedString extends BNFBuildParser<String, Packet> {

    public QuotedString(BNF rule, BNFReg base) {
        super(rule, base);
    }

    @Override
    protected String build(BNF.Match<Packet> src) {
        return str(src.sub.sub(1, src.sub.length() - 2));
    }

}
