package net.siisise.abnf.parser;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.io.FrontPacket;

/**
 *
 */
public class ABNFStringParser extends ABNFBaseParser<String,ABNF> {

    public ABNFStringParser(ABNF def, ABNFReg reg) {
        super(def, reg);
    }

    @Override
    public String parse(FrontPacket pac) {
        return str(pac);
    }
    
}
