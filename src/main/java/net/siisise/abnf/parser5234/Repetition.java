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
     * @param base
     */
    public Repetition(ABNF rule, ABNFReg base) {
        super(rule, base, "repeat", "element");
    }

    @Override
    protected ABNF build(ABNF.C<Object> ret) {
        List<Object> rep = ret.get("repeat");
        ABNF ele = (ABNF) ret.get("element").get(0);

        if (rep != null) {
            return repeat((String)rep.get(0), ele);
        }
        return ele;
    }

    private ABNF repeat(String rep, ABNF element) {
        if (rep.contains("*")) {
            int off = rep.indexOf("*");
            String l = rep.substring(0, off);
            String r = rep.substring(off + 1);
            int min = l.isEmpty() ? 0 : Integer.parseInt(l);
            int max = r.isEmpty() ? -1 : Integer.parseInt(r);
            return element.x(min, max);
        } else {
            int r = Integer.parseInt(rep);
            return element.x(r, r);
        }
    }

}
