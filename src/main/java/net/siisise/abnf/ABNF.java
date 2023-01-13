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

import java.util.ArrayList;
import java.util.List;
import net.siisise.block.ReadableBlock;
import net.siisise.bnf.BNF;
import net.siisise.bnf.BNFReg;
import net.siisise.lang.CodePoint;

/**
 * RFC 5234.
 * BNFとABNFの差異抽出中。
 * 元はUS-ASCII 7bitコードを想定しているが、UTF-8として利用されている場合もあるので対応する
 * RegExpのPattern相当?
 * ABNFのparserは含まない。
 */
public interface ABNF extends BNF<ABNF> {

    /**
     * 大文字小文字を区別しない1文字。
     *
     * @param ch UCS2文字
     * @return 文字のABNF構文
     */
    static ABNF text(int ch) {
        return new ABNFtext(ch);
    }

    /**
     * 大文字小文字を区別しない文字列。
     *
     * @param text 文字列
     * @return 文字列のABNF構文
     */
    static ABNF text(String text) {
        return new ABNFtext(text);
    }

    /**
     * 1文字ずつAlternationで結合する。
     * text caseInsensitive っぽく
     * ABNFor1(text) も同じ
     * ABNFmap 推奨
     *
     * @param chlist 文字の列(UCS2単位)
     * @return 文字のor1繋ぎABNF構文
     */
    static ABNF list(String chlist) {
        ReadableBlock src = ReadableBlock.wrap(chlist);
        List<ABNF> abnfs = new ArrayList<>();
        while (src.length() > 0) {
            int c = CodePoint.utf8(src);
            abnfs.add(new ABNFtext(c));
        }
        return new ABNFor1(abnfs.toArray(new ABNF[abnfs.size()]));
    }

    /**
     * どれかの文字に一致するかな
     *
     * @param binlist バイト列的な文字列
     * @return どれかが含まれるABNF構文
     */
    static ABNF binlist(String binlist) {
        return new ABNFmap(binlist);
    }

    /**
     * 1文字 Unicodeふる
     * @param ch 文字
     * @return 文字と一致するABNF
     */
    static ABNFbin bin(int ch) {
        return new ABNFbin(ch);
    }

    /**
     * 文字列/文字との比較
     *
     * @param str utf-8で一致文字列
     * @return 一致するABNF
     */
    static ABNFbin bin(String str) {
        return new ABNFbin(str);
    }

    /**
     * (仮)
     * @param b バイト
     * @return 1バイトのABNF
     */
    static ABNFbin bin(byte b) {
        return new ABNFbin(new byte[] {b});
    }
    
    /**
     * (仮)
     * @param bin バイト列
     * @return バイト列のABNF
     */
    static ABNFbin bin(byte[] bin) {
        return new ABNFbin(bin);
    }

    /**
     * 文字の範囲.
     * utf-8 をデコードするので0x80以上はバイト列と同じではない
     * @param min 最小文字コード 0 - 0x10ffff
     * @param max 最大文字コード 0 - 0x10ffff
     * @return 文字のABNF
     */
    static ABNFrange range(int min, int max) {
        return new ABNFrange(min, max);
    }

    /**
     * 1バイト比較.
     * @param min 最小コード 0x00 - 0xff
     * @param max 最大コード 0x00 - 0xff
     * @return バイトのABNF
     */
    static ABNFbinRange binRange(int min, int max) {
        return new ABNFbinRange(min, max);
    }

    /**
     * 複製可能な構造を推奨(ループがあると複製は難しい)
     *
     * @param reg 複製先
     * @return ABNFの複製
     */
    @Override
    ABNF copy(BNFReg<ABNF> reg);
}
