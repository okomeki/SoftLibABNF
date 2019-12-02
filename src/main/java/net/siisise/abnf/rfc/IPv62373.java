package net.siisise.abnf.rfc;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser5234.ABNF5234;

/**
 *
 */
public class IPv62373 {
    static final ABNFReg REG = new ABNFReg(ABNF5234.BASE);
    
    static final ABNF hex4 = REG.rule("hex4", ABNF5234.HEXDIG.x(1,4));
    static final ABNF hexseq = REG.rule("hexseq","hex4 *( \":\" hex4)");
    static final ABNF hexpart = REG.rule("hexpart","hexseq [ \"::\" [ hexseq ] ] | \"::\" [ hexseq ]");

    public static final ABNF IPv4address = REG.rule("IPv4address","1*3DIGIT \".\" 1*3DIGIT \".\" 1*3DIGIT \".\" 1*3DIGIT");
    public static final ABNF IPv6address = REG.rule("IPv6address","hexpart [ \":\" IPv4address ]");
    
    public static final ABNF IPv6prefix = REG.rule("IPv6prefix","hexpart \"/\" 1*2DIGIT");
    
}
