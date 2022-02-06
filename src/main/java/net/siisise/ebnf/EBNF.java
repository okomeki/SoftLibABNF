package net.siisise.ebnf;

import net.siisise.bnf.BNF;

/**
 * ISO 14977 くらいの標準で?
 */
public interface EBNF extends BNF<EBNF> {

    /**
     * 
     * @param ch 文字
     * @return 文字と一致するABNF
     */
    static EBNF bin(int ch) {
        return new EBNFbin(ch);
    }

    static EBNF bin(String str) {
        return new EBNFbin(str);
    }

    static EBNFrange range(int min, int max) {
        return new EBNFrange(min, max);
    }

    /**
     * 複製可能な構造を推奨(ループがあると複製は難しい)
     *
     * @param reg 複製先
     * @return ABNFの複製
     */
    EBNF copy(EBNFReg reg);

    public EBNF mn(EBNF bnf);


}
