package net.siisise.abnf.rfc;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;

/**
 * RFC 1738のBNFをABNFに読み替えたもの
 * @author okome
 */
public class URL1738 {
    static final ABNFReg REG = new ABNFReg();
    
//    static ABNF scheme = REG.rule("scheme","1*[lowalpha / digit / \"*\" / \"-\" / \".\" ]");
    static ABNF otherurl = REG.rule("otherurl","genericurl");
    static ABNF url = REG.rule("url","httpurl / ftpurl / newsurl / nntpurl / telneturl / gopherurl / "
            + "waisurl / mailtourl / fileurl / prosperourl / otherurl");
}
