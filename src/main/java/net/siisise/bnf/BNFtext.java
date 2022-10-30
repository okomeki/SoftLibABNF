package net.siisise.bnf;

import net.siisise.io.Packet;
import net.siisise.pac.ReadableBlock;

/**
 *
 */
public class BNFtext extends IsBNF {

    private final String text;
    private final byte[] utf8;

    /**
     *
     * @param ch
     */
    BNFtext(int ch) {
        char[] chars = Character.toChars(ch);
        text = String.valueOf(chars);
        if (ch < 0x7f && ch >= 0x20 && ch != 0x22) {
            name = "\"" + text + "\"";
        } else {
            name = hex(ch);
        }
        utf8 = text.getBytes(UTF8);
    }

    BNFtext(String text) {
        this.text = text;
        name = "\"" + text + "\"";
        utf8 = text.getBytes(UTF8);
    }

    @Override
    public Packet is(ReadableBlock src) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public BNF copy(BNFReg reg) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
