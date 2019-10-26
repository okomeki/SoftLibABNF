package net.siisise.abnf.parser;

import net.siisise.abnf.ABNF;
import net.siisise.io.Packet;

/**
 * ABNFの構文解析装置
 * @param <T>
 */
public interface ABNFParser<T> {

    ABNF getBNF();

    /**
     * 先頭一致解析
     * pacから抽出する 一致するところまで読み、一致しないところはpacに残す
     *
     * @param pac
     * @return 一致しない場合はnull、一致した場合は一致部分
     */
    T parse(Packet pac);
    T parse(String src);
}
