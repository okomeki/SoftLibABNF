package net.siisise.abnf.parser5234;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser.ABNFSelect;

/**
 * ABNF element の Parser.
 * 選択するだけ
 * @author okome
 */
public class Element extends ABNFSelect<ABNF> {

    public Element(ABNFReg reg) {
        super(ABNF5234.element, reg, Rulename.class, Group.class, Option.class, CharVal.class, NumVal.class, ProseVal.class);
    }
 
    public Element(ABNF def, ABNFReg reg, ABNFReg base) {
        super(def, reg, base, "rulename", "group", "option", "char-val", "num-val", "prose-val");
    }
}
