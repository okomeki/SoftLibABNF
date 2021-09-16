package net.siisise.bnf;

import net.siisise.abnf.io.FrontIO;
import net.siisise.bnf.parser.BNFParser;
import net.siisise.io.FrontPacket;
import net.siisise.io.Packet;
import net.siisise.io.PacketA;

/**
 *
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
            if(name.equals(ps.getBNF().getName())) {
                return ps;
            }
        }
        return null;
    }

    @Override
    public boolean is(String val) {
        return is(pac(val)) != null;
    }
    
    class BNFPacketParser implements BNFParser {
        BNF rule;
        BNFPacketParser(BNF rule) {
            this.rule = rule;
        }

        @Override
        public BNF getBNF() {
            return rule;
        }

        @Override
        public Object parse(FrontPacket pac) {
            return pac;
        }

        @Override
        public Object parse(String src) {
            return pac(src);
        }
    }
    
    public boolean eq(FrontPacket val) {
        Packet r = is(val);
        if (val.length() == 0) {
            return true;
        }
        if (r != null) {
            val.backWrite(r.toByteArray());
        }
        return false;
    }
    
    public boolean eq(String val) {
        return eq(pac(val));
    }
    
    public static FrontPacket pac(String str) {
        //Packet p = new PacketA();
        //p.write(str.getBytes(UTF8));
        FrontPacket p = new FrontIO(str.getBytes(UTF8));
        return p;
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
        pac.backWrite(data);
        Packet n = new PacketA(data);
        return str(n);
    }

    /**
     * 名前が該当すればそれ以下を削除して詰め直す
     * @param <X>
     * @param cret
     * @param parser 一致するものだけ必要
     * @return
     */
    protected <X> C<X> subBuild(C<X> cret, BNFParser<? extends X> parser) {
        if (parser != null) {
            cret.subs.clear();
            byte[] data = cret.ret.toByteArray();
            cret.ret.backWrite(data);
            cret.add(name, parser.parse(new PacketA(data)));
        }
        return cret;
    }

}
