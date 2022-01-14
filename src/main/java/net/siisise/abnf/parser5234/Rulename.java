package net.siisise.abnf.parser5234;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser.ABNFBaseParser;
import net.siisise.io.FrontPacket;

/**
 *
 */
public class Rulename extends ABNFBaseParser<ABNF, Object> {

    public Rulename(ABNF rule, ABNFReg base) {
        super(rule);
    }

    @Override
    public <N> ABNF parse(FrontPacket pac, N ns) {
        FrontPacket name = rule.is(pac);
        if (name == null) {
            return null;
        }
        return ((ABNFReg)ns).ref(str(name));
    }

    @Override
    public ABNF parse(FrontPacket pac) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
