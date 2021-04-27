package net.siisise.abnf.parser7405;

import java.util.List;
import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser.ABNFBuildParser;
import net.siisise.bnf.BNF;

public class CharVal7405 extends ABNFBuildParser<ABNF, Object> {

    public CharVal7405(ABNF rule, ABNFReg reg, ABNFReg base) {
        super(rule, reg, base, "case-insensitive-string", "case-sensitive-string", "quoted-string");
    }

    @Override
    public ABNF build(ABNF.C<Object> ret) {
        List<Object> insen = ret.get("case-insensitive-string");
        List<Object> sen = ret.get("case-sensitive-string");
        ABNF bnf;
        if (sen != null) {
            bnf = base.href("case-sensitive-string");
        } else {
            bnf = base.href("case-insensitive-string");
        }
        BNF.C<?> qs = bnf.find(ret.ret, subs[2]);
        String val = (String) qs.get("quoted-string").get(0);
        if (sen != null) {
            return ABNF.bin(val);
        } else if (insen != null) {
            return ABNF.text(val);
        }
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
