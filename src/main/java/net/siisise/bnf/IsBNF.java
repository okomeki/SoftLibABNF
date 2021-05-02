package net.siisise.bnf;

import net.siisise.bnf.parser.BNFParser;
import net.siisise.io.FrontPacket;
import net.siisise.io.Packet;

/**
 *
 */
public abstract class IsBNF extends AbstractBNF {

    @Override
    public <X> C<X> find(FrontPacket pac, BNFParser<? extends X>... parsers) {
        Packet r = is(pac);
        if (r == null) {
            return null;
        }
        return subBuild(new C(r), matchParser(parsers));
    }
}
