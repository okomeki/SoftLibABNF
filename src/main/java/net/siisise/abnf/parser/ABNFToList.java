package net.siisise.abnf.parser;

import java.util.List;
import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;

/**
 *
 * @author okome
 * @param <T> 最終形式
 */
public abstract class ABNFToList<T, M> extends ABNFList<List<T>, M> {

//    public ABNFToList(ABNF def, ABNFParser<M> sub) {
//        super(def, sub);
//    }
//    public ABNFToList(ABNF def, Class<? extends ABNFParser<M>> subc) {
//        super(def, subc);
//    }
    protected ABNFToList(ABNF def, ABNFReg reg, Class<? extends ABNFParser<M>> subc) {
        super(def, reg, subc);
    }

    /**
     * 差し替え可能にしたかった
     * @deprecated 失敗
     * @param def
     * @param reg
     * @param subcn 
     */
    protected ABNFToList(ABNF def, ABNFReg reg, ABNFReg base, String subcn) {
        super(def, reg, base, subcn);
    }
}
