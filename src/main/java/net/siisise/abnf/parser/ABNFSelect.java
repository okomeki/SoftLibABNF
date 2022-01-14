package net.siisise.abnf.parser;

import java.util.List;
import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.io.FrontPacket;

/**
 * 一致するものを選択する (分岐).
 * 含まれている要素(名前)なので全体一致ではなくていい
 * @param <T>
 */
public class ABNFSelect<T> extends ABNFBuildParser<T, T> {

    protected ABNFSelect(ABNF rule, ABNFReg base, String...  casenames) {
        super(rule, base, casenames);
    }

    /**
     *
     * @param pac
     * @return
     */
    @Override
    protected T build(ABNF.C<T> pac) {
        for (String sub : subName) {
            List<T> r = pac.get(sub);
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
