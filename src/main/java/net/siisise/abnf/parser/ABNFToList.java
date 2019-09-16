package net.siisise.abnf.parser;

import java.util.List;
import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;

/**
 *
 * @author okome
 * @param <T> 最終形式
 * @param <M>
 */
public abstract class ABNFToList<T, M> extends ABNFList<List<T>, M> {

    /**
     * 差し替え可能にしたかった
     * @param def
     * @param reg
     * @param base
     * @param subcn 
     */
    protected ABNFToList(ABNF def, ABNFReg reg, ABNFReg base, String subcn) {
        super(def, reg, base, subcn);
    }
}
