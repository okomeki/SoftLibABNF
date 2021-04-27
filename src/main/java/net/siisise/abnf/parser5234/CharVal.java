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

    public CharVal(ABNF rule, ABNFReg reg, ABNFReg base) {
        super(rule);
    }

    @Override
    public ABNF parse(FrontPacket pac) {
        Packet p = rule.is(pac);
        if (p == null) {
            return null;
        }
        p.backRead();
        p.read();
        byte[] d = p.toByteArray();
        return ABNF.text(new String(d, ABNF.UTF8));
    }

}
