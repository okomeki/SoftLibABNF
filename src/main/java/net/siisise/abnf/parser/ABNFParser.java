package net.siisise.abnf.parser;

import net.siisise.abnf.ABNF;
import net.siisise.io.Packet;

/**
 *
 * @author okome
 * @param <T>
 */
public interface ABNFParser<T> {

    ABNF getBNF();

    /**
     * pacから抽出する 一致するところまで読み、一致しないところはpacに残す
     *
     * @param pac
     * @return
     */
    T parse(Packet pac);
    T parse(String src);
}
