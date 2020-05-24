package net.siisise.abnf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.siisise.io.FrontPacket;
import net.siisise.io.Packet;
import net.siisise.io.PacketA;
import net.siisise.lang.CodePoint;

/**
 * バイナリ表現。
 * 一文字
 */
public class ABNFbin extends IsABNF {

    private byte[] data;

    ABNFbin(int ch) { // " a-z A-Z, 0x80以降 を%表記、それ以外を文字表記
        if (ch >= 0x20 && ((ch != 0x22 && ch < 0x41) || (ch > 0x5a && ch < 0x61) || (ch > 0x7a && ch < 0x7f))) {
            name = "\"" + (char) ch + "\"";
        } else {
            name = hex(ch);
        }

        data = CodePoint.utf8(ch);
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
        data = val.getBytes(UTF8);
    }

    @Override
    public ABNFbin copy(ABNFReg reg) {
        return new ABNFbin(new String(data, UTF8));
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
            list.add(new ABNFbin(CodePoint.utf8(src)));
        }
        if (list.size() == 1) {
            return list.get(0);
        }
        return new ABNFor(list.toArray(new ABNF[list.size()]));
    }

    @Override
    public Packet is(FrontPacket pac) {
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
