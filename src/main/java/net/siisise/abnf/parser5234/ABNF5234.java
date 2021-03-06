package net.siisise.abnf.parser5234;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;

/**
 * ABNFの定義をJavaで書いてみたりしたもの.
 * RFC 7230, RFC 7405 などに拡張がある. (対応済み)
 * ABNFは最長一致判定が苦手なので使い方によっては書き換える必要もある (ABNFplmで解決)
 *
 */
public class ABNF5234 {

    public static final ABNFReg BASE = new ABNFReg((ABNFReg)null, null);

    // classのようなもの RFC 2234 6.1 Core Rules から変わらず
    public static final ABNF ALPHA = BASE.rule("ALPHA", ABNF.range(0x41, 0x5a).or(ABNF.range(0x61, 0x7a)));
    public static final ABNF BIT = BASE.rule("BIT", ABNF.bin('0').or(ABNF.bin('1')));
    public static final ABNF CHAR = BASE.rule("CHAR", ABNF.range(0x01, 0x7f));
    public static final ABNF CR = BASE.rule("CR", ABNF.bin(0x0d));
    public static final ABNF LF = BASE.rule("LF", ABNF.bin(0x0a));
    public static final ABNF CRLF = BASE.rule("CRLF", ABNF.bin("\r\n"));
    public static final ABNF CTL = BASE.rule("CTL", ABNF.range(0x00, 0x1f).or(ABNF.bin(0x7f)));
    public static final ABNF DIGIT = BASE.rule("DIGIT", ABNF.range(0x30, 0x39));
    public static final ABNF DQUOTE = BASE.rule("DQUOTE", ABNF.bin(0x22));
    public static final ABNF HEXDIG = BASE.rule("HEXDIG", DIGIT.or(ABNF.range('a', 'f'), ABNF.range('A', 'F')));
    public static final ABNF HTAB = BASE.rule("HTAB", ABNF.bin(0x09));
    public static final ABNF OCTET = BASE.rule("OCTET", ABNF.range(0x0, 0xff));
    public static final ABNF SP = BASE.rule("SP", ABNF.bin(0x20));
    public static final ABNF VCHAR = BASE.rule("VCHAR", ABNF.range(0x21, 0x7e));
    public static final ABNF WSP = BASE.rule("WSP", SP.or(HTAB));
    public static final ABNF LWSP = BASE.rule("LWSP", WSP.or(CRLF.pl(WSP)).x());

    /**
     * 各ABNFの定義をParserを使わずJavaで書いたもの
     * 
     */
    public static final ABNFReg REG = new ABNFReg(BASE, null);

