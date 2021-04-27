package net.siisise.abnf.parser7405;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser.ABNFBaseParser;
import net.siisise.io.FrontPacket;
import net.siisise.io.Packet;

public class QS extends ABNFBaseParser<String,Object> {

    public QS(ABNF rule, ABNFReg reg, ABNFReg base) {
        super(rule, reg, base);
    }
    
    @Override
    public String parse(FrontPacket pac) {
        Packet str = rule.is(pac);
        if ( str == null ) {
            return null;
        }
        str.read();
        str.backRead();
        return str(str);
    }
    
}
