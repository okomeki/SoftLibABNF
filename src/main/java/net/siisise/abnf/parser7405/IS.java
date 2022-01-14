package net.siisise.abnf.parser7405;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser.ABNFBuildParser;
import net.siisise.bnf.BNF;

public class IS extends ABNFBuildParser<ABNF, String> {

    public IS(ABNF rule, ABNFReg base) {
        super(rule, base, "quoted-string");
    }

    @Override
    protected ABNF build(BNF.C<String> src) {
        return ABNF.text(src.get("quoted-string").get(0));
    }

}
