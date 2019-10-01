package net.siisise.abnf;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.siisise.io.Packet;
import net.siisise.io.PacketA;

/**
 * casesensitiveではない方
 *
 * @see ABNFbin
 * @author okome
 */
public class ABNFtext extends IsABNF {

    String text;
    byte[] utf8;

    public ABNFtext(int ch) {
        text = Character.toString(ch);
        if (ch < 0x7f && ch >= 0x20 && ch != 0x22) {
            name = "\"" + (char) ch + "\"";
        } else {
            name = hex(ch);
        }
        try {
            utf8 = text.getBytes("utf-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ABNFtext.class.getName()).log(Level.SEVERE, null, ex);
            throw new java.lang.UnsupportedOperationException();
        }
    }

    ABNFtext(String text) {
        this("\"" + text + "\"", text);
    }

    public ABNFtext(String name, String val) {
        text = val;
        this.name = name;
        try {
            utf8 = text.getBytes("utf-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ABNFtext.class.getName()).log(Level.SEVERE, null, ex);
            throw new java.lang.UnsupportedOperationException();
        }
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
    public Packet is(Packet pac) {
        if (pac.length() < utf8.length) {
            return null;
        }
        byte[] d = new byte[utf8.length];
        pac.read(d);
        String u;
        try {
            u = new String(d, "utf-8");
            if (u.equalsIgnoreCase(text)) {
                return new PacketA(d);
            }
            pac.backWrite(d);
            return null;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ABNFtext.class.getName()).log(Level.SEVERE, null, ex);
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
