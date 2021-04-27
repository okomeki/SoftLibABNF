package net.siisise.abnf.parser;

import java.util.ArrayList;
import java.util.List;
import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;

/**
 * リスト系のものをパース後特定型にして返す.
 * @param <T> 最終形式 抽出型
 * @param <M> 要素の形式
 */
public abstract class ABNFList<T, M> extends ABNFBuildParser<T, M> {

    protected ABNFList(ABNF rule, ABNFReg base, String... subcn) {
        super(rule, base, subcn);
    }

    /**
     * 
     * @param rule 処理対象のABNF構文
     * @param reg 名前空間参照用
     * @param base Parser駆動用
     * @param subcn 含まれる要素
     */
    protected ABNFList(ABNF rule, Object reg, ABNFReg base, String... subcn) {
        super(rule, reg, base, subcn);
    }

    @Override
    protected T build(ABNF.C<M> sret) {
        List<M> mlist = new ArrayList<>();
        for ( ABNFParser<? extends M> sub : subs ) {
            List<M> s = sret.get(sub.getBNF());
            if ( s != null ) {
                mlist.addAll(s);
            }
        }
        return build(mlist);
    }

    /**
     * 構築過程
     * @param val nullなし 並び順はsubcn順の中で並び順になるので注意
     * @return
     */
    protected abstract T build(List<M> val);
}
