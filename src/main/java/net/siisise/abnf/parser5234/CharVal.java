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
import net.siisise.abnf.parser.ABNFBaseParser;
import net.siisise.io.FrontPacket;
import net.siisise.io.Packet;

/**
 *
 */
public class CharVal extends ABNFBaseParser<ABNF, ABNF> {

    public CharVal(ABNF rule, ABNFReg base) {
        super(rule);
    }

    @Override
    public ABNF parse(FrontPacket pac) {
        Packet p = rule.is(pac);
        if (p == null) {
            return null;
        }
        p.backRead();
        p.read();
        byte[] d = p.toByteArray();
        return ABNF.text(new String(d, ABNF.UTF8));
    }

}
