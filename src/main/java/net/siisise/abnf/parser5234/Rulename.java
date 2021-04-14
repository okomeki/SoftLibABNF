package net.siisise.abnf.parser5234;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.AbstractABNF;
import net.siisise.abnf.parser.ABNFBaseParser;
import net.siisise.io.FrontPacket;

/**
 *
 */
public class Rulename extends ABNFBaseParser<ABNF, ABNF> {

    public Rulename(ABNF rule, ABNFReg reg, ABNFReg base) {
        super(rule, reg, base);
    }

    @Override
    public ABNF parse(FrontPacket pac) {
        FrontPacket p = rule.is(pac);
        if (p == null) {
            return null;
        }
        return reg.ref(AbstractABNF.str(p));
    }

}
