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
import net.siisise.abnf.ABNFCC;
import net.siisise.abnf.ABNFReg;
import net.siisise.bnf.BNFReg;
import net.siisise.bnf.parser.BNFStringParser;

/**
 * ABNFの定義をJavaで書いてみたりしたもの.
 * RFC 7230, RFC 7405 などに拡張がある. (対応済み)
 * ABNFは最長一致判定が苦手なので使い方によっては書き換える必要もある (ABNFplmで解決)
 *
 */
public class ABNF5234 {

    public static final ABNFReg BASE = new ABNFReg((BNFReg) null, null);

    // classのようなもの RFC 2234 6.1 Core Rules から変わらず
    public static final ABNF ALPHA = BASE.rule("ALPHA", ABNF.range(0x41, 0x5a).or1(ABNF.range(0x61, 0x7a)));
    public static final ABNF BIT = BASE.rule("BIT", ABNF.bin('0').or1(ABNF.bin('1')));
    public static final ABNF CHAR = BASE.rule("CHAR", ABNF.range(0x01, 0x7f));
    public static final ABNF CR = BASE.rule("CR", ABNF.bin(0x0d));
    public static final ABNF LF = BASE.rule("LF", ABNF.bin(0x0a));
    public static final ABNF CRLF = BASE.rule("CRLF", ABNF.bin("\r\n"));
    public static final ABNF CTL = BASE.rule("CTL", ABNF.range(0x00, 0x1f).or1(ABNF.bin(0x7f)));
    public static final ABNF DIGIT = BASE.rule("DIGIT", ABNF.range(0x30, 0x39));
    public static final ABNF DQUOTE = BASE.rule("DQUOTE", ABNF.bin(0x22));
    public static final ABNF HEXDIG = BASE.rule("HEXDIG", DIGIT.or1(ABNF.range('a', 'f'), ABNF.range('A', 'F')));
    public static final ABNF HTAB = BASE.rule("HTAB", ABNF.bin(0x09));
    public static final ABNF OCTET = BASE.rule("OCTET", ABNF.range(0x0, 0xff));
    public static final ABNF SP = BASE.rule("SP", ABNF.bin(0x20));
    public static final ABNF VCHAR = BASE.rule("VCHAR", ABNF.range(0x21, 0x7e));
    public static final ABNF WSP = BASE.rule("WSP", SP.or1(HTAB));
    public static final ABNF LWSP = BASE.rule("LWSP", WSP.or1(CRLF.pl(WSP)).x());

    // 拡張
    public static final ABNFReg EX = new ABNFReg((ABNFReg) null, null);
    // 大雑把な判定
    public static final ABNF HIRAGANA = EX.rule("HIRAGANA", ABNF.range(0x3041, 0x3096).or1(ABNF.range(0x3099, 0x309f)));
    public static final ABNF KATAKANA = EX.rule("KATAKANA", ABNF.range(0x30a0, 0x30ff));

    /**
     * 各ABNFの定義をParserを使わずJavaで書いたもの
     */
    public static final ABNFCC REG = new ABNFCC(BASE, null);

