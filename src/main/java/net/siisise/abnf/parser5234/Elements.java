package net.siisise.abnf.parser5234;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser.ABNFSub;

/**
 * elements のParser.
 * alternationを取り出すだけ
 * 定義のみなので消せる?
 */
public class Elements extends ABNFSub<ABNF> {
    
    public Elements(ABNF rule, ABNFReg reg, ABNFReg base) {
        super(rule, reg, base, "alternation");
    }
}
