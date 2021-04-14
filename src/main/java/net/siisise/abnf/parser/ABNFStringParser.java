package net.siisise.abnf.parser;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.io.FrontPacket;

/**
 * 
 */
public class ABNFStringParser extends ABNFBaseParser<String,ABNF> {

    public ABNFStringParser(ABNF rule, ABNFReg reg) {
        super(rule, reg);
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
