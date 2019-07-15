package net.siisise.abnf.parser5234;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser.ABNFBaseParser;
import net.siisise.io.Packet;

/**
 *
 * @author okome
 */
public class CharVal extends ABNFBaseParser<ABNF, ABNF> {

    public CharVal(ABNFReg reg) {
        super(ABNF5234.charVal);
    }

    @Override
    public ABNF parse(Packet pac) {
        Packet p = def.is(pac);
        if (p == null) {
            return null;
        }
        p.backRead();
        p.read();
        byte[] d = p.toByteArray();
        try {
            return ABNF.text(new String(d, "utf-8"));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(CharVal.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
