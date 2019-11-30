package net.siisise.abnf.parser5234;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser.ABNFSub;

/**
 * groupのParser。
 * 
 */
public class Group extends ABNFSub<ABNF> {

    /**
     * 
     * @param def 処理対象のABNF構文
     * @param reg 名前空間参照用
     * @param base Parser駆動用
     */
    public Group(ABNF def, ABNFReg reg, ABNFReg base) {
        super(def, reg, base, "alternation");
    }
}
