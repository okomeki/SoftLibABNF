package net.siisise.abnf.parser;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;

/**
 * 
 */
public class ABNFStringParser extends ABNFBuildParser<String,ABNF> {

    public ABNFStringParser(ABNF rule) {
        super(rule, null);
    }

    public ABNFStringParser(ABNF rule, ABNFReg base) {
        super(rule, base);
    }

    /**
     * find専用なのでなにもしていない
     * @param val
     * @return 
     */
    @Override
    public String build(ABNF.C val) {
        return str(val.ret);
    }
    
}
