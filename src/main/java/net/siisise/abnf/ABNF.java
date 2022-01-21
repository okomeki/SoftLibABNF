package net.siisise.abnf;

import net.siisise.bnf.BNF;
import net.siisise.io.FrontPacket;

/**
 * RFC 5234.
 * BNFとABNFの差異抽出中。
 * 元はUS-ASCII 7bitコードを想定しているが、UTF-8として利用されている場合もあるので対応する
 * RegExpのPattern相当?
 * ABNFのparserは含まない。
 */
public interface ABNF extends BNF {

    /**
     * 名付ける。
     *
     * @param name この構文に付与する名前
     * @return 名前付きABNF構文
     */
    ABNF name(String name);

    /**
     * 大文字小文字を区別しない1文字。
     *
     * @param ch UCS2文字
     * @return 文字のABNF構文
     */
    static ABNF text(int ch) {
        return new ABNFtext(ch);
    }

    /**
     * 大文字小文字を区別しない文字列。
     *
     * @param text 文字列
     * @return 文字列のABNF構文
     */
    static ABNF text(String text) {
        return new ABNFtext(text);
    }

    /**
     * 1文字ずつAlternationで結合する。
     *
     * @param chlist 文字の列(UCS2単位)
     * @return 文字のor繋ぎABNF構文
     */
    static ABNF list(String chlist) {
        return new ABNFor(chlist);
    }

    /**
     * どれかの文字に一致するかな
     *
     * @param binlist バイト列的な文字列
     * @return どれかが含まれるABNF構文
     */
    static ABNF binlist(String binlist) {
        return new ABNFmap(binlist);
    }

    /**
     * 
     * @param ch 文字
     * @return 文字と一致するABNF
     */
    static ABNFbin bin(int ch) {
        return new ABNFbin(ch);
    }

    /**
     * 文字列/文字との比較
     *
     * @param str utf-8で一致文字列
     * @return 一致するABNF
     */
    static ABNFbin bin(String str) {
        return new ABNFbin(str);
    }

    static ABNFrange range(int min, int max) {
        return new ABNFrange(min, max);
    }

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
     * バイト列単位で厳密に比較する.
     *
     * @param vals 接続したいABNF構文の列挙
     * @return 繋がったABNF構文
     */
    ABNF plm(ABNF... vals);

    /**
     * Concatenation に翻訳されるABNF。
     * 難しい繰り返しにも対応する最長一致。
     * utf-8の単位で厳密に比較する. plmよりは速い?
     *
     * @param vals 接続したいABNF構文の列挙
     * @return 繋がったABNF構文
     */
    ABNF plu(ABNF... vals);

    /**
     * マイナス演算
     * ABNFにはないので仮
     *
     * @deprecated 未定
     * @param val 引かれるABNF
     * @return 引き算できるABNF
     */
    ABNF mn(ABNF val);

    /**
     * Alternation に翻訳される ABNF / 的な構文の生成装置。
     * 最大一致で検索されるので誤読もある。
     *
     * @param val 接続したいABNF構文の列挙
     * @return Alternationに結合されたABNF構文
     */
    ABNF or(ABNF... val);

    /**
     * min*maxXXX.
     *
     * @param min 指定しない場合は 0
     * @param max 指定しない場合は -1
     * @return 繰り返しのABNF
     */
    ABNF x(int min, int max);

    /**
     * *XXX 繰り返し
     *
     * @return 繰り返しABNF
     */
    ABNF x();

    /**
     * 1回以上ループ
     * 1*XXX
     *
     * @return 1回以上繰り返しのABNF
     */
    ABNF ix();

    /**
     * [XXX] 省略可能
     *
     * @return ABNF構文
     */
    ABNF c();

    /**
     * 完全一致の判定
     * valの最後まで一致するかどうか判定する
     *
     * @param pac 比較対象データ
     * @return 判定結果
     */
    boolean eq(FrontPacket pac);

    /**
     * 完全一致の判定
     *
     * @param val 比較対象文字列
     * @return 判定結果
     */
    boolean eq(String val);

    /**
     * 複製可能な構造を推奨(ループがあると複製は難しい)
     *
     * @param reg 複製もと
     * @return ABNFの複製
     */
    ABNF copy(ABNFReg reg);
}
