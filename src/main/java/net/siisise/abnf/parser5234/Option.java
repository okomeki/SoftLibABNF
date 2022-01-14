package net.siisise.abnf.parser5234;

import java.util.List;
import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser.ABNFList;

/**
 * OptionのParser.
 * 繰り返し0*1に変換する
 */
public class Option extends ABNFList<ABNF,ABNF> {

    public Option(ABNF rule, ABNFReg base) {
        super(rule, base, "alternation");
    }

    @Override
    protected ABNF build(List<ABNF> pac) {
        return pac.get(0).c();
    }

}
