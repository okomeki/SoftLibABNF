package net.siisise.abnf.parser5234;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser.ABNFSelect;

/**
 *
 * @author okome
 */
public class Element extends ABNFSelect<ABNF> {

    public Element(ABNFReg reg) {
        super(ABNF5234.element, reg, Rulename.class, Group.class, Option.class, CharVal.class, NumVal.class, ProseVal.class);
    }
 
    public Element(ABNFReg reg, ABNFReg base) {
        super(ABNF5234.element, reg, base, "rulename", "group", "option", "char-val", "num-val", "prose-val");
    }
}
