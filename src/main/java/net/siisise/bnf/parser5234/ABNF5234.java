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
package net.siisise.bnf.parser5234;

import net.siisise.abnf.parser5234.Alternation;
import net.siisise.abnf.parser5234.CharVal;
import net.siisise.abnf.parser5234.Concatenation;
import net.siisise.abnf.parser5234.Element;
import net.siisise.abnf.parser5234.NumVal;
import net.siisise.abnf.parser5234.Option;
import net.siisise.abnf.parser5234.ProseVal;
import net.siisise.abnf.parser5234.Repetition;
import net.siisise.abnf.parser5234.Rule;
import net.siisise.abnf.parser5234.Rulelist;
import net.siisise.abnf.parser5234.Rulename;
import net.siisise.abnf.parser5234.SubAlternation;
import net.siisise.bnf.BNF;
import net.siisise.bnf.BNFCC;
import net.siisise.bnf.BNFReg;
import net.siisise.bnf.parser.BNFStringParser;

/**
 * BNF系のclassでABNFを作る実験
 */
public class ABNF5234 {

    public static final BNFReg<BNF> BASE = new BNFReg((BNFReg)null, null);

    // classのようなもの RFC 2234 6.1 Core Rules から変わらず
    public static final BNF ALPHA = BASE.rule("ALPHA", BNF.range(0x41, 0x5a).or1(BNF.range(0x61, 0x7a)));
    public static final BNF BIT = BASE.rule("BIT", BNF.bin('0').or1(BNF.bin('1')));
    public static final BNF CHAR = BASE.rule("CHAR", BNF.range(0x01, 0x7f));
    public static final BNF CR = BASE.rule("CR", BNF.bin(0x0d));
    public static final BNF LF = BASE.rule("LF", BNF.bin(0x0a));
    public static final BNF CRLF = BASE.rule("CRLF", BNF.bin("\r\n"));
    public static final BNF CTL = BASE.rule("CTL", BNF.range(0x00, 0x1f).or1(BNF.bin(0x7f)));
    public static final BNF DIGIT = BASE.rule("DIGIT", BNF.range(0x30, 0x39));
    public static final BNF DQUOTE = BASE.rule("DQUOTE", BNF.bin(0x22));
    public static final BNF HEXDIG = BASE.rule("HEXDIG", DIGIT.or1(BNF.range('a', 'f'), BNF.range('A', 'F')));
    public static final BNF HTAB = BASE.rule("HTAB", BNF.bin(0x09));
    public static final BNF OCTET = BASE.rule("OCTET", BNF.range(0x0, 0xff));
    public static final BNF SP = BASE.rule("SP", BNF.bin(0x20));
    public static final BNF VCHAR = BASE.rule("VCHAR", BNF.range(0x21, 0x7e));
    public static final BNF WSP = BASE.rule("WSP", SP.or1(HTAB));
    public static final BNF LWSP = BASE.rule("LWSP", WSP.or1(CRLF.pl(WSP)).x());
    
    /**
     * 各ABNFの定義をParserを使わずJavaで書いたもの
     * 
     */
    public static final BNFCC<BNF> REG = BNFCC.abnf(BASE, null);

