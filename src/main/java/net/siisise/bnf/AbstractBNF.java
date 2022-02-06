package net.siisise.bnf;

import java.io.StringReader;
import net.siisise.bnf.parser.BNFParser;
import net.siisise.io.FrontPacket;
import net.siisise.io.Packet;
import net.siisise.io.PacketA;
import net.siisise.io.StreamFrontPacket;

/**
 * 基本実装
 * @param <B>
 */
public abstract class AbstractBNF<B extends BNF> implements BNF<B> {

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

    @Override
    public B name(String name) {
        return (B) new BNFor(name, this); // ?
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

    @Override
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

    @Override
    public boolean eq(String val) {
        return eq(pac(val));
    }

    /**
     * Concatenation の単純版
     * *(a / b ) a の様なものが解析できないが速そう
     * @param val plus  する ABNF列
     * @return 簡易に比較するABNF
     */
    @Override
    public B pl(BNF... val) {
        if (val.length == 0) {
            return (B) this;
        }
        BNF[] list = new BNF[val.length + 1];
        list[0] = this;
        System.arraycopy(val, 0, list, 1, val.length);
        return (B) new BNFpl(list);
    }

    /**
     * やや厳密に対応する足し算版
     * *( a / b ) a にも対応
     * @param val plus する ABNF列
     * @return 厳密に比較できるABNF
     */
    @Override
    public B plm(BNF... val) {
        if (val.length == 0) {
            return (B) this;
        }
        BNF[] list = new BNF[val.length + 1];
        list[0] = this;
        System.arraycopy(val, 0, list, 1, val.length);
        return (B) new BNFplm(list);
    }

    /**
     * Unicode単位で比較する若干速いのかもしれない版 plm
     * @param val plus する ABNF列
     * @return unicodeで比較されるABNF処理
     */
    @Override
    public B plu(BNF... val) {
        if (val.length == 0) {
            return (B) this;
        }
        BNF[] list = new BNF[val.length + 1];
        list[0] = this;
        System.arraycopy(val, 0, list, 1, val.length);
        return (B) new BNFplu(list);
    }

    @Override
    public B or(BNF... val) {
        BNF[] list = new BNF[val.length + 1];
        list[0] = this;
        System.arraycopy(val, 0, list, 1, val.length);
        return (B) new BNFor(list);
    }


    @Override
    public B x(int min, int max) {
        return (B) new BNFx(min, max, this);
    }

    @Override
    public B x() {
        return x(0, -1);
    }

    @Override
    public B ix() {
        return x(1, -1);
    }

    @Override
    public B c() {
        return x(0, 1);
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
     * 文字列に起こす。 データは元に戻すので減らない。
     *
     * @param pac 元パケット
     * @return 文字に変えたもの
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
     * @param <X> 戻りの型
     * @param <N> name space の型
     * @param cret 戻り
     * @param ns name space
     * @param parser 一致するものだけ必要
     * @return 戻りからさらにparserを通したものを追加したもの
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

    /**
     * 結果2を1に混ぜる
     * @param <X> 戻り型
     * @param ret 結果1
     * @param sub 結果2
     */
    protected static <X> void mix(C<X> ret, C<X> sub) {
        ret.ret.write(sub.ret.toByteArray());
        sub.subs.forEach((key,val) -> {
            val.forEach((v) -> {
                ret.add(key, v);
            });
        });
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
}
