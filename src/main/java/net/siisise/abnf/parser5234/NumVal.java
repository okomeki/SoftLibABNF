package net.siisise.abnf.parser5234;

import java.util.List;
import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser.ABNFBaseParser;
import net.siisise.io.Packet;

/**
 * 数値系まとめてParser
 * @author okome
 */
public class NumVal extends ABNFBaseParser<ABNF, ABNF> {

    public NumVal(ABNFReg reg) {
        super(ABNF5234.numVal);
    }

    public NumVal(ABNF abnf, ABNFReg reg, ABNFReg base) {
        super(abnf);
    }

    @Override
    public ABNF parse(Packet pac) {
        NumSub b = new NumSub(ABNF5234.binVal, ABNF5234.BIT, 2, 'b', 'B');
        NumSub d = new NumSub(ABNF5234.decVal, ABNF5234.DIGIT, 10, 'd', 'D');
        NumSub h = new NumSub(ABNF5234.hexVal, ABNF5234.HEXDIG, 16, 'x', 'X');
        ABNF.C<ABNF> ret = def.findx(pac, b, d, h);
        if (ret == null) {
            return null;
        }

        for (String key : ret.keySet()) {
            List<ABNF> r = ret.get(key);
            if (r != null) {
                return r.get(0);
            }
        }

        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
