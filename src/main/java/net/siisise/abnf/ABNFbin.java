package net.siisise.abnf;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.siisise.io.Packet;
import net.siisise.io.PacketA;

/**
 * バイナリ表現。
 */
public class ABNFbin extends IsABNF {

    private byte[] data;

    ABNFbin(int ch) { // " a-z A-Z, 0x80以降 を%表記、それ以外を文字表記
        if (ch >= 0x20 && ((ch != 0x22 && ch < 0x41) || (ch > 0x5a && ch < 0x61) || (ch > 0x7a && ch < 0x7f))) {
            name = "\"" + (char) ch + "\"";
        } else {
            name = hex(ch);
        }

        data = utf8(ch);
    }

    /**
     * ascii ?
     * @param val 
     */
    ABNFbin(String val) {
        StringBuilder sb = new StringBuilder(50);
        sb.append(hex(val.charAt(0)));
        for (int i = 1; i < val.length(); i++) {
            sb.append(".");
            sb.append(hex(val.charAt(i)).substring(2));
        }
        name = sb.toString();
        try {
            data = val.getBytes("utf-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ABNFbin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public ABNFbin copy(ABNFReg reg) {
        try {
            return new ABNFbin(new String(data, "utf-8"));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ABNFbin.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     * 1文字単位でor分割。
     * どれかの文字に該当するor型ABNFをつくる
     *
     * @param val 文字の一括登録
     * @return
     */
    public static ABNF list(String val) {
        Packet src = pac(val);

        List<ABNF> list = new ArrayList<>();
        while (src.length() > 0) {
            list.add(new ABNFbin(utf8(src)));
        }
        if (list.size() == 1) {
            return list.get(0);
        }
        return new ABNFor(list.toArray(new ABNF[list.size()]));
    }

    @Override
    public Packet is(Packet pac) {
        if (pac.length() < data.length) {
            return null;
        }
        byte[] d = new byte[data.length];
        pac.read(d);
        if (Arrays.equals(data, d)) {
            return new PacketA(d);
        }
        pac.backWrite(d);
        return null;
    }

}
