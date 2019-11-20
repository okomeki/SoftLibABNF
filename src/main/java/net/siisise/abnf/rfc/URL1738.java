package net.siisise.abnf.rfc;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;

/**
 * RFC 1738のBNFをABNFに読み替えたもの
 * まだ
 * @author okome
 */
public class URL1738 {
    static final ABNFReg REG = new ABNFReg();

    // FTP RFC 959
    static ABNF ftpurl = REG.rule("ftpurl","\"ftp://\" login [ \"/\" fpath [ \";type=\" ftptype ] ]");
    
    // FILE
    static ABNF fileurl = REG.rule("fileurl","\"file://\" [ host / \"localhost\" ] \"/\" fpath");
    
    static ABNF hsegment = REG.rule("hsegment","*( uchar / \";\" / \":\" / \"@\" / \"&\" / \"=\" )");
    static ABNF search = REG.rule("search","*( uchar / \";\" / \":\" / \"@\" / \"&\" / \"=\" )");
    static ABNF hpath = REG.rule("hpath","hsegment *( \"/\" hsegment )");
    static ABNF httpurl = REG.rule("httpurl","\"http://\" hostport [ \"/\" hpath [ \"?\" search ]]");

    static ABNF schemepart = REG.rule("schemepart","*xchar / ip-schemepart");
    static ABNF scheme = REG.rule("scheme","1*(lowalpha / digit / \"*\" / \"-\" / \".\" )");
    
    static ABNF genericurl = REG.rule("genericurl", "scheme \":\" schemepart");
    static ABNF otherurl = REG.rule("otherurl", "genericurl");
    static ABNF url = REG.rule("url", "httpurl / ftpurl / newsurl / nntpurl / telneturl / gopherurl / "
            + "waisurl / mailtourl / fileurl / prosperourl / otherurl");
}
