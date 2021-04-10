package net.siisise.bnf.parser;

import net.siisise.bnf.BNF;
import net.siisise.io.FrontPacket;

/**
 * BNF Parserの基本型。
 * @param <T>
 */
public interface BNFParser<T> {

    BNF getBNF();

    /**
     * 先頭一致解析.
     * pacを解析、完了した部分を先頭から削除する
     * 先頭一致で解析するのでpacにデータが残る場合もある
     * 失敗した(一致しなかった)場合、pacは元の状態、戻り値はnullとなる
     * 
     * @param pac 解析対象データ
     * @return 一致しない場合はnull、一致した場合は一致部分
     */
    T parse(FrontPacket pac);
    T parse(String src);
}
