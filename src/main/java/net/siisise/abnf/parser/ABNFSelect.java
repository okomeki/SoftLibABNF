package net.siisise.abnf.parser;

import java.util.List;
import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.io.FrontPacket;

/**
 * 一致するものを選択する (分岐).
 * @param <T>
 */
public class ABNFSelect<T> extends ABNFBuildParser<T, T> {

    protected ABNFSelect(ABNF rule, ABNFReg base, String...  subcs) {
        super(rule, base, subcs);
    }

    protected ABNFSelect(ABNF rule, Object reg, ABNFReg base, String...  subcs) {
        super(rule, reg, base, subcs);
    }

    @Override
    protected T build(ABNF.C<T> pac) {
        for (ABNFParser<? extends T> p : subs) {
            List<T> r = pac.get(p.getBNF());
            if (r != null) {
                return r.get(0);
            }
        }
        return other(pac.ret);
    }

    /**
     * 拡張する猶予.
     * @param pac
     * @return 
     */
    protected T other(FrontPacket pac) {
        return null;
    }

}
