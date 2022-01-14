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
     * @param rule
     * @param base 
     */
    public Element(ABNF rule, ABNFReg base) {
        super(rule, base, "rulename", "group", "option", "char-val", "num-val", "prose-val");
    }
}
