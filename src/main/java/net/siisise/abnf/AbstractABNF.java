package net.siisise.abnf;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.siisise.abnf.parser.ABNFParser;
import net.siisise.io.Packet;
import net.siisise.io.PacketA;

/**
 * 簡単なプレ実装
 */
public abstract class AbstractABNF implements ABNF {

    /**
     * 名前、またはABNFに近いもの (まだ抜けもある)
     */
    String name;

    @Override
    public String getName() {
        if (name == null) {
            return "???";
        }
        return name;
    }

    @Override
    public ABNF name(String name) {
        return new ABNFor(name, this); // ?
    }
    
    protected boolean isName(ABNFParser<?>[] parsers) {
        for (ABNFParser p : parsers ) {
            if (name.equals(p.getBNF().getName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean is(String val) {
        return is(pac(val)) != null;
    }
    
    @Override
    public boolean eq(Packet val) {
        Packet r = is(val);
        if (val.length() == 0) {
            return true;
        }
        if (r != null) { // 部分一致
            val.backWrite(r.toByteArray());
        }
        return false;
    }

    @Override
    public boolean eq(String val) {
        return eq(pac(val));
    }

    @Override
    public ABNF pl(ABNF... val) {
        if (val.length == 0) {
            return this;
        }
        ABNF[] list = new ABNF[val.length + 1];
        list[0] = this;
        System.arraycopy(val, 0, list, 1, val.length);
        return new ABNFpl(list);
    }
    
    public ABNF plm(ABNF... val) {
        if (val.length == 0) {
            return this;
        }
        ABNF[] list = new ABNF[val.length + 1];
        list[0] = this;
        System.arraycopy(val, 0, list, 1, val.length);
        return new ABNFplm(list);
    }

    @Override
    public ABNFor or(ABNF... val) {
        ABNF[] list = new ABNF[val.length + 1];
        list[0] = this;
        System.arraycopy(val, 0, list, 1, val.length);
        return new ABNFor(list);
    }

    /**
     * 
     * @param <X>
     * @param ret
     * @param sub 
     */
    static <X> void mix(C<X> ret, C<X> sub) {
        ret.ret.write(sub.ret.toByteArray());
        sub.subs.keySet().forEach((key) -> {
            List<Object> val = (List<Object>) sub.subs.get(key);
            val.forEach((v) -> {
                ret.add(key, (X) v);
            });
        });
    }

    /**
     * 
     * @param <X>
     * @param cret
     * @param parsers
     * @return 
     */
    <X> C<X> sub(C<X> cret, ABNFParser<? extends X>... parsers) {
        for (ABNFParser<? extends X> p : parsers) {
            String pname = p.getBNF().getName();
            if (pname.equals(name)) {
                cret.subs.clear();
                byte[] data = cret.ret.toByteArray();
                cret.ret.backWrite(data);
                cret.add(name, p.parse(new PacketA(data)));
            }
        }
        return cret;
    }

    @Override
    public ABNF x(int min, int max) {
        return new ABNFx(min, max, this);
    }

    @Override
    public ABNF x() {
        return x(0, -1);
    }

    @Override
    public ABNF ix() {
        return x(1, -1);
    }

    @Override
    public ABNF c() {
        return x(0, 1);
    }

    protected String hex(int ch) {
        if (ch < 0x100) {
            return "%x" + Integer.toHexString(0x100 + ch).substring(1);
        } else if (ch < 0x10000) {
            return "%x" + Integer.toHexString(0x10000 + ch).substring(1);
        } else {
            return "%x" + Integer.toHexString(0x1000000 + ch).substring(1);
        }
    }

    @Override
    public String toString() {
        return name;
    }

    public static Packet pac(String str) {
        Packet p = new PacketA();
        try {
            p.write(str.getBytes("utf-8"));
        } catch (UnsupportedEncodingException ex) { // ない
            Logger.getLogger(AbstractABNF.class.getName()).log(Level.SEVERE, null, ex);
        }
        return p;
    }

    public static String str(Packet pac) {
        try {
            return new String(pac.toByteArray(), "utf-8");
        } catch (UnsupportedEncodingException ex) {
            throw new java.lang.UnsupportedOperationException();
        }
    }

    /**
     * 文字列に起こす。
     * データは元に戻す。
     * @param pac
     * @return 
     */
    public static String strd(Packet pac) {
        byte[] data = pac.toByteArray();
        pac.backWrite(data);
        Packet n = new PacketA(data);
        return str(n);
    }

    /**
     * UTF-8をUCSに変換
     *
     * @param pac
     * @return UCS-4または-1
     */
    public static int utf8(Packet pac) {
        int rd = pac.read();
        int len;
        int min;
        if (rd < 0) {
            return -1;
        }
        if (rd < 0x80) {        // 0xxx xxxx 1バイト 7bit 00 - 7f
            return rd;
        } else if (rd < 0xc0) { // 10xx xxxx 80 - 7ff 2バイト目以降
            return -1;
        } else if (rd < 0xe0) { // 110x xxxx 2バイト 11bit
            rd &= 0x1f;
            len = 1;
            min = 0x80;
        } else if (rd < 0xf0) { // 1110 xxxx 3バイト 16bit
            rd &= 0xf;
            len = 2;
            min = 0x800;
        } else {                  // 1111 0xxx 4バイト 21bit
            rd &= 0x7;
            len = 3;
            min = 0x10000;
        }

        for (int i = 0; i < len; i++) {
            rd <<= 6;
            int c = pac.read();
            if ((c & 0xc0) != 0x80) {
                return -1;
            }
            rd |= (c & 0x3f);
        }
        if (rd < min || rd > 0x10ffff) {
            return -1;
        }
        return rd;
    }

    public static byte[] utf8(int ch) {
        if (ch < 0x80) {
            return new byte[]{(byte) ch};
        } else if (ch < 0x800) {
            return new byte[]{
                (byte) (0xc0 | (ch >> 6)),
                (byte) (0x80 | (ch & 0x3f))
            };
        } else if (ch < 0x10000) {
            return new byte[]{
                (byte) (0xe0 | (ch >> 12)),
                (byte) (0x80 | ((ch >> 6) & 0x3f)),
                (byte) (0x80 | (ch & 0x3f))
            };
        } else if (ch < 0x110000) {
            return new byte[]{
                (byte) (0xf0 | (ch >> 18)),
                (byte) (0x80 | ((ch >> 12) & 0x3f)),
                (byte) (0x80 | ((ch >> 6) & 0x3f)),
                (byte) (0x80 | (ch & 0x3f))
            };
        }
        throw new java.lang.UnsupportedOperationException();
    }
}