    public static final BNF charVal = REG.rule("char-val", CharVal.class, DQUOTE.pl(BNF.range(0x20, 0x21).or1(BNF.range(0x23, 0x7e)).x(), DQUOTE));
    public static final BNF binVal = REG.rule("bin-val", NumVal.BinVal.class, BNF.text('b').pl(BIT.ix(), BNF.bin('.').pl(BIT.ix()).ix().or(BNF.bin('-').pl(BIT.ix())).c()));
    public static final BNF decVal = REG.rule("dec-val", NumVal.DecVal.class, BNF.text('d').pl(DIGIT.ix(), BNF.bin('.').pl(DIGIT.ix()).ix().or(BNF.bin('-').pl(DIGIT.ix())).c()));
    public static final BNF hexVal = REG.rule("hex-val", NumVal.HexVal.class, BNF.text('x').pl(HEXDIG.ix(), BNF.bin('.').pl(HEXDIG.ix()).ix().or(BNF.bin('-').pl(HEXDIG.ix())).c()));
    public static final BNF proseVal = REG.rule("prose-val", ProseVal.class, BNF.text('<').pl(BNF.range(0x20, 0x3d).or1(BNF.range(0x3f, 0x7e)).x(), BNF.bin('>')));
    public static final BNF numVal = REG.rule("num-val", NumVal.class, BNF.text('%').pl(binVal.or1(decVal, hexVal)));
    public static final BNF rulename = REG.rule("rulename", Rulename.class, ALPHA.pl(ALPHA.or1(DIGIT, BNF.bin('-')).x()));
    public static final BNF comment = REG.rule("comment", BNF.bin(';').pl(WSP.or1(VCHAR).x(), CRLF));
//    public static final ABNF repeat = REG.rule("repeat",DIGIT.ix().or(DIGIT.x().pl(new ABNFbin('*'), DIGIT.x())));
    /**
     * orで短いものをそれを含む長いものの前に配置すると誤判定するので順序を入れ換えるなど
     */
    public static final BNF repeat = REG.rule("repeat", BNFStringParser.class, DIGIT.x().pl(BNF.bin('*'), DIGIT.x()).or1(DIGIT.ix()));
    public static final BNF repetition = REG.rule("repetition", Repetition.class, repeat.c().pl(REG.ref("element")));
    static final BNF cNl = REG.rule("c-nl", comment.or1(CRLF));
    static final BNF cWsp = REG.rule("c-wsp", WSP.or(cNl.pl(WSP)));
    public static final BNF concatenation = REG.rule("concatenation", Concatenation.class, repetition.pl(cWsp.ix().pl(repetition).x()));
    public static final BNF alternation = REG.rule("alternation", Alternation.class, concatenation.pl(cWsp.x().pl(BNF.text('/'), cWsp.x(), concatenation).x()));
    public static final BNF group = REG.rule("group", SubAlternation.class, BNF.bin('(').pl(cWsp.x(), alternation, cWsp.x(), BNF.bin(')')));
    public static final BNF option = REG.rule("option", Option.class, BNF.bin('[').pl(cWsp.x(), alternation, cWsp.x(), BNF.bin(']')));
    public static final BNF element = REG.rule("element", Element.class, rulename.or1(group, option, charVal, numVal, proseVal));
    public static final BNF elements = REG.rule("elements", SubAlternation.class, alternation.pl(cWsp.x()));
    public static final BNF definedAs = REG.rule("defined-as", BNFStringParser.class, cWsp.x().pl(BNF.bin('=').or(BNF.bin("=/")), cWsp.x()));
    public static final BNF rule = REG.rule("rule", Rule.class, rulename.pl(definedAs, elements, cNl));
    public static final BNF rulelist = REG.rule("rulelist", Rulelist.class, rule.or1(cWsp.x().pl(cNl)).ix());

    /**
     * 複製できる弱結合版
     *
     * @return 複製しやすい版
     */
    public static BNFCC<BNF> copyREG() {
        BNFCC<BNF> reg = BNFCC.abnf(BASE, null);

        reg.rule("char-val", CharVal.class, ABNF5234.charVal);
        reg.rule("bin-val", NumVal.BinVal.class, ABNF5234.binVal);
        reg.rule("dec-val", NumVal.DecVal.class, ABNF5234.decVal);
        reg.rule("hex-val", NumVal.HexVal.class, ABNF5234.hexVal);
        reg.rule("prose-val", ProseVal.class, ABNF5234.proseVal);
        reg.rule("num-val", NumVal.class, BNF.text('%').pl(reg.ref("bin-val").or1(reg.ref("dec-val"), reg.ref("hex-val"))));
        reg.rule("rulename", Rulename.class, ABNF5234.rulename);
        reg.rule("element", Element.class, reg.ref("rulename").or1(reg.ref("group"), reg.ref("option"), reg.ref("char-val"), reg.ref("num-val"), reg.ref("prose-val")));
        reg.rule("comment", ABNF5234.comment);
        reg.rule("repeat", BNFStringParser.class, DIGIT.x().pl(BNF.bin('*'), DIGIT.x()).or1(DIGIT.ix()));
        reg.rule("repetition", Repetition.class, reg.ref("repeat").c().pl(reg.ref("element")));
        reg.rule("c-nl", reg.ref("comment").or1(CRLF));
        reg.rule("c-wsp", WSP.or1(cNl.pl(WSP)));
        reg.rule("concatenation", Concatenation.class, reg.ref("repetition").pl(cWsp.ix().pl(reg.ref("repetition")).x()));
        reg.rule("alternation", Alternation.class, reg.ref("concatenation").pl(cWsp.x().pl(BNF.text('/'), cWsp.x(), reg.ref("concatenation")).x()));
        reg.rule("group", SubAlternation.class, BNF.bin('(').pl(cWsp.x(), reg.ref("alternation"), cWsp.x(), BNF.bin(')')));
        reg.rule("option", Option.class, BNF.bin('[').pl(cWsp.x(), reg.ref("alternation"), cWsp.x(), BNF.bin(']')));
        reg.rule("elements", SubAlternation.class, reg.ref("alternation").pl(cWsp.x()));
        reg.rule("defined-as", BNFStringParser.class, ABNF5234.definedAs);
        reg.rule("rule", Rule.class, reg.ref("rulename").pl(definedAs, reg.ref("elements"), cNl));
        reg.rule("rulelist", Rulelist.class, reg.ref("rule").or1(cWsp.x().pl(cNl)).ix());

        return reg;
    }
}
