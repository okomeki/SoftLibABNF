package net.siisise.abnf.parser;

import net.siisise.abnf.ABNF;

/**
 * 
 */
public class ABNFStringParser extends ABNFBuildParser<String,ABNF> {

    public ABNFStringParser(ABNF rule) {
        super(rule, null);
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
