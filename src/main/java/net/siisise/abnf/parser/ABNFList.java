package net.siisise.abnf.parser;

import java.util.ArrayList;
import java.util.List;
import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.io.Packet;

/**
 *
 * @param <T> 最終形式
 * @param <M> 中間形式
 */
public abstract class ABNFList<T, M> extends ABNFBaseParser<T, M> {

    /**
     * 
     * @param def 処理対象のABNF構文
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

        ABNF.C<M> sret = def.find(pac, subs);
        if (sret == null) {
            return null;
        }
        List<M> mlist = sret.get(subs[0].getBNF());
        if ( mlist == null ) {
            mlist = new ArrayList();
        }
        return parse(mlist);
    }

    /**
     *
     * @param val sub要素の配列 nullなしでお試し中
     * @return
     */
    protected abstract T parse(List<M> val);
}
