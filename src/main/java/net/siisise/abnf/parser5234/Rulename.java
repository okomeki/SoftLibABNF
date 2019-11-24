package net.siisise.abnf.parser5234;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.AbstractABNF;
import net.siisise.abnf.parser.ABNFBaseParser;
import net.siisise.io.Packet;

/**
 *
 */
public class Rulename extends ABNFBaseParser<ABNF, ABNF> {

    public Rulename(ABNF def, ABNFReg reg, ABNFReg base) {
        super(def, reg, base);
    }

    @Override
    public ABNF parse(Packet pac) {
        Packet p = def.is(pac);
        if (p == null) {
            return null;
        }
        return reg.ref(AbstractABNF.str(p));
    }

}
