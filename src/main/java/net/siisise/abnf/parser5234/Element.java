package net.siisise.abnf.parser5234;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser.ABNFSelect;

/**
 * ABNF element の Parser.
 * 選択するだけ
 */
public class Element extends ABNFSelect<ABNF> {

    /**
     * より抽象化したもの
     * @param def
     * @param reg
     * @param base 
     */
    public Element(ABNF def, ABNFReg reg, ABNFReg base) {
        super(def, reg, base, "rulename", "group", "option", "char-val", "num-val", "prose-val");
    }
}
