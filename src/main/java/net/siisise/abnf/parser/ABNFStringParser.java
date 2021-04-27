package net.siisise.abnf.parser;

import net.siisise.abnf.ABNF;

/**
 * 
 */
public class ABNFStringParser extends ABNFBuildParser<String,ABNF> {

    public ABNFStringParser(ABNF rule) {
        super(rule);
    }

    /**
     * find専用なのでなにもしていない
     * @param pac
     * @return 
     */
    @Override
    protected String build(ABNF.C<ABNF> pac) {
        return str(pac.ret);
    }
    
}
