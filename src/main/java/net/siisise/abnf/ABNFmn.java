package net.siisise.abnf;

import net.siisise.io.FrontPacket;
import net.siisise.io.Packet;

/**
 * ABNFにはないがマイナス演算
 */
public class ABNFmn extends IsABNF {

    private final ABNF a;
    private final ABNF b;

    ABNFmn(ABNF a, ABNF b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public ABNFmn copy(ABNFReg reg) {
        return new ABNFmn(a.copy(reg), b.copy(reg));
    }

    @Override
    public <N> Packet is(FrontPacket pac, N ns) {
        Packet p1 = a.is(pac, ns);
        if (p1 == null) {
            return null;
        }
        Packet p2 = b.is(p1, ns);
        if (p2 != null) {
            pac.dbackWrite(p1.toByteArray());
            pac.dbackWrite(p2.toByteArray());
            return null;
        }
        return p1;
    }

    @Override
    public Packet is(FrontPacket pac) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
