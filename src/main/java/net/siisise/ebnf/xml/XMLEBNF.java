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
    static final ABNFReg BASE = new ABNFReg();
    
    static final ABNF BaseChar = BASE.rule("BaseChar", ABNF.range(0x41,0x5a));
    static final ABNF Letter = BASE.rule("Letter","BaseChar / Ideographic");
    
    static final ABNF xchar = BASE.rule("xchar", ABNF.bin("#x").pl(ABNF5234.HEXDIG.ix()));
    static final ABNF achar = BASE.rule("achar", ABNF5234.ALPHA.or(ABNF5234.DIGIT));
//    static final ABNF dbchar = BASE.rule("dbchar", ABNF.bin('"').pl(dxstring).pl(ABNF.bin('"')));
//    static final ABNF sbchar = BASE.rule("sbchar", ABNF.bin("'").pl(sxstring).pl(ABNF.bin("'")));
    
}
