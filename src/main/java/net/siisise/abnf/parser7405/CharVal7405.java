package net.siisise.abnf.parser7405;

import java.util.List;
import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser.ABNFBaseParser;
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
    public ABNF parse(Packet pac) {
        ABNF.B ret = def.find(pac,"case-insensitive-string","case-sensitive-string","quoted-string");
        if (ret == null) {
            return null;
        }
        List<Packet> in = ret.get("case-insensitive-string");
        List<Packet> sen = ret.get("case-sensitive-string");
        Packet qs = (Packet) ret.get("quoted-string").get(0);
        qs.read();
        qs.backRead();
        String val = str(qs);
        if ( in != null ) {
            return ABNF.text(val);
        } else if ( sen != null ) {
            return ABNF.bin(val);
        }
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
