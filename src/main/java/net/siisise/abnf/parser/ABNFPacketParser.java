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
package net.siisise.abnf.parser;

import net.siisise.abnf.ABNF;
import net.siisise.io.FrontPacket;
import net.siisise.io.Packet;

/**
 * Packetに分割するだけ。
 * ABNFで指定の対象クラスには変換しない。
 * みかんせい?
 */
public class ABNFPacketParser extends ABNFBaseParser<Packet, ABNF> {

    public ABNFPacketParser(ABNF rule) {
        super(rule);
    }

    /**
     * find で使うだけなので判定していない
     * @param pac 解析対象
     * @return 解析結果を切り取ったPacket
     */
    @Override
    public Packet parse(FrontPacket pac) {
        Packet r = rule.is(pac);
        if ( r == null ) {
            return null;
        }
        return r;
    }

}
