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
package net.siisise.bnf;

import java.util.ArrayList;
import java.util.List;
import net.siisise.block.ReadableBlock;
import net.siisise.lang.CodePoint;

/**
 *
 */
public class BNFmap extends IsBNF<BNF> {

    private final List<Integer> map = new ArrayList<>();

    @Override
    public ReadableBlock is(ReadableBlock pac) {
        if (pac.length() == 0) {
            return null;
        }
        int of = pac.backSize();
        int ch = CodePoint.utf8(pac);
        if (ch < 0) {
            return null;
        }
        byte[] bin8 = CodePoint.utf8(ch);
        if (map.contains(ch)) {
            return ReadableBlock.wrap(bin8);
        }
        pac.seek(of);
        return null;
    }

    /**
     * 複製する.
     * @param reg 複製先
     * @return 複製
     */
    @Override
    public BNF copy(BNFReg<BNF> reg) {
        BNFmap nm = new BNFmap();
        nm.map.addAll(map);
        return nm;
    }

    /**
     * 
     * @return Javaっぽく
     */
    @Override
    public String toJava() {
        StringBuilder sb = new StringBuilder();
        sb.append("BNF.binlist(\"");
        for ( Integer ch : map ) {
            switch (ch) { // Java なのでなんとかなる?
                case 0x08:  sb.append("\\b");   break;
                case 0x09:  sb.append("\\t");   break;
                case 0x0a:  sb.append("\\n");   break;
                case 0x0c:  sb.append("\\f");   break;
                case 0x0d:  sb.append("\\r");   break;
                case 0x22:  sb.append("\\\"");  break;
                case 0x5c:  sb.append("\\\\");  break;
                default:
                    char[] cp = Character.toChars(ch);
                    for ( char c : cp ) {
                        if ( c < 0x20 || c == 0x7f) {
                            sb.append("\\u");
                            String hex = "000" + Integer.toHexString(ch);
                            sb.append(hex.substring(hex.length() - 4));
                        }
                    }
                    sb.appendCodePoint(ch);
            }
        }
        sb.append("\")");
        return sb.toString();
    }
}
