package net.siisise.abnf.parser5234;

import java.util.List;
import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.ABNFor;
import net.siisise.abnf.parser.ABNFList;

/**
 * or 相当.
 * 先頭一致なのでアナログな解釈と異なる場合もあるかもしれない
 */
public class Alternation extends ABNFList<ABNF, ABNF> {

    public Alternation(ABNF def, ABNFReg reg, ABNFReg base) {
        super(def, reg, base, "concatenation");
    }

    @Override
    protected ABNF parse(List<ABNF> list) {
        if (list.size() == 1) {
            return list.get(0);
        }
        return new ABNFor(list.toArray(new ABNF[list.size()]));
    }

}
