package net.siisise.abnf;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.siisise.abnf.parser.ABNFParser;
import net.siisise.io.Packet;
import net.siisise.io.PacketA;

/**
 *
 * @author okome
 */
public abstract class AbstractABNF implements ABNF {

    String name;

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

    @Override
    public ABNFor or(ABNF... val) {
        ABNF[] list = new ABNF[val.length + 1];
        list[0] = this;
        System.arraycopy(val, 0, list, 1, val.length);
        return new ABNFor(list);
    }

    /*    @Override
    public Packet is(Packet src) {
        B ret = find(src);
        if ( ret == null ) {
            return null;
        }
        return ret.ret;
    }
     */
    protected boolean isName(String[] names) {
        List<String> nnn = Arrays.asList(names);
        return nnn.contains(name);
    }

    protected boolean isName(String name, String[] names) {
        for (String n : names) {
            if (name.equals(n)) {
                return true;
            }
        }
        return false;
    }

    /**
     * sub要素のない場合の対応
     *
     * @param pac
     * @param names
     * @return
     */
    @Override
    public B find(Packet pac, String... names) {
        Packet r = is(pac);
        if (r == null) {
            return null;
        }
        return sub(new B(r), names);
    }

    @Override
    public <X> C<X> findx(Packet pac, ABNFParser<? extends X>... parsers) {
//        System.out.println("findx:" +getName()+":"+ strd(pac));
        List<String> names = new ArrayList<>();
        for (ABNFParser<? extends X> p : parsers) {
            names.add(p.getBNF().getName());
        }
        B<X> ret = find(pac, names.toArray(new String[parsers.length]));
        if (ret == null) {
            return null;
        }
//        System.out.println("findx2:" + ret);
        C<X> cret = new C<>(ret.ret);
        for (ABNFParser<? extends X> p : parsers) {
            String pname = p.getBNF().getName();
            List<Packet> subps = ret.subs.get(pname);
            if (subps != null) {
                for (Packet subp : subps) {
//                    System.out.println("findx2s:" + p.getClass().getName() + " : " + strd(subp));
                    cret.add(pname, p.parse(subp));
                }
            }
        }
        return cret; // sub(cret,parsers);
    }

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

    void rollback(Packet src, Packet data) {
        byte[] d = data.toByteArray();
        src.backWrite(d);
    }

    void mix(B<Packet> ret, B<Packet> sub) {
        ret.ret.write(sub.ret.toByteArray());
        sub.subs.keySet().forEach((key) -> {
            List<Packet> val = sub.subs.get(key);
            val.forEach((v) -> {
                ret.add(key, v);
            });
        });
    }

    <X> void mix(C<X> ret, C<X> sub) {
        ret.ret.write(sub.ret.toByteArray());
        sub.subs.keySet().forEach((key) -> {
            List<Object> val = (List<Object>) sub.subs.get(key);
            val.forEach((v) -> {
                ret.add(key, (X) v);
            });
        });
    }

    /**
     * 該当すれば sub要素を消す
     *
     * @param src
     * @param names
     * @return
     */
    B sub(B src, String[] names) {
        List<String> nn = Arrays.asList(names);

        if (nn.contains(name)) {
            byte[] b = src.ret.toByteArray();
            src.ret.backWrite(b);
            Packet p = new PacketA(b);
            List<Packet> pl = new ArrayList<>();
            pl.add(p);
            src.subs.clear();
            src.subs.put(name, pl); // 上書き
        }

        return src;
    }

//    B mix(Packet src, String[] names) {
//        return sub(new B(src),names);
//    }
    @Override
    public Packet is(Packet src) {
        B ret = find(src);
        if (ret == null) {
            return null;
        }
        return ret.ret;
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
