package net.siisise.abnf;

import net.siisise.io.Packet;
import net.siisise.io.PacketA;

/**
 * 文字
 *
 * @author okome
 */
public class ABNFrange extends IsABNF {

    private final int min;
    private final int max;

    ABNFrange(int min, int max) {
        this.min = min;
        this.max = max;
        name = hex(min) + "-" + hex(max).substring(2);
    }

    @Override
    public ABNFrange copy(ABNFReg reg) {
        return new ABNFrange(min, max);
    }

    @Override
    public Packet is(Packet pac) {
        if (pac.length() == 0) {
            return null;
        }
        int ch = utf8(pac);
        if (ch < 0) {
            throw new UnsupportedOperationException();
        }
        byte[] bin8 = utf8(ch);
        if (ch >= min && ch <= max) {
            return new PacketA(bin8);
        }
        pac.backWrite(bin8);
        return null;
    }
}
