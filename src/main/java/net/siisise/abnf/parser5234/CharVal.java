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

import java.nio.charset.StandardCharsets;
import net.siisise.abnf.ABNF;
import net.siisise.bnf.BNF;
import net.siisise.bnf.BNFReg;
import net.siisise.bnf.parser.BNFBaseParser;
import net.siisise.io.Packet;
import net.siisise.pac.ReadableBlock;

/**
 *
 */
public class CharVal extends BNFBaseParser<ABNF> {

    public CharVal(BNF rule, BNFReg base) {
        super(rule);
    }

    @Override
    public ABNF parse(ReadableBlock pac) {
        Packet p = rule.is(pac);
        if (p == null) {
            return null;
        }
        p.backRead();
        p.read();
        byte[] d = p.toByteArray();
        return ABNF.text(new String(d, StandardCharsets.UTF_8));
    }
}
