package net.siisise.abnf.parser5234;

import java.util.List;
import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser.ABNFList;

/**
 *
 */
public class Rulelist extends ABNFList<List<ABNF>, ABNF> {

    public Rulelist(ABNF rule, ABNFReg reg, ABNFReg base) {
        super(rule, reg, base, "rule");
    }

    @Override
    protected List<ABNF> build(List<ABNF> val) {
        return val;
    }

}
