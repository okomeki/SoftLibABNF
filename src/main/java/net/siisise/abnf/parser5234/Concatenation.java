package net.siisise.abnf.parser5234;

import java.util.List;
import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.ABNFpl;
import net.siisise.abnf.parser.ABNFList;

/**
 *
 * @author okome
 */
public class Concatenation extends ABNFList<ABNF, ABNF> {

    public Concatenation(ABNFReg reg) {
        super(ABNF5234.concatenation, reg, Repetition.class);
    }

    public Concatenation(ABNFReg reg, ABNFReg base) {
        super(ABNF5234.concatenation, reg, base, "repetition");
    }

    @Override
    public ABNF parse(List<ABNF> pac) {
        if (pac.size() == 1) {
            return pac.get(0);
        }
        return new ABNFpl(pac.toArray(new ABNF[pac.size()]));
    }

}
