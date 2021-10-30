package net.siisise.abnf;

import net.siisise.io.FrontPacket;
import net.siisise.io.Packet;
import net.siisise.io.PacketA;
import net.siisise.lang.CodePoint;

/**
 * 文字
 *
 */
public class ABNFrange extends IsABNF {

    private final int min;
    private final int max;

    public ABNFrange(int min, int max) {
        this.min = min;
        this.max = max;
        name = hex(min) + "-" + hex(max).substring(2);
    }

    @Override
    public ABNFrange copy(ABNFReg reg) {
        return new ABNFrange(min, max);
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
        if (ch >= min && ch <= max) {
            return new PacketA(bin8);
        }
        pac.dbackWrite(bin8);
        return null;
    }
}
