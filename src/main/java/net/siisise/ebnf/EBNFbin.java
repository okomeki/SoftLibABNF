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
package net.siisise.ebnf;

import java.util.Arrays;
import net.siisise.io.Packet;
import net.siisise.io.PacketA;
import net.siisise.lang.CodePoint;
import net.siisise.block.ReadableBlock;

/**
 * ABNFと同じ
 */
public class EBNFbin extends IsEBNF {

    private final byte[] data;

    EBNFbin(int ch) { // " a-z A-Z, 0x80以降 を%表記、それ以外を文字表記
        if (ch >= 0x20 && ((ch != 0x22 && ch < 0x41) || (ch > 0x5a && ch < 0x61) || (ch > 0x7a && ch < 0x7f))) {
            name = "\"" + (char) ch + "\"";
        } else {
            name = hex(ch);
        }

        data = CodePoint.utf8(ch);
    }

    /**
     * ascii ?
     * @param val 
     */
    EBNFbin(String val) {
        StringBuilder sb = new StringBuilder(50);
        sb.append(hex(val.charAt(0)));
        for (int i = 1; i < val.length(); i++) {
            sb.append(".");
            sb.append(hex(val.charAt(i)).substring(2));
        }
        name = sb.toString();
        data = val.getBytes(UTF8);
    }

    /**
     * 複製する.
     * @param reg 複製先
     * @return 複製
     */
    @Override
    public EBNFbin copy(EBNFReg reg) {
        return new EBNFbin(new String(data, UTF8));
    }

    /**
     * 比較
     * @param pac 比較対象
     * @return 一致した場合pacと同じ 一致しなければnull
     */
    @Override
    public ReadableBlock is(ReadableBlock pac) {
        if (pac.length() < 1) {
            return null;
        }
        byte[] d = new byte[data.length];
        int dlsize = pac.read(d);
        if ( dlsize < data.length || !Arrays.equals(data, d)) {
            pac.back(dlsize);
            return null;
        }
        return ReadableBlock.wrap(d);
    }
}
