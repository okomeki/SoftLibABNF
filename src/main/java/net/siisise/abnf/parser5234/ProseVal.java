package net.siisise.abnf.parser5234;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser.ABNFBaseParser;
import net.siisise.io.FrontPacket;
import net.siisise.io.Packet;

/**
 * コメント的なもの
 * 対応していないので止まりたい
 */
public class ProseVal extends ABNFBaseParser<ABNF, ABNF> {

    public ProseVal(ABNF abnf, ABNFReg base) {
        super(abnf);
    }

    @Override
    public ABNF parse(FrontPacket pac) {
        Packet p = rule.is(pac);
        if ( p == null ) {
            return null;
        }
        throw new UnsupportedOperationException("Not supported yet." + strd(pac)); //To change body of generated methods, choose Tools | Templates.
    }
    
}
