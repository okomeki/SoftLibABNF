package net.siisise.abnf.parser5234;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser.ABNFBaseParser;
import net.siisise.io.Packet;

/**
 *
 * @author okome
 */
public class ProseVal extends ABNFBaseParser<ABNF,ABNF> {

    public ProseVal(ABNF abnf, ABNFReg reg, ABNFReg base) {
        super(abnf);
    }

    @Override
    public ABNF parse(Packet pac) {
        Packet p = def.is(pac);
        if ( p == null ) {
            return null;
        }
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
