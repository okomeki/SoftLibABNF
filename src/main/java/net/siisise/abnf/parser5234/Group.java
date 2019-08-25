package net.siisise.abnf.parser5234;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser.ABNFSub;

/**
 * groupのParser
 * @author okome
 */
public class Group extends ABNFSub<ABNF> {

    /**
     * 
     * @param reg 名前空間参照用
     */
    public Group(ABNFReg reg) {
        super(ABNF5234.group, reg, Alternation.class);
    }

    public Group(ABNF def, ABNFReg reg, ABNFReg base) {
        super(def, reg, base, "alternation");
    }
}
