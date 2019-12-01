package net.siisise.abnf.parser5234;

import java.util.List;
import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser.ABNFToList;

/**
 *
 */
public class Rulelist extends ABNFToList<ABNF, ABNF> {

    public Rulelist(ABNF abnf, ABNFReg reg, ABNFReg base) {
        super(abnf, reg, base, "rule");
    }

    @Override
    protected List<ABNF> parse(List<ABNF> val) {
        return val;
    }

}
