package net.siisise.abnf.parser;

import java.util.List;
import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;

/**
 * 要素を抽出するだけ
 * @param <T>
 */
public class ABNFSub<T> extends ABNFList<T, T> {

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
