package net.siisise.abnf.parser5234;

import java.util.List;
import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser.ABNFSub;

/**
 *
 * @author okome
 */
public class Option extends ABNFSub<ABNF> {

    public Option(ABNFReg reg) {
        super(ABNF5234.option, reg, Alternation.class);
    }

    public Option(ABNFReg reg, ABNFReg base) {
        super(ABNF5234.option, reg, base, "alternation");
    }

    @Override
    public ABNF parse(List<ABNF> pac) {
        return pac.get(0).c();
    }
    
}
