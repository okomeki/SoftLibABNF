package net.siisise.ebnf.xml;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser5234.ABNF5234;

/**
 * XML用のEBNF (the Extended Backus-Naur Form)
 * XML 1.0 第5版 6.表記法
 * 
 * 正規表現に依存しているっぽいので簡単にする?
 * 名前がないので適当に付ける とても曖昧
 * 
 */
public class XMLEBNF {
    public static final ABNFReg BASE = new ABNFReg();
    
    static final ABNF BaseChar = BASE.rule("BaseChar", ABNF.range(0x41,0x5a).or(ABNF.range(0x61,0x7a), ABNF.range(0xc0,0xd6), ABNF.range(0xd8,0xf6), ABNF.range(0xf8,0x131),
                  ABNF.range(0x134,0x13e), ABNF.range(0x141, 0x148), ABNF.range(0x14a,0x17e), ABNF.range(0x180,0x1c3), ABNF.range(0x1cd, 0x1f0),
                  ABNF.range(0x1f4,0x1f5), ABNF.range(0x1fa,0x217), ABNF.range(0x250,0x2a8), ABNF.range(0x2bb,0x2c1), ABNF.bin(0x386), ABNF.range(0x388, 0x38a), ABNF.bin(0x38c)));
    static final ABNF Ideographic = BASE.rule("Ideographic", ABNF.range(0x4e00,0x9fa5).or( ABNF.bin(0x3007), ABNF.range(0x3021,0x3029)));
    static final ABNF Letter = BASE.rule("Letter", BaseChar.or(Ideographic));
//    static final ABNF CombiningChar = BASE.rule("CombiningChar", );
    
    static final ABNF xchar = BASE.rule("xchar", ABNF.bin("#x").pl(ABNF5234.HEXDIG.ix()));
    static final ABNF achar = BASE.rule("achar", ABNF5234.ALPHA.or(ABNF5234.DIGIT));
//    static final ABNF dbchar = BASE.rule("dbchar", ABNF.bin('"').pl(dxstring).pl(ABNF.bin('"')));
//    static final ABNF sbchar = BASE.rule("sbchar", ABNF.bin("'").pl(sxstring).pl(ABNF.bin("'")));
    
    public static final ABNF Char = BASE.rule("Char", ABNF.bin(0x09).or(ABNF.bin(0x0a), ABNF.bin(0x0d), ABNF.range(0x20, 0xd7ff), ABNF.range(0xe000, 0xfffd), ABNF.range(0x10000, 0x10ffff))); // [2]
    public static final ABNF S = BASE.rule("S", ABNF.bin(0x20).or(ABNF.bin(0x09), ABNF.bin(0x0d), ABNF.bin(0x0a))); // [3]
    public static final ABNF NameStartChar = BASE.rule("NameStartChar", ABNF.bin(':').or(ABNF.range('A','Z'), ABNF.bin('_'), ABNF.range('a','z'),
            ABNF.range(0xc0,0xd6), ABNF.range(0xd8,0xf6), ABNF.range(0xf8,0x2ff), ABNF.range(0x370,0x37d), ABNF.range(0x37f,0x1fff),
            ABNF.range(0x200c,0x200d), ABNF.range(0x2070,0x218f), ABNF.range(0x2c00,0x2fef), ABNF.range(0x3001,0xd7ff), ABNF.range(0xf900,0xfdcf), ABNF.range(0xfdf0,0xfffd), ABNF.range(0x10000,0xeffff))); // [4]
    public static final ABNF NameChar = BASE.rule("NameChar", NameStartChar.or(ABNF.bin("-."),ABNF.range('0','9'),ABNF.bin(0xb7), ABNF.range(0x300,0x36f), ABNF.range(0x203f,0x2040))); // [4a]
    public static final ABNF Name = BASE.rule("Name", NameStartChar.pl(NameChar.x())); // [5]
    public static final ABNF Names = BASE.rule("Names", Name.pl(ABNF.bin(0x20).pl(Name).x())); // [6]
    public static final ABNF Nmtoken = BASE.rule("Nmtoken", NameChar.ix()); // [7]
    public static final ABNF Nmtokens = BASE.rule("Nmtokens", Nmtoken.pl(ABNF.bin(0x20).pl(Nmtoken).x()));

//    public static final ABNF prolog;
//    public static final ABNF element;
//    public static final ABNF Misc;
//    public static final ABNF document = BASE.rule("document", prolog.pl(element, Misc)); // [1]
    
}
