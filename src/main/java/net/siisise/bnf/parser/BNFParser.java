package net.siisise.bnf.parser;

import net.siisise.bnf.BNF;
import net.siisise.io.FrontPacket;

/**
 * BNF Parserの基本型。
 * @param <T> 戻り型
 */
public interface BNFParser<T> {

    BNF getBNF();

    /**
     * 先頭一致解析.
     * 構築?
     * parse を直接呼び出す場合、findを一度通して判定が必要。
     * findのパラメータとして渡す場合、rulename で判定するため一致判定は不要。
     * というわけなので2つに分割する?
     * 
     * pacを解析、完了した部分を先頭から削除する
     * 先頭一致で解析するのでpacにデータが残る場合もある
     * 失敗した(一致しなかった)場合、pacは元の状態、戻り値はnullとなる
     *
     * @param pac 解析対象データ
     * @param ns user name space
     * @return 一致しない場合はnull、一致した場合は一致部分
     */
    T parse(FrontPacket pac, Object ns);
    T parse(FrontPacket pac);
    T parse(String src, Object ns);
    T parse(String src);
}
