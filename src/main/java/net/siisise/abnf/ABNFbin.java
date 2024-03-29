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

import java.util.Arrays;
import net.siisise.block.ByteBlock;
import net.siisise.block.ReadableBlock;
import net.siisise.bnf.BNFReg;
import net.siisise.lang.CodePoint;

/**
 * バイナリ表現。
 * まだ utf-8 の範囲内で
 * 一文字に限らずかもしれない
 */
public class ABNFbin extends IsABNF {

    private final byte[] data;

    ABNFbin(int ch) { // " a-z A-Z, 0x80以降 を%表記、それ以外を文字表記
        if (ch >= 0x20 && ((ch != 0x22 && ch < 0x41) || (ch > 0x5a && ch < 0x61) || (ch > 0x7a && ch < 0x7f))) {
            name = "\"" + (char) ch + "\"";
        } else {
            name = uhex(ch);
        }

        data = CodePoint.utf8(ch);
    }

    /**
     * ascii ?
     * @param str null empty 不可
     */
    ABNFbin(String str) {
        StringBuilder sb = new StringBuilder(50);
        sb.append(uhex(str.charAt(0)));
        for (int i = 1; i < str.length(); i++) {
            sb.append(".");
            sb.append(uhex(str.charAt(i)).substring(2));
        }
        name = sb.toString();
        data = str.getBytes(UTF8);
    }

    /**
     * バイト列
     * @param src バイト列
     */
    ABNFbin(byte[] src) {
        data = src;
        name = "バイト列(" + src.length + ")";
        
        StringBuilder sb = new StringBuilder();
        
        if ( src.length > 0 ) {
            sb.append("%x");
            sb.append(binHex(src[0]));
            
            for ( int i = 1; i < src.length; i++ ) {
                    sb.append('.').append(binHex(src[i]));
            }
            name = sb.toString();
        }
    }
    
    static String binHex(byte b) {
        return Integer.toHexString(0x100 + (b & 0xff)).substring(1).toUpperCase();
    }

    /**
     * 複製する.
     * @param reg 複製先
     * @return 複製
     */
    @Override
    public ABNFbin copy(BNFReg<ABNF> reg) {
        return new ABNFbin(new String(data, UTF8));
    }

    /**
     * 比較
     *
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
        if ( dlsize == data.length && Arrays.equals(data, d)) {
            return pac.sub(pac.backLength() - data.length, data.length);
        }
        pac.back(dlsize);
        return null;
    }

    /**
     * 1文字の場合のみ
     * @return 1文字:コード 1文字以外:-1
     */
    public int ch() {
        ReadableBlock src = new ByteBlock(data);
        int ch = CodePoint.utf8(src); // 1文字デコード
        return src.length() == 0 ? ch : -1;
    }

    /**
     * @return Javaっぽくする
     */
    @Override
    public String toJava() {
        StringBuilder src = new StringBuilder("ABNF.bin(");
        if ( data.length == 1 ) {
            src.append(toJavaCh(data[0]));
        } else {
            src.append("new byte[] {");
            boolean st = true;
            for ( byte d : data ) {
                if ( st ) {
                    st = false;
                } else {
                    src.append(",");
                }
                src.append(toJavaCh(d));
            }
            src.append("}");
        }
        src.append(")");
        return src.toString();
    }
}
