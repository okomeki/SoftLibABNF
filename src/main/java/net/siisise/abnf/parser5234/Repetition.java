package net.siisise.abnf.parser5234;

import java.util.List;
import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser.ABNFBaseParser;
import net.siisise.io.FrontPacket;
import net.siisise.io.Packet;

/**
 * リピートはorに展開せよ?
 */
public class Repetition extends ABNFBaseParser<ABNF, ABNF> {

    /**
     * abnfの他、ABNF5234のrepeat も参照する
     * @param abnf
     * @param reg
     * @param base 
     */
    public Repetition(ABNF abnf, ABNFReg reg, ABNFReg base) {
        super(abnf, reg, base, "element");
    }

    @Override
    public ABNF parse(FrontPacket pac) {
        inst();
//        System.out.println("rep: " + strd(pac));
        ABNF repeat = ABNF5234.repeat; // base.href("repeat");
        ABNF.C<Object> ret = def.find(pac, pacp(repeat), subs[0]);
        if (ret == null) {
            return null;
        }
        List<Object> rep = ret.get(repeat);
        ABNF ele = (ABNF) ret.get(subs[0].getBNF()).get(0);
//        System.out.println("ee;:" + strd(element));
        //ABNF ele = subs[0].parse(element);
        if (rep != null) {
            return repeat((Packet) rep.get(0), ele);
        }
        return ele;
    }

    ABNF repeat(Packet repeat, ABNF element) {
        String rep = str(repeat);
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
