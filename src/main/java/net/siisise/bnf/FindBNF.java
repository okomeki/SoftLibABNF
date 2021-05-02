package net.siisise.bnf;

import net.siisise.bnf.parser.BNFParser;
import net.siisise.io.FrontPacket;
import net.siisise.io.Packet;

/**
 *
 */
public abstract class FindBNF extends AbstractBNF {

    @Override
    public Packet is(FrontPacket src) {
        C ret = find(src);
        if (ret == null) {
            return null;
        }
        return ret.ret;
    }
    
    public <X> C<X> find(FrontPacket pac, BNFParser<? extends X>... parsers) {
        BNFParser<? extends X> mp = matchParser(parsers);
        C<X> ret = buildFind(pac, mp == null ? parsers : new BNFParser[0]);
        return ret != null ? subBuild(ret, mp) : null;
    }
    
    abstract <X> C<X> buildFind(FrontPacket pac, BNFParser<? extends X>... parsers);
}
