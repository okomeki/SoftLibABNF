package net.siisise.abnf.rfc;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;

/**
 *
 * @author okome
 */
public class IPv62373 {
    static final ABNFReg REG = new ABNFReg();
    
    static final ABNF hex4 = REG.rule("hex4","1*4HEXDIG");
    static final ABNF hexseq = REG.rule("hexseq","hex4 *( \":\" hex4)");
    static final ABNF hexpart = REG.rule("hexpart","hexseq [ \"::\" [ hexseq ] ] | \"::\" [ hexseq ]");

    static final ABNF IPv4address = REG.rule("IPv4address","1*3DIGIT \".\" 1*3DIGIT \".\" 1*3DIGIT \".\" 1*3DIGIT");
    static final ABNF IPv6address = REG.rule("IPv6address","hexpart [ \":\" IPv4address ]");
    
    static final ABNF IPv6prefix = REG.rule("IPv6prefix","hexpart \"/\" 1*2DIGIT");
    
}
