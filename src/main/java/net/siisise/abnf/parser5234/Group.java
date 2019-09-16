package net.siisise.abnf.parser5234;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser.ABNFSub;

/**
 * groupのParser
 * @author okome
 */
public class Group extends ABNFSub<ABNF> {

    public Group(ABNF def, ABNFReg reg, ABNFReg base) {
        super(def, reg, base, "alternation");
    }
}
