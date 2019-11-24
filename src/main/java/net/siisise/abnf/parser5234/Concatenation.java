package net.siisise.abnf.parser5234;

import java.util.List;
import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.ABNFplm;
import net.siisise.abnf.parser.ABNFList;

/**
 * 並びを結合する。
 * 類似した要素の解析で、あなろぐなRFCのABNFの想定と一致しない場合もある。
 * 要素が1つの場合はABNFplを省略している。
 * RFC 5432
 * 
 * @author okome
 */
public class Concatenation extends ABNFList<ABNF, ABNF> {

    public Concatenation(ABNF abnf, ABNFReg reg, ABNFReg base) {
        super(abnf, reg, base, "repetition");
    }

    /**
     * 
     * @param pac 子のABNF要素
     * @return 繋げた結果のABNF Concatenation
     */
    @Override
    public ABNF parse(List<ABNF> pac) {
        if (pac.size() == 1) {
            return pac.get(0);
        }
        return new ABNFplm(pac.toArray(new ABNF[pac.size()]));
    }

}
