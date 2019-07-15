package net.siisise.abnf.parser5234;

import java.util.List;
import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.ABNFor;
import net.siisise.abnf.parser.ABNFList;

/**
 *
 * @author okome
 */
public class Alternation extends ABNFList<ABNF, ABNF> {

    public Alternation(ABNFReg reg) {
        super(ABNF5234.alternation, reg, Concatenation.class);
    }

    @Override
    public ABNF parse(List<ABNF> list) {
        if (list.size() == 1) {
            return list.get(0);
        }
        return new ABNFor(list.toArray(new ABNF[list.size()]));
    }

}