    public static final ABNF charVal = REG.rule("char-val", CharVal.class, DQUOTE.pl(ABNF.range(0x20, 0x21).or(ABNF.range(0x23, 0x7e)).x(), DQUOTE));
    public static final ABNF binVal = REG.rule("bin-val", ABNF.text('b').pl(BIT.ix(), ABNF.bin('.').pl(BIT.ix()).ix().or(ABNF.bin('-').pl(BIT.ix())).c()));
    public static final ABNF decVal = REG.rule("dec-val", ABNF.text('d').pl(DIGIT.ix(), ABNF.bin('.').pl(DIGIT.ix()).ix().or(ABNF.bin('-').pl(DIGIT.ix())).c()));
    public static final ABNF hexVal = REG.rule("hex-val", ABNF.text('x').pl(HEXDIG.ix(), ABNF.bin('.').pl(HEXDIG.ix()).ix().or(ABNF.bin('-').pl(HEXDIG.ix())).c()));
    public static final ABNF proseVal = REG.rule("prose-val", ProseVal.class, ABNF.text('<').pl(ABNF.range(0x20, 0x3d).or(ABNF.range(0x3f, 0x7e)).x(), ABNF.bin('>')));
    public static final ABNF numVal = REG.rule("num-val", NumVal.class, ABNF.text('%').pl(binVal.or(decVal, hexVal)));
    public static final ABNF rulename = REG.rule("rulename", Rulename.class, ALPHA.pl(ALPHA.or(DIGIT, ABNF.bin('-')).x()));
    public static final ABNF comment = REG.rule("comment", ABNF.bin(';').pl(WSP.or(VCHAR).x(), CRLF));
//    public static final ABNF repeat = REG.rule("repeat",DIGIT.ix().or(DIGIT.x().pl(new ABNFbin('*'), DIGIT.x())));
    /**
     * orで短いものをそれを含む長いものの前に配置すると誤判定するので順序を入れ換えるなど
     */
    public static final ABNF repeat = REG.rule("repeat", DIGIT.x().pl(ABNF.bin('*'), DIGIT.x()).or(DIGIT.ix()));
    public static final ABNF repetition = REG.rule("repetition", Repetition.class, repeat.c().pl(REG.ref("element")));
    static final ABNF cNl = REG.rule("c-nl", comment.or(CRLF));
    static final ABNF cWsp = REG.rule("c-wsp", WSP.or(cNl.pl(WSP)));
    public static final ABNF concatenation = REG.rule("concatenation", Concatenation.class, repetition.pl(cWsp.ix().pl(repetition).x()));
    public static final ABNF alternation = REG.rule("alternation", Alternation.class, concatenation.pl(cWsp.x().pl(ABNF.text('/'), cWsp.x(), concatenation).x()));
    public static final ABNF group = REG.rule("group", Group.class, ABNF.bin('(').pl(cWsp.x(), alternation, cWsp.x(), ABNF.bin(')')));
    public static final ABNF option = REG.rule("option", Option.class, ABNF.bin('[').pl(cWsp.x(), alternation, cWsp.x(), ABNF.bin(']')));
    public static final ABNF element = REG.rule("element", Element.class, rulename.or(group, option, charVal, numVal, proseVal));
    public static final ABNF elements = REG.rule("elements", Elements.class, alternation.pl(cWsp.x()));
    public static final ABNF definedAs = REG.rule("defined-as", cWsp.x().pl(ABNF.bin('=').or(ABNF.bin("=/")), cWsp.x()));
    public static final ABNF rule = REG.rule("rule", Rule.class, rulename.pl(definedAs, elements, cNl));
    public static final ABNF rulelist = REG.rule("rulelist", Rulelist.class, rule.or(cWsp.x().pl(cNl)).ix());

    /**
     * 複製できる弱結合版
     *
     * @return
     */
    public static ABNFReg copyREG() {
        ABNFReg reg = new ABNFReg(BASE, null);

        reg.rule("char-val", CharVal.class, ABNF5234.charVal);
        reg.rule("bin-val", ABNF5234.binVal);
        reg.rule("dec-val", ABNF5234.decVal);
        reg.rule("hex-val", ABNF5234.hexVal);
        reg.rule("prose-val", ProseVal.class, ABNF5234.proseVal);
        reg.rule("num-val", NumVal.class, ABNF.text('%').pl(reg.ref("bin-val").or(reg.ref("dec-val"), reg.ref("hex-val"))));
        reg.rule("rulename", Rulename.class, ABNF5234.rulename);
        reg.rule("element", Element.class, reg.ref("rulename").or(reg.ref("group"), reg.ref("option"), reg.ref("char-val"), reg.ref("num-val"), reg.ref("prose-val")));
        reg.rule("comment", ABNF5234.comment);
        reg.rule("repeat", DIGIT.x().pl(ABNF.bin('*'), DIGIT.x()).or(DIGIT.ix()));
        reg.rule("repetition", Repetition.class, reg.ref("repeat").c().pl(reg.ref("element")));
        reg.rule("c-nl", reg.ref("comment").or(CRLF));
        reg.rule("c-wsp", WSP.or(cNl.pl(WSP)));
        reg.rule("concatenation", Concatenation.class, reg.ref("repetition").pl(cWsp.ix().pl(reg.ref("repetition")).x()));
        reg.rule("alternation", Alternation.class, reg.ref("concatenation").pl(cWsp.x().pl(ABNF.text('/'), cWsp.x(), reg.ref("concatenation")).x()));
        reg.rule("group", Group.class, ABNF.bin('(').pl(cWsp.x(), reg.ref("alternation"), cWsp.x(), ABNF.bin(')')));
        reg.rule("option", Option.class, ABNF.bin('[').pl(cWsp.x(), reg.ref("alternation"), cWsp.x(), ABNF.bin(']')));
        reg.rule("elements", Elements.class, reg.ref("alternation").pl(cWsp.x()));
        reg.rule("defined-as", ABNF5234.definedAs);
        reg.rule("rule", Rule.class, reg.ref("rulename").pl(definedAs, reg.ref("elements"), cNl));
        reg.rule("rulelist", Rulelist.class, reg.ref("rule").or(cWsp.x().pl(cNl)).ix());

        return reg;
    }

}
