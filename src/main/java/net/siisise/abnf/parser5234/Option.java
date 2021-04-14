package net.siisise.abnf.parser5234;

import java.util.List;
import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser.ABNFSub;

/**
 * OptionのParser.
 * 繰り返し0*1に変換する
 */
public class Option extends ABNFSub<ABNF> {

    public Option(ABNF rule, ABNFReg reg, ABNFReg base) {
        super(rule, reg, base, "alternation");
    }

    @Override
    protected ABNF parse(List<ABNF> pac) {
        return pac.get(0).c();
    }
    
}
