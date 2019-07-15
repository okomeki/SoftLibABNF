package net.siisise.abnf.rfc;

import java.util.List;
import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser.ABNFBaseParser;
import net.siisise.abnf.parser5234.Element;
import net.siisise.io.Packet;

/**
 *
 * @author okome
 */
public class HTTP7230List extends ABNFBaseParser<ABNF, ABNF> {
    
    public HTTP7230List(ABNFReg reg) {
        super(HTTP7230.repeatList,reg,Element.class);
    }

    @Override
    public ABNF parse(Packet pac) {
        inst();
//        System.out.println("rep: " + strd(pac));
        ABNF.C<Object> ret = def.findx(pac, x(HTTP7230.repList), subs[0]);
        if (ret == null) {
            return null;
        }
        List<Object> rep = ret.get(HTTP7230.repList);
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
        if (rep.contains("#")) {
            int off = rep.indexOf("#");
            String l = rep.substring(0, off);
            String r = rep.substring(off + 1);
            if (l.isEmpty()) {
                l = "1";
            }
            if (r.isEmpty()) {
                r = "0";
            }
            int ln = Integer.parseInt(l);
            int rn = Integer.parseInt(r);
            ABNF ex = element.pl(HTTP7230.OWS.pl(ABNF.bin(","),HTTP7230.OWS,element).x(ln-1,rn-1));
            return ex;
        } else {
            int r = Integer.parseInt(rep);
            ABNF ex = element;
            return ex.x(r, r);
        }
    }
}