    public static final ABNF charVal = REG.rule("char-val", CharVal.class, DQUOTE.pl(ABNF.range(0x20, 0x21).or1(ABNF.range(0x23, 0x7e)).x(), DQUOTE));
    public static final ABNF binVal = REG.rule("bin-val", NumVal.BinVal.class, ABNF.text('b').pl(BIT.ix(), ABNF.bin('.').pl(BIT.ix()).ix().or(ABNF.bin('-').pl(BIT.ix())).c()));
    public static final ABNF decVal = REG.rule("dec-val", NumVal.DecVal.class, ABNF.text('d').pl(DIGIT.ix(), ABNF.bin('.').pl(DIGIT.ix()).ix().or(ABNF.bin('-').pl(DIGIT.ix())).c()));
    public static final ABNF hexVal = REG.rule("hex-val", NumVal.HexVal.class, ABNF.text('x').pl(HEXDIG.ix(), ABNF.bin('.').pl(HEXDIG.ix()).ix().or(ABNF.bin('-').pl(HEXDIG.ix())).c()));
    public static final ABNF proseVal = REG.rule("prose-val", ProseVal.class, ABNF.text('<').pl(ABNF.range(0x20, 0x3d).or1(ABNF.range(0x3f, 0x7e)).x(), ABNF.bin('>')));
    public static final ABNF numVal = REG.rule("num-val", NumVal.class, ABNF.text('%').pl(binVal.or1(decVal, hexVal)));
    public static final ABNF rulename = REG.rule("rulename", Rulename.class, ALPHA.pl(ALPHA.or1(DIGIT, ABNF.bin('-')).x()));
    public static final ABNF comment = REG.rule("comment", ABNF.bin(';').pl(WSP.or1(VCHAR).x(), CRLF));
//    public static final ABNF repeat = REG.rule("repeat",DIGIT.ix().or(DIGIT.x().pl(new ABNFbin('*'), DIGIT.x())));
    /**
     * orで短いものをそれを含む長いものの前に配置すると誤判定するので順序を入れ換えるなど
     */
    public static final ABNF repeat = REG.rule("repeat", BNFStringParser.class, DIGIT.x().pl(ABNF.bin('*'), DIGIT.x()).or1(DIGIT.ix()));
    public static final ABNF repetition = REG.rule("repetition", Repetition.class, repeat.c().pl(REG.ref("element")));
    static final ABNF cNl = REG.rule("c-nl", comment.or1(CRLF));
    static final ABNF cWsp = REG.rule("c-wsp", WSP.or1(cNl.pl(WSP)));
    public static final ABNF concatenation = REG.rule("concatenation", Concatenation.class, repetition.pl(cWsp.ix().pl(repetition).x()));
    public static final ABNF alternation = REG.rule("alternation", Alternation.class, concatenation.pl(cWsp.x().pl(ABNF.text('/'), cWsp.x(), concatenation).x()));
    public static final ABNF group = REG.rule("group", SubAlternation.class, ABNF.bin('(').pl(cWsp.x(), alternation, cWsp.x(), ABNF.bin(')')));
    public static final ABNF option = REG.rule("option", Option.class, ABNF.bin('[').pl(cWsp.x(), alternation, cWsp.x(), ABNF.bin(']')));
    public static final ABNF element = REG.rule("element", Element.class, rulename.or1(group, option, charVal, numVal, proseVal));
    public static final ABNF elements = REG.rule("elements", SubAlternation.class, alternation.pl(cWsp.x()));
    public static final ABNF definedAs = REG.rule("defined-as", BNFStringParser.class, cWsp.x().pl(ABNF.bin('=').or(ABNF.bin("=/")), cWsp.x()));
    public static final ABNF rule = REG.rule("rule", Rule.class, rulename.pl(definedAs, elements, cNl));
    public static final ABNF rulelist = REG.rule("rulelist", Rulelist.class, rule.or1(cWsp.x().pl(cNl)).ix());

    /**
     * 複製できる弱結合版
     *
     * @return 複製しやすい版
     */
    public static ABNFCC copyREG() {
        ABNFCC reg = new ABNFCC(BASE, null);

        reg.rule("char-val", CharVal.class, ABNF5234.charVal);
        reg.rule("bin-val", NumVal.BinVal.class, ABNF5234.binVal);
        reg.rule("dec-val", NumVal.DecVal.class, ABNF5234.decVal);
        reg.rule("hex-val", NumVal.HexVal.class, ABNF5234.hexVal);
        reg.rule("prose-val", ProseVal.class, ABNF5234.proseVal);
        reg.rule("num-val", NumVal.class, ABNF.text('%').pl(reg.ref("bin-val").or1(reg.ref("dec-val"), reg.ref("hex-val"))));
        reg.rule("rulename", Rulename.class, ABNF5234.rulename);
        reg.rule("element", Element.class, reg.ref("rulename").or1(reg.ref("group"), reg.ref("option"), reg.ref("char-val"), reg.ref("num-val"), reg.ref("prose-val")));
        reg.rule("comment", ABNF5234.comment);
        reg.rule("repeat", BNFStringParser.class, DIGIT.x().pl(ABNF.bin('*'), DIGIT.x()).or1(DIGIT.ix()));
        reg.rule("repetition", Repetition.class, reg.ref("repeat").c().pl(reg.ref("element")));
        reg.rule("c-nl", reg.ref("comment").or1(CRLF));
        reg.rule("c-wsp", WSP.or1(cNl.pl(WSP)));
        reg.rule("concatenation", Concatenation.class, reg.ref("repetition").pl(cWsp.ix().pl(reg.ref("repetition")).x()));
        reg.rule("alternation", Alternation.class, reg.ref("concatenation").pl(cWsp.x().pl(ABNF.text('/'), cWsp.x(), reg.ref("concatenation")).x()));
        reg.rule("group", SubAlternation.class, ABNF.bin('(').pl(cWsp.x(), reg.ref("alternation"), cWsp.x(), ABNF.bin(')')));
        reg.rule("option", Option.class, ABNF.bin('[').pl(cWsp.x(), reg.ref("alternation"), cWsp.x(), ABNF.bin(']')));
        reg.rule("elements", SubAlternation.class, reg.ref("alternation").pl(cWsp.x()));
        reg.rule("defined-as", BNFStringParser.class, ABNF5234.definedAs);
        reg.rule("rule", Rule.class, reg.ref("rulename").pl(definedAs, reg.ref("elements"), cNl));
        reg.rule("rulelist", Rulelist.class, reg.ref("rule").or1(cWsp.x().pl(cNl)).ix());

        return reg;
    }

}
