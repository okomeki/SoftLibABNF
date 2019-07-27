package net.siisise.abnf.parser5234;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser.ABNFSub;

/**
 *
 * @author okome
 */
public class Elements extends ABNFSub<ABNF> {

    public Elements(ABNFReg reg) {
        super(ABNF5234.elements, reg, Alternation.class);
    }

    public Elements(ABNF abnf, ABNFReg reg, ABNFReg base) {
        super(abnf, reg, base, "alternation");
    }
}
