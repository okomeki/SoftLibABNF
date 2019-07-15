package net.siisise.abnf.parser5234;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.AbstractABNF;
import net.siisise.abnf.parser.ABNFBaseParser;
import net.siisise.io.Packet;

/**
 *
 * @author okome
 */
public class Rulename extends ABNFBaseParser<ABNF, ABNF> {

    public Rulename(ABNFReg reg) {
        super(ABNF5234.rulename, reg);
    }

    public Rulename(ABNFReg reg, ABNFReg base) {
        super(ABNF5234.rulename, reg, base);
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
