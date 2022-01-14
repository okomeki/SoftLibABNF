package net.siisise.abnf.parser5234;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser.ABNFSelect;

/**
 * elementsとgroup のParser.
 * alternationを取り出すだけ
 * 定義のみなので消せる?
 */
public class SubAlternation extends ABNFSelect<ABNF> {
    
    /**
     * 
     * @param rule groupのABNF構文
     * @param base Parser駆動用
     */
    public SubAlternation(ABNF rule, ABNFReg base) {
        super(rule, base, "alternation");
    }
}
