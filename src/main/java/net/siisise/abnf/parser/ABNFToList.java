package net.siisise.abnf.parser;

import java.util.List;
import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;

/**
 *
 * @deprecated ABNFListにまとめる
 * @param <T> 最終形式
 * @param <M>
 */
public abstract class ABNFToList<T, M> extends ABNFList<List<T>, M> {

    /**
     * 差し替え可能にしたかった
     * @param rule
     * @param reg
     * @param base
     * @param subcn 
     */
    protected ABNFToList(ABNF rule, ABNFReg reg, ABNFReg base, String subcn) {
        super(rule, reg, base, subcn);
    }
}
