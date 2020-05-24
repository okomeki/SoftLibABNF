package net.siisise.abnf;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.siisise.abnf.parser.ABNFParser;
import net.siisise.bnf.BNF;
import net.siisise.io.FrontPacket;
import net.siisise.io.Packet;
import net.siisise.io.PacketA;

/**
 * RFC 5234.
 * 元はUS-ASCII 7bitコードを想定しているが、UTF-8として利用されている場合もあるので対応する
 * RegExpのPattern相当?
 * ABNFのparserは含まない。
 */
public interface ABNF extends BNF {

    static Charset UTF8 = Charset.forName("utf-8");

    /**
     * 名前または構文っぽいものの取得。
     * 名前がない場合に完全ではないので注意。
     * @return 名前または構文らしきもの
     */
    String getName();

    /**
     * 名付ける。
     * @param name この構文に付与する名前
     * @return 名前付きABNF構文
     */
    ABNF name(String name);

    /**
     * 大文字小文字を区別しない1文字。
     * @param ch UCS2文字
     * @return 文字のABNF構文
     */
    static ABNF text(int ch) {
        return new ABNFtext(ch);
    }

    /**
     * 大文字小文字を区別しない文字列。
     * @param text 文字列
     * @return 文字列のABNF構文
     */
    static ABNF text(String text) {
        return new ABNFtext(text);
    }

    /**
     * 1文字ずつAlternationで結合する。
     * @param chlist 文字の列(UCS2単位)
     * @return 文字のor繋ぎABNF構文
     */
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
     * 
     * @param <X>
     * @param pac 元データ
     * @param parsers おまけで検索するサブ要素
     * @return 検索結果
     */
    <X> C<X> find(FrontPacket pac, ABNFParser<? extends X>... parsers);

    /**
     * 完全一致の判定
     * valの最後まで一致するかどうか判定する
     * @param pac 比較対象データ
     * @return 判定結果
     */
    boolean eq(Packet pac);
    /**
     * 完全一致の判定
     * @param val 比較対象文字列
     * @return 判定結果
     */
    boolean eq(String val);

    /**
     * Concatenation に翻訳される ABNF をスペースで並べた相当の記述。
     * 最長一致は期待しないでよい場合の軽量簡易版。
     * 
     * @param vals 接続したいABNF構文の列挙
     * @return 繋がったABNF構文
     */
    ABNF pl(ABNF... vals);
    
    /**
     * Concatenation に翻訳されるABNF。
     * 難しい繰り返しにも対応する最長一致。
     * 
     * @param vals 接続したいABNF構文の列挙
     * @return 繋がったABNF構文
     */
    ABNF plm(ABNF... vals);
    /**
     * Alternation に翻訳される ABNF / 的な構文の生成装置。
     * 最大一致で検索されるので誤読もある。
     * @param val 接続したいABNF構文の列挙
     * @return Alternationに結合されたABNF構文
     */
    ABNFor or(ABNF... val);
    /**
     * a*bXXX.
     * 
     * @param a 指定しない場合は 0
     * @param b 指定しない場合は -1
     * @return 
     */
    ABNF x(int a, int b);
    /**
     * *XXX 繰り返し
     * @return 繰り返しABNF
     */
    ABNF x();
    /** 1*XXX */
    ABNF ix();
    /**
     * [XXX] 省略可能
     * @return ABNF構文
     */
    ABNF c();

    /**
     * 複製可能な構造を推奨(ループがあると複製は難しい)
     * @param reg 複製もと
     * @return 
     */
    ABNF copy(ABNFReg reg);

    /**
     * 検索結果用構造
     * あと1回書き直したい
     * @param <X> データ型
     */
    static class C<X> {
        /**
         * メインの戻り値
         */
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
