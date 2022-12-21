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
package net.siisise.ebnf;

import net.siisise.block.ReadableBlock;
import net.siisise.bnf.BNFReg;

/**
 * casesensitiveではない方
 *
 * @see EBNFbin
 */
public class EBNFtext extends IsEBNF {

    private final String text;
    private final byte[] utf8;

    /**
     *
     * @param ch
     */
    EBNFtext(int ch) {
        char[] chars = Character.toChars(ch);
        text = String.valueOf(chars);
        if (ch < 0x7f && ch >= 0x20 && ch != 0x22) {
            name = "\"" + text + "\"";
        } else {
            name = hex(ch);
        }
        utf8 = text.getBytes(UTF8);
    }

    EBNFtext(String text) {
        this.text = text;
        name = "\"" + text + "\"";
        utf8 = text.getBytes(UTF8);
    }

    /**
     * ABNFtextの複製.
     * @param reg 複製先
     * @return 複製
     */
    @Override
    public EBNFtext copy(BNFReg<EBNF> reg) {
        return new EBNFtext(text);
    }

    /**
     * 1文字単位でor分割
     *
     * ABNFor(text) も同じ
     *
     * @param chlist orで処理する文字の列
     * @return 1文字毎にor分割したABNF
     */
    public static EBNF list(String chlist) {
        return new EBNFor(chlist);
    }

    @Override
    public ReadableBlock is(ReadableBlock pac) {
        if (pac.length() < 1) {
            return null;
        }
        byte[] d = new byte[utf8.length];
        int size = pac.read(d);
        if ( size < utf8.length ) {
            pac.back(size);
            return null;
        }
        String u;
        u = new String(d, UTF8);
        if (u.equalsIgnoreCase(text)) {
            return ReadableBlock.wrap(d);
        }
        pac.back(d.length);
        return null;
    }

    @Override
    public String toJava() {
        return "EBNF.text(" +text+ ")";
    }
}
