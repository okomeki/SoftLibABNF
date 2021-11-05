package net.siisise.abnf;

import java.util.ArrayList;
import java.util.List;
import net.siisise.io.FrontPacket;
import net.siisise.io.Packet;
import net.siisise.io.PacketA;
import net.siisise.lang.CodePoint;

/**
 * 文字が含まれるかどうかのMapだかListだか
 * 大文字小文字は別物のバイナリ表記寄り
 */
public class ABNFmap extends IsABNF {

    private final List<Integer> map = new ArrayList<>();

    public ABNFmap() {
        name = "略";
    }

    public ABNFmap(String val) {
        FrontPacket pac = pac(val);
        while (pac.size() > 0) {
            int ch = CodePoint.utf8(pac);
            if (!map.contains(ch)) {
                map.add(ch);
            }
        }
    }

    @Override
    public Packet is(FrontPacket pac) {
        if (pac.length() == 0) {
            return null;
        }
        int ch = CodePoint.utf8(pac);
        if (ch < 0) {
            return null;
        }
        byte[] bin8 = CodePoint.utf8(ch);
        if (map.contains(ch)) {
            return new PacketA(bin8);
        }
        pac.backWrite(bin8);
        return null;
    }

    @Override
    public ABNF copy(ABNFReg reg) {
        ABNFmap nm = new ABNFmap();
        nm.map.addAll(map);
        return nm;
    }

    @Override
    public ABNF or(ABNF... abnf) {
        List<Integer> tmap = new ArrayList<>();
        List<ABNF> xabnf = new ArrayList<>();
        boolean n = true;
        for (ABNF a : abnf) {
            if (n && a instanceof ABNFbin) {
                int ach = ((ABNFbin) a).ch();
                if (ach >= 0 && !map.contains(ach)) {
                    tmap.add(((ABNFbin) a).ch());
                }
            } else {
                n = false;
                xabnf.add(a);
            }
        }
        if (n) {
            ABNFmap nm = new ABNFmap();
            nm.map.addAll(map);
            nm.map.addAll(tmap);
            if (xabnf.isEmpty()) {
                return nm;
            }
            xabnf.add(0, nm);
            return new ABNFor(xabnf.toArray(new ABNF[0]));
        }
        return super.or(abnf);
    }

}
