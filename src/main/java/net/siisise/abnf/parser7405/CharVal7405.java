package net.siisise.abnf.parser7405;

import java.util.List;
import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser.ABNFBaseParser;
import net.siisise.io.FrontPacket;
import net.siisise.io.Packet;

/**
 *
 * @author okome
 */
public class CharVal7405 extends ABNFBaseParser<ABNF, ABNF> {

    public CharVal7405(ABNFReg reg) {
        super(ABNF7405.charVal);
    }

    @Override
    public ABNF parse(FrontPacket pac) {
        ABNF.C ret = rule.findPacket(pac,ABNF7405.caseInsensitiveString,ABNF7405.caseSensitiveString,ABNF7405.quotedString);
        if (ret == null) {
            return null;
        }
        List<Packet> insen = ret.get(ABNF7405.caseInsensitiveString);
        List<Packet> sen = ret.get(ABNF7405.caseSensitiveString);
        Packet qs = (Packet) ret.get(ABNF7405.quotedString).get(0);
        qs.read();
        qs.backRead();
        String val = str(qs);
        if ( insen != null ) {
            return ABNF.text(val);
        } else if ( sen != null ) {
            return ABNF.bin(val);
        }
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
