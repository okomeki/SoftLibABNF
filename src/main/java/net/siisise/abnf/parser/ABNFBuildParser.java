package net.siisise.abnf.parser;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.io.FrontPacket;

/**
 * subnsで指定した名の部分を中間型で受け取り、変換後の型で渡すだけのお仕事にするための部分実装。
 *
 * @param <T> 変換後の型
 * @param <M> 中間型
 */
public abstract class ABNFBuildParser<T, M> extends ABNFBaseParser<T, M> {

    /** ABNF Parser側 名前空間 */
    private final ABNFReg base;
    private ABNFParser<? extends M>[] subs;
    protected final String[] subName;

    protected ABNFBuildParser(ABNF rule, ABNFReg base, String... subrulenames) {
        super(rule);
        this.base = base;
        subName = subrulenames;
    }
    
    @Override
    public T parse(FrontPacket pac) {
        return parse(pac, null);
    }

    /**
     * 対象であるかの判定と要素抽出をする
     * @param <N>
     * @param pac
     * @param ns
     * @return
     */
    @Override
    public <N> T parse(FrontPacket pac, N ns) {
        if (subs == null) {
            subs = new ABNFParser[subName.length];
            for (int i = 0; i < subName.length; i++) {
                subs[i] = base.parser(subName[i]);
            }
        }
        ABNF.C<M> val = rule.find(pac, ns, subs);
        if (val == null) {
            return null;
        }
        return build(val, ns);
    }

    protected <N> T build(ABNF.C<M> src, N ns) {
        return build(src);
    }

    protected T build(ABNF.C<M> src) {
        throw new java.lang.UnsupportedOperationException();
    }
}
