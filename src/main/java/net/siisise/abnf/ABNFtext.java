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
package net.siisise.abnf;

import net.siisise.block.ReadableBlock;
import net.siisise.bnf.BNFReg;

/**
 * casesensitiveではない方
 *
 * @see ABNFbin
 */
public class ABNFtext extends IsABNF {

    private final String text;
    private final byte[] utf8;

    /**
     * 1文字対象.
     * @param ch UTF-32 1文字
     */
    ABNFtext(int ch) {
        char[] chars = Character.toChars(ch);
        text = String.valueOf(chars);
        if (ch < 0x7f && ch >= 0x20 && ch != 0x22) {
            name = "\"" + text + "\"";
        } else {
            name = uhex(ch);
        }
        utf8 = text.getBytes(UTF8);
    }

    ABNFtext(String text) {
        this.text = text;
        name = "\"" + text + "\"";
        utf8 = text.getBytes(UTF8);
    }
    
    @Override
    public String toJava() {
        return "ABNF.text("+name+")";
    }

    /**
     * ABNFtextの複製.
     * @param reg 複製先
     * @return 複製
     */
    @Override
    public ABNFtext copy(BNFReg<ABNF> reg) {
        return new ABNFtext(text);
    }

    /**
     *
     * @param pac
     * @return 一致した結果
     */
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
        pac.back(size);
        return null;
    }
}
