package net.siisise.abnf.parser7405;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser.ABNFSelect;

public class CharVal7405 extends ABNFSelect<ABNF> {

    public CharVal7405(ABNF rule, ABNFReg base) {
        super(rule, base, "case-insensitive-string", "case-sensitive-string");
    }
}    
