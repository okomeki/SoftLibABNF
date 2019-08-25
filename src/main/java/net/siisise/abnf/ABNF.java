package net.siisise.abnf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.siisise.abnf.parser.ABNFParser;
import net.siisise.bnf.BNF;
import net.siisise.io.Packet;

/**
 * RFC 5234. 元はUS-ASCII 7bitコードを想定しているが、UTF-8として利用されている場合もあるので対応する
 * RegExpのPattern相当?
 *
 * @author okome
 */
public interface ABNF extends BNF {

    public static ABNF text(int ch) {
        return new ABNFtext(ch);
    }

    public static ABNF text(String text) {
        return new ABNFtext(text);
    }

    public static ABNF list(String chlist) {
        return new ABNFor(chlist);
    }

    public static ABNFbin bin(int ch) {
        return new ABNFbin(ch);
    }

    public static ABNFbin bin(String str) {
        return new ABNFbin(str);
    }

    public static ABNFrange range(int min, int max) {
        return new ABNFrange(min, max);
    }

    public ABNF copy(ABNFReg reg);

    static class B<X> {

        public Packet ret;
        Map<String, List<Packet>> subs = new HashMap<>();
        Map<String, List<X>> subx = new HashMap<>();

        B(Packet pac) {
            ret = pac;
        }

        void add(String name, Packet r) {
            List<Packet> l;
            l = subs.get(name);
            if (l == null) {
                l = new ArrayList();
                subs.put(name, l);
            }
            l.add(r);
        }

        void addx(String name, X r) {
            List<X> l;
            l = subx.get(name);
            if (l == null) {
                l = new ArrayList();
                subx.put(name, l);
            }
            l.add(r);
        }

        public Set<String> keySet() {
            return subx.keySet();
        }

        public List<Packet> get(String name) {
            return subs.get(name);
        }

        public List<X> getx(String name) {
            return subx.get(name);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(AbstractABNF.strd(ret));
            if (!subs.isEmpty()) {
                sb.append("{\r\n");
                for (String name : subs.keySet()) {
                    sb.append(name);
                    sb.append(": ");
                    for (Packet s : subs.get(name)) {
                        sb.append(AbstractABNF.strd(s));
                        sb.append("\r\n");
                    }
                }
                sb.append("}");
            }
            return sb.toString();
        }
    }

    static class C<X> {

        public Packet ret;
        Map<String, List<X>> subs = new HashMap<>();

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

    /**
     * 先頭一致でパースする
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

    /**
     * 名前と一致した要素は細分化して探さない
     *
     * @param pac
     * @param names
     * @return
     */
    public B find(Packet pac, String... names);

    public <X> C<X> findx(Packet pac, ABNFParser<? extends X>... parsers);

    /**
     * 完全一致の判定
     * valの最後まで一致するかどうか判定する
     * @param val
     * @return
     */
    boolean eq(Packet val);

    boolean eq(String val);

    ABNF pl(ABNF... val);

    ABNFor or(ABNF... val);

    /**
     * a*bXXX
     *
     * @param a
     * @param b
     * @return
     */
    public ABNF x(int a, int b);

    /**
     * *XXX
     *
     * @return
     */
    public ABNF x();

    /**
     * 1*XXX
     *
     * @return
     */
    public ABNF ix();

    /**
     * [XXX]
     *
     * @return
     */
    public ABNF c();

    String getName();

    public ABNF name(String name);

}
