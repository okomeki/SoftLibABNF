/*
 * Copyright 2022 Siisise Net.
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
package net.siisise.bnf.parser;

import net.siisise.bnf.BNF;
import net.siisise.io.Packet;
import net.siisise.pac.ReadableBlock;

/**
 * Packetに分割するだけ。
 * ABNFで指定の対象クラスには変換しない。
 * みかんせい?
 */
public class BNFPacketParser extends BNFBaseParser<Packet> {

    public BNFPacketParser(BNF rule) {
        super(rule);
    }

    @Override
    public Packet parse(ReadableBlock pac) {
        return rule.is(pac);
    }
}
