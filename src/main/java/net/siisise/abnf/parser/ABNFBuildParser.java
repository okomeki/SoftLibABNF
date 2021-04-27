package net.siisise.abnf.parser;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.io.FrontPacket;

/**
 *
 * @param <T>
 * @param <M>
 */
public abstract class ABNFBuildParser<T, M> extends ABNFBaseParser<T, M> {

    protected ABNFBuildParser(ABNF rule) {
        super(rule);
    }

    protected ABNFBuildParser(ABNF rule, ABNFReg base, String... subns) {
        super(rule, null, base, subns);
    }

    protected ABNFBuildParser(ABNF rule, Object reg, ABNFReg base, String... subns) {
        super(rule, reg, base, subns);
    }

    @Override
    public T parse(FrontPacket pac) {
        inst();
        ABNF.C<M> ret = rule.find(pac, subs);
        if (ret == null) {
            return null;
        }
        return build(ret);
    }

    protected abstract T build(ABNF.C<M> src);
}
