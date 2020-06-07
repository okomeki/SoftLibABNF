package net.siisise.abnf;

import java.util.List;
import net.siisise.abnf.parser.ABNFParser;
import net.siisise.io.FrontPacket;
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

    @Override
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
    public ABNF plu(ABNF... val) {
        if (val.length == 0) {
            return this;
        }
        ABNF[] list = new ABNF[val.length + 1];
        list[0] = this;
        System.arraycopy(val, 0, list, 1, val.length);
        return new ABNFplu(list);
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
        p.write(str.getBytes(UTF8));
        return p;
    }

    public static String str(FrontPacket pac) {
        return new String(pac.toByteArray(), UTF8);
    }

    /**
     * 文字列に起こす。
     * データは元に戻す。
     * @param pac
     * @return 
     */
    public static String strd(FrontPacket pac) {
        byte[] data = pac.toByteArray();
        pac.backWrite(data);
        Packet n = new PacketA(data);
        return str(n);
    }
}
