package net.siisise.abnf.parser7405;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser.ABNFBuildParser;
import net.siisise.bnf.BNF;

public class SS extends ABNFBuildParser<ABNF, String> {
    
    public SS(ABNF rule, ABNFReg reg, ABNFReg base) {
        super(rule, reg, base, "quoted-string");
    }

    @Override
    protected ABNF build(BNF.C<String> src) {
        return ABNF.bin(src.get("quoted-string").get(0));
    }
    
}
