package net.siisise.abnf.parser5234;

import java.util.List;
import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser.ABNFToList;

/**
 *
 * @author okome
 */
public class Rulelist extends ABNFToList<ABNF, ABNF> {
/*
    public Rulelist(ABNFReg reg) {
        super(ABNF5234.rulelist, reg, Rule.class);
    }
*/
    public Rulelist(ABNFReg reg, ABNFReg base) {
        super(ABNF5234.rulelist, reg, base, "rule");
    }

    @Override
    public List<ABNF> parse(List<ABNF> val) {
        return val;
    }

}
