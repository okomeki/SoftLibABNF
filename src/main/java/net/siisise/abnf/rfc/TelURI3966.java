package net.siisise.abnf.rfc;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser5234.ABNF5234;

/**
 * URI RFC 2396 に Tel URL RFC 2806をのせる
 * URIが2732,3986と改定されているので微妙
 * BASEは同じなのでRFC 2234からRFC 5234に変更
 * faxとmodemは廃止された
 * @author okome
 */
public class TelURI3966 {
    public static final ABNFReg REG = new ABNFReg(ABNF5234.BASE);
    // まだ
    static final ABNF reserved = REG.rule("reserved", ABNF.list(";/?:@&=+$,"));
    static final ABNF alphanum = REG.rule("alphanum", ABNF5234.ALPHA.or(ABNF5234.DIGIT));
    static final ABNF visualSeparator = REG.rule("visual-separator","\"-\" / \".\" / \"(\" / \")\"");
    static final ABNF phonedigitHex = REG.rule("phonedigit-hex","HEXDIG / \"*\" / \"#\" / [ visual-separator ]");
    static final ABNF phonedigit = REG.rule("phonedigit","DIGIT / [ visual-separator ]");
    static final ABNF paramUnreserved = REG.rule("param-unreserved","\"[\" / \"]\" / \"/\" / \":\" / \"&\" / \"+\" / \"$\"");
    static final ABNF pctEncoded = REG.rule("pct-encoded","\"%\" HEXDIG HEXDIG");
    static final ABNF mark = REG.rule("mark", ABNF.list("-_.!~*'()"));
    static final ABNF unreserved = REG.rule("unreserved",alphanum.or(mark));
    static final ABNF uric = REG.rule("uric",reserved.or(unreserved, pctEncoded));
    static final ABNF paramchar = REG.rule("paramchar",paramUnreserved.or(unreserved,pctEncoded));
    static final ABNF pvalue = REG.rule("pvalue",paramchar.ix());
    static final ABNF pname = REG.rule("pname","1*( alphanum / \"-\" )");
    static final ABNF parameter = REG.rule("parameter","\";\" pname [\"=\" pvalue ]");
    /** RFC 2396 改変 */
//    static final ABNF toplabel = REG.rule("toplabel","ALPHA / ALPHA *( alphanum / \"-\" ) alphanum");
    static final ABNF toplabel = REG.rule("toplabel", ABNF5234.ALPHA.pl( ABNF.bin('-').x().pl(alphanum).x()));    
    /** RFC 2396 改変 */
//    static final ABNF domainlabel = REG.rule("domainlabel","alphanum / alphanum *( alphanum / \"-\" ) alphanum");
    static final ABNF domainlabel = REG.rule("domainlabel",   "alphanum *( *( \"-\" ) alphanum )");
    static final ABNF domainname = REG.rule("domainname","*( domainlabel \".\" ) toplabel [ \".\" ]");
    static final ABNF localNumberDigits = REG.rule("local-number-digits","*phonedigit-hex (HEXDIG / \"*\" / \"#\")*phonedigit-hex");
    static final ABNF globalNumberDigits = REG.rule("global-number-digits","\"+\" *phonedigit DIGIT *phonedigit");
    static final ABNF descriptor = REG.rule("descriptor",domainname.or(globalNumberDigits));
    static final ABNF context = REG.rule("context","\";phone-context=\" descriptor");
    static final ABNF extension = REG.rule("extension","\";ext=\" 1*phonedigit");
    static final ABNF isdnSubaddress = REG.rule("isdn-subaddress","\";isub=\" 1*uric");
    static final ABNF par = REG.rule("par",parameter.or(extension, isdnSubaddress));
    static final ABNF localNumber = REG.rule("local-number","local-number-digits *par context *par");
    static final ABNF globalNumber = REG.rule("global-number","global-number-digits *par");
    static final ABNF telephoneSubscriber = REG.rule("telephone-subscriber",globalNumber.or(localNumber));
    static final ABNF telephoneUri = REG.rule("telephone-uri","\"tel:\" telephone-subscriber");
    
}
