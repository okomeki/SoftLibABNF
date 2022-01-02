package net.siisise.abnf;

import net.siisise.io.FrontPacket;
import net.siisise.io.Packet;
import net.siisise.io.PacketA;

/**
 * casesensitiveではない方
 *
 * @see ABNFbin
 */
public class ABNFtext extends IsABNF {

    private final String text;
    private final byte[] utf8;

    /**
     *
     * @param ch
     */
    ABNFtext(int ch) {
        char[] chars = Character.toChars(ch);
        text = String.valueOf(chars);
        if (ch < 0x7f && ch >= 0x20 && ch != 0x22) {
            name = "\"" + text + "\"";
        } else {
            name = hex(ch);
        }
        utf8 = text.getBytes(UTF8);
    }

    ABNFtext(String text) {
        this.text = text;
        name = "\"" + text + "\"";
        utf8 = text.getBytes(UTF8);
    }

    /**
     *
     * @param reg
     * @return
     */
    @Override
    public ABNFtext copy(ABNFReg reg) {
        return new ABNFtext(text);
    }

    /**
     * 1文字単位でor分割
     *
     * ABNFor(text) も同じ
     *
     * @param chlist
     * @return
     */
    public static ABNF list(String chlist) {
        return new ABNFor(chlist);
    }

    @Override
    public Packet is(FrontPacket pac) {
        if (pac.length() < 1) {
            return null;
        }
        byte[] d = new byte[utf8.length];
        int size = pac.read(d);
        if ( size < utf8.length ) {
            pac.backWrite(d, 0, size);
            return null;
        }
        String u;
        u = new String(d, UTF8);
        if (u.equalsIgnoreCase(text)) {
            return new PacketA(d);
        }
        pac.dbackWrite(d);
        return null;
    }
}
