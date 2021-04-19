package net.siisise.abnf.parser;

import net.siisise.abnf.ABNF;
import net.siisise.io.FrontPacket;

/**
 * 
 */
public class ABNFStringParser extends ABNFBaseParser<String,ABNF> {

    public ABNFStringParser(ABNF rule) {
        super(rule);
    }

    /**
     * find専用なのでなにもしていない
     * @param pac
     * @return 
     */
    @Override
    public String parse(FrontPacket pac) {
        return str(pac);
    }
    
}
