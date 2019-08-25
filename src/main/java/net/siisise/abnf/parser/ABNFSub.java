package net.siisise.abnf.parser;

import java.util.List;
import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;

/**
 * 要素を抽出するだけ
 * @author okome
 * @param <T>
 */
public class ABNFSub<T> extends ABNFList<T, T> {

//    public ABNFSub(ABNF def, ABNFParser<T> subs) {
//        super(def, subs);
//    }
    /**
     * 
     * @param def
     * @param reg 名前空間参照用
     * @param subs 
     */
    protected ABNFSub(ABNF def, ABNFReg reg, ABNFParser<T> subs) {
        super(def, reg, subs);
    }

    protected ABNFSub(ABNF def, ABNFReg reg, Class<? extends ABNFParser<T>> subc) {
        super(def, reg, subc);
    }

    /**
     * 
     * @param def
     * @param reg 名前空間参照用
     * @param base Parser駆動用
     * @param subcn 
     */
    protected ABNFSub(ABNF def, ABNFReg reg, ABNFReg base, String subcn) {
        super(def, reg, base, subcn);
    }

    @Override
    public T parse(List<T> val) {
        return val.get(0);
    }

}
