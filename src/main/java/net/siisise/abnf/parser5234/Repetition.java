package net.siisise.abnf.parser5234;

import java.util.List;
import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser.ABNFBuildParser;
import net.siisise.io.FrontPacket;

/**
 * リピートはorに展開せよ?
 */
public class Repetition extends ABNFBuildParser<ABNF, Object> {

    /**
     * abnfの他、ABNF5234のrepeat も参照する
     *
     * @param rule
     * @param reg
     * @param base
     */
    public Repetition(ABNF rule, ABNFReg reg, ABNFReg base) {
        super(rule, reg, base, "repeat", "element");
    }

    @Override
    protected ABNF build(ABNF.C<Object> ret) {
        List<Object> rep = ret.get("repeat");
        ABNF ele = (ABNF) ret.get("element").get(0);

        if (rep != null) {
            return repeat(str((FrontPacket) rep.get(0)), ele);
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
