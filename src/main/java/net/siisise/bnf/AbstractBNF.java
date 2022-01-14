package net.siisise.bnf;

import java.io.StringReader;
import net.siisise.bnf.parser.BNFParser;
import net.siisise.io.FrontPacket;
import net.siisise.io.Packet;
import net.siisise.io.PacketA;
import net.siisise.io.StreamFrontPacket;

/**
 * 基本実装
 */
public abstract class AbstractBNF implements BNF {

    /**
     * 名前、またはABNFに近いもの (まだ抜けもある)
     */
    protected String name;

    @Override
    public String getName() {
        if (name == null) {
            return "???";
        }
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    public BNF name(String name) {
        return new BNFor(name, this); // ?
    }

    protected <X> BNFParser<? extends X> matchParser(BNFParser<? extends X>[] parsers) {
        for (BNFParser ps : parsers) {
            if (name.equals(ps.getBNF().getName())) {
                return ps;
            }
        }
        return null;
    }

    @Override
    public boolean is(String val) {
        return is(pac(val)) != null;
    }

    @Override
    public <N> boolean is(String val, N ns) {
        return is(pac(val), ns) != null;
    }

    public boolean eq(FrontPacket val) {
        Packet r = is(val);
        if (val.length() == 0) {
            return true;
        }
        if (r != null) {
            val.dbackWrite(r.toByteArray());
        }
        return false;
    }

    public boolean eq(String val) {
        return eq(pac(val));
    }

    public static FrontPacket pac(String str) {
        PacketA pac = new PacketA();
        pac.dwrite(str.getBytes(UTF8));
        return pac;
    }
    
    public static FrontPacket fpac(String str) {
        return new StreamFrontPacket(new StringReader(str));
    }

    public static String str(FrontPacket pac) {
        return new String(pac.toByteArray(), UTF8);
    }

    /**
     * 文字列に起こす。 データは元に戻す。
     *
     * @param pac
     * @return
     */
    public static String strd(FrontPacket pac) {
        byte[] data = pac.toByteArray();
        Packet n = new PacketA(data);
        pac.dbackWrite(data);
        return str(n);
    }

    /**
     * 名前が該当すればそれ以下を削除して詰め直す
     *
     * @param <X>
     * @param cret
     * @param parser 一致するものだけ必要
     * @return
     */
    protected <X,N> C<X> subBuild(C<X> cret, N ns, BNFParser<? extends X> parser) {
        if (parser != null) {
            cret.subs.clear();
            byte[] data = cret.ret.toByteArray();
            cret.ret.backWrite(data);
            cret.add(name, parser.parse(new PacketA(data), ns));
        }
        return cret;
    }

}
