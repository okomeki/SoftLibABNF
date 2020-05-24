package net.siisise.abnf.parser5234;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser.ABNFBaseParser;
import net.siisise.io.FrontPacket;
import net.siisise.io.Packet;

/**
 *
 */
public class CharVal extends ABNFBaseParser<ABNF, ABNF> {

    public CharVal(ABNFReg reg) {
        super(ABNF5234.charVal);
    }

    public CharVal(ABNF abnf, ABNFReg reg, ABNFReg base) {
        super(abnf);
    }

    @Override
    public ABNF parse(FrontPacket pac) {
        Packet p = def.is(pac);
        if (p == null) {
            return null;
        }
        p.backRead();
        p.read();
        byte[] d = p.toByteArray();
        return ABNF.text(new String(d, ABNF.UTF8));
    }

}
