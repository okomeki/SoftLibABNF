package net.siisise.abnf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.siisise.abnf.parser.ABNFParser;
import net.siisise.bnf.BNF;
import net.siisise.io.Packet;
import net.siisise.io.PacketA;

/**
 * RFC 5234.
 * 元はUS-ASCII 7bitコードを想定しているが、UTF-8として利用されている場合もあるので対応する
 * RegExpのPattern相当?
 * ABNFのparserは含まない。
 * @author okome
 */
public interface ABNF extends BNF {

    String getName();
    ABNF name(String name);

    static ABNF text(int ch) {
        return new ABNFtext(ch);
    }

    static ABNF text(String text) {
        return new ABNFtext(text);
    }

    static ABNF list(String chlist) {
        return new ABNFor(chlist);
    }

    static ABNFbin bin(int ch) {
        return new ABNFbin(ch);
    }

    static ABNFbin bin(String str) {
        return new ABNFbin(str);
    }

    static ABNFrange range(int min, int max) {
        return new ABNFrange(min, max);
    }

    /**
     * 先頭一致でパースする
     * findとどちらかの実装が必要、片方は省略可能
     * @param pac
     * @return 一致した範囲
     */
    Packet is(Packet pac);

    /**
     * 先頭一致でパースする
     * 
     * @param val
     * @return 一致ありなし
     */
    boolean is(String val);
    
    <X> C<X> find(Packet pac, ABNFParser<? extends X>... parsers);

    /**
     * 完全一致の判定
     * valの最後まで一致するかどうか判定する
     * @param pac
     * @return
     */
    boolean eq(Packet pac);
    boolean eq(String val);

    ABNF pl(ABNF... val);
    ABNFor or(ABNF... val);
    /**
     * a*bXXX.
     * 
     * @param a 指定しない場合は 0
     * @param b 指定しない場合は -1
     * @return 
     */
    ABNF x(int a, int b);
    /** *XXX */
    ABNF x();
    /** 1*XXX */
    ABNF ix();
    /** [XXX] */
    ABNF c();

    /**
     * 複製可能な構造を推奨(ループがあると複製は難しい)
     * @param reg
     * @return 
     */
    ABNF copy(ABNFReg reg);

    /**
     * 検索用構造
     * あと1回書き直したい
     * @param <X> 
     */
    static class C<X> {

        public Packet ret;
        Map<String, List<X>> subs = new HashMap<>();

        C() {
            ret = new PacketA();
        }

        C(Packet pac) {
            ret = pac;
        }

        void add(String name, X r) {
            List l;
            l = subs.get(name);
            if (l == null) {
                l = new ArrayList();
                subs.put(name, l);
            }
            l.add(r);
        }

        public Set<String> keySet() {
            return subs.keySet();
        }

        public List<X> get(String name) {
            return subs.get(name);
        }

        public List<X> get(ABNF bnf) {
            return get(bnf.getName());
        }
    }
}
