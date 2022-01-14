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

    /**
     * 
     * @param rule 処理対象のABNF構文
     * @param base subrulenameのParser駆動用
     * @param subrulenames 含まれる要素
     */
    protected ABNFList(ABNF rule, ABNFReg base, String... subrulenames) {
        super(rule, base, subrulenames);
    }

    @Override
    protected T build(ABNF.C<M> sret) {
        List<M> mlist = new ArrayList<>();
        for ( String sub : subName ) {
            List<M> s = sret.get(sub);
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
