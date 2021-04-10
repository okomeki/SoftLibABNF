package net.siisise.abnf.parser;

import java.util.ArrayList;
import java.util.List;
import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.io.FrontPacket;

/**
 * リスト系のものをパース後特定型にして返す.
 * @param <T> 最終形式 抽出型
 * @param <M> 要素の形式
 */
public abstract class ABNFList<T, M> extends ABNFBaseParser<T, M> {

    /**
     * 
     * @param def 処理対象のABNF構文
     * @param reg 名前空間参照用
     * @param base Parser駆動用
     * @param subcn 含まれる要素
     */
    protected ABNFList(ABNF def, ABNFReg reg, ABNFReg base, String... subcn) {
        super(def, reg, base, subcn);
    }

    @Override
    public T parse(FrontPacket pac) {
        inst();

        ABNF.C<M> sret = def.find(pac, subs);
        if (sret == null) {
            return null;
        }
        List<M> mlist = new ArrayList<>();
        for ( ABNFParser<? extends M> sub : subs ) {
            List<M> s = sret.get(sub.getBNF());
            if ( s != null ) {
                mlist.addAll(s);
            }
        }
        return parse(mlist);
    }

    /**
     *
     * @param val nullなし 並び順はsubcn順の中で並び順になるので注意
     * @return
     */
    protected abstract T parse(List<M> val);
}
