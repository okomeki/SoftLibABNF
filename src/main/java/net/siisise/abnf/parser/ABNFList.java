package net.siisise.abnf.parser;

import java.util.List;
import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.io.Packet;

/**
 *
 * @author okome
 * @param <T> 最終形式
 * @param <M> 中間形式
 */
public abstract class ABNFList<T, M> extends ABNFBaseParser<T, M> {

    /**
     * 
     * @param def
     * @param reg 名前空間参照用
     * @param base Parser駆動用
     * @param subcn 
     */
    protected ABNFList(ABNF def, ABNFReg reg, ABNFReg base, String subcn) {
        super(def, reg, base, subcn);
    }

    @Override
    public T parse(Packet pac) {
        inst();

        ABNF.C<M> sret = def.findx(pac, subs);
        if (sret == null) {
            return null;
        }
        List<M> r = sret.get(subs[0].getBNF());
        // r nullあり
        return parse(r);
    }

    /**
     *
     * @param val sub要素の配列 nullの場合あり
     * @return
     */
    public abstract T parse(List<M> val);
}
