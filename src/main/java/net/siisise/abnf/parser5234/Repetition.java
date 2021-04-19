package net.siisise.abnf.parser5234;

import java.util.List;
import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser.ABNFBaseParser;
import net.siisise.io.FrontPacket;

/**
 * リピートはorに展開せよ?
 */
public class Repetition extends ABNFBaseParser<ABNF, ABNF> {

    /**
     * abnfの他、ABNF5234のrepeat も参照する
     * @param rule
     * @param reg
     * @param base 
     */
    public Repetition(ABNF rule, ABNFReg reg, ABNFReg base) {
        super(rule, reg, base, "element");
    }

    @Override
    public ABNF parse(FrontPacket pac) {
        inst();

        ABNF repeat = ABNF5234.repeat; // base.href("repeat");
        ABNF.C<Object> ret = rule.find(pac, strp(repeat), subs[0]);
        if (ret == null) {
            return null;
        }
        List<Object> rep = ret.get(repeat);
        ABNF ele = (ABNF) ret.get(subs[0].getBNF()).get(0);

        if (rep != null) {
            return repeat((String) rep.get(0), ele);
        }
        return ele;
    }

    ABNF repeat(String rep, ABNF element) {
        if (rep.contains("*")) {
            int off = rep.indexOf("*");
            String l = rep.substring(0, off);
            String r = rep.substring(off + 1);
            if (l.isEmpty()) {
                l = "0";
            }
            if (r.isEmpty()) {
                r = "-1";
            }
            return element.x(Integer.parseInt(l), Integer.parseInt(r));
        } else {
            int r = Integer.parseInt(rep);
            return element.x(r, r);
        }
    }

}
