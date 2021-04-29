package net.siisise.abnf.parser;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.io.FrontPacket;

/**
 * subnsで指定した名の部分を中間型で受け取り、変換後の型で渡すだけのお仕事にするための部分実装。
 * @param <T> 変換後の型
 * @param <M> 中間型
 */
public abstract class ABNFBuildParser<T, M> extends ABNFBaseParser<T, M> {

    /** ABNF Parser側 名前空間 */
    private final ABNFReg base;
    private ABNFParser<? extends M>[] subs;
    protected final String[] subName;

    protected ABNFBuildParser(ABNF rule) {
        super(rule);
        base = null;
        subName = null;
        subs = new ABNFParser[0];
    }

    protected ABNFBuildParser(ABNF rule, ABNFReg base, String... subrulenames) {
        super(rule, null);
        this.base = base;
        subName = subrulenames;
    }

    protected ABNFBuildParser(ABNF rule, Object reg, ABNFReg base, String... subrulenames) {
        super(rule, reg);
        this.base = base;
        subName = subrulenames;
    }

     /**
     * 対象であるかの判定と要素抽出をする
     * @param pac
     * @return 
     */
    @Override
    public T parse(FrontPacket pac) {
        if (subs == null) {
            subs = new ABNFParser[subName.length];
            for (int i = 0; i < subName.length; i++) {
                subs[i] = base.parser(subName[i], reg);
            }
        }
        ABNF.C<M> val = rule.find(pac, subs);
        if (val == null) {
            return null;
        }
        return build(val);
    }

    protected abstract T build(ABNF.C<M> src);
}
