package net.siisise.abnf.parser;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.io.Packet;

/**
 * 一致するものを選択する (分岐)
 * @author okome
 * @param <T>
 */
public class ABNFSelect<T> extends ABNFBaseParser<T, T> {

    protected ABNFSelect(ABNF def, ABNFReg reg) {
        super(def, reg);
    }

    protected ABNFSelect(ABNF def, ABNFReg reg, ABNFParser<T>... sub) {
        super(def, reg, sub);
    }

//    protected ABNFSelect(ABNF def, Class<? extends ABNFParser<T>>... subc) {
//        super(def,subc);
//    }
    protected ABNFSelect(ABNF def, ABNFReg reg, Class<? extends ABNFParser<? extends T>>... subc) {
        super(def, reg, subc);
    }

    protected ABNFSelect(ABNF def, ABNFReg reg, ABNFReg base, String...  subcs) {
        super(def, reg, base, subcs);
    }

    @Override
    public T parse(Packet pac) {
        inst();
        for (ABNFParser<? extends T> p : subs) {
            T r = p.parse(pac);
            if (r != null) {
                return r;
            }
        }
        return other(pac);
    }

    protected T other(Packet pac) {
        return null;
    }

}
