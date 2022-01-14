package net.siisise.abnf.parser7405;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser.ABNFBuildParser;
import net.siisise.bnf.BNF;
import net.siisise.io.Packet;

public class QS extends ABNFBuildParser<String,Packet> {

    public QS(ABNF rule, ABNFReg base) {
        super(rule, base);
    }

    @Override
    protected String build(BNF.C<Packet> src) {
        Packet str = src.ret;
        str.read();
        str.backRead();
        return str(str);
    }
    
}
