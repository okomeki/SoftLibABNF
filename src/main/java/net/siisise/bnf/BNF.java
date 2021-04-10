package net.siisise.bnf;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.siisise.bnf.parser.BNFParser;
import net.siisise.io.FrontPacket;
import net.siisise.io.Packet;
import net.siisise.io.PacketA;

/**
 * 抽象的なBNF全般
 */
public interface BNF {
    
    static Charset UTF8 = Charset.forName("utf-8");

    /**
     * 名前または構文っぽいものの取得。
     * 名前がない場合に完全ではないので注意。
     * @return 名前または構文らしきもの
     */
    String getName();

    /**
     * 先頭一致でパースする。
     * findとどちらかの実装が必要、片方は省略可能
     * @param pac
     * @return 一致した範囲
     */
    Packet is(FrontPacket pac);

    /**
     * 先頭一致でパースする。
     * 
     * @param val
     * @return 一致ありなし
     */
    boolean is(String val);

    /**
     * サブ要素含んで抽出するパーサな機能.
     * @param <X> 返し型
     * @param pac 元データ
     * @param parsers おまけで検索するサブ要素
     * @return 検索結果
     */
    <X> C<X> find(FrontPacket pac, BNFParser<? extends X>... parsers);

    /**
     * 検索結果用構造
     * あと1回書き直したい
     * @param <X> データ型
     */
    public static class C<X> {
        /**
         * メインの戻り値
         */
        public Packet ret;
        public Map<String, List<X>> subs = new HashMap<>();

        public C() {
            ret = new PacketA();
        }

        public C(Packet pac) {
            ret = pac;
        }

        public void add(String name, X r) {
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

        public List<X> get(BNF bnf) {
            return get(bnf.getName());
        }
    }
}
