package net.siisise.abnf.parser5234;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser.ABNFSelect;

/**
 * 数値系まとめてParser
 */
public class NumVal extends ABNFSelect<ABNF> {

    public NumVal(ABNF rule, ABNFReg reg, ABNFReg base) {
        super(rule, reg, base, "bin-val", "dec-val", "hex-val");
    }

    public static class BinVal extends NumSub {

        public BinVal(ABNF rule, ABNFReg reg, ABNFReg base) {
            super(rule, ABNF5234.BIT, 2, 'b', 'B');
        }
    }

    public static class DecVal extends NumSub {

        public DecVal(ABNF rule, ABNFReg reg, ABNFReg base) {
            super(rule, ABNF5234.DIGIT, 10, 'd', 'D');
        }
    }

    public static class HexVal extends NumSub {

        public HexVal(ABNF rule, ABNFReg reg, ABNFReg base) {
            super(rule, ABNF5234.HEXDIG, 16, 'x', 'X');
        }
    }
}
