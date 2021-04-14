package net.siisise.abnf.parser;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.io.FrontPacket;

/**
 * 一致するものを選択する (分岐).
 * @param <T>
 */
public class ABNFSelect<T> extends ABNFBaseParser<T, T> {

    protected ABNFSelect(ABNF rule, ABNFReg reg, ABNFReg base, String...  subcs) {
        super(rule, reg, base, subcs);
    }

    @Override
    public T parse(FrontPacket pac) {
        inst();
        for (ABNFParser<? extends T> p : subs) {
            T r = p.parse(pac);
            if (r != null) {
                return r;
            }
        }
        return other(pac);
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
