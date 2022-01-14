package net.siisise.bnf;

import net.siisise.bnf.parser.BNFParser;
import net.siisise.io.FrontPacket;
import net.siisise.io.Packet;

/**
 * isとfindをis側でなんとかする系
 */
public abstract class IsBNF extends AbstractBNF {

    /**
     *
     * @param <N>
     * @param pac
     * @param ns
     * @return
     */
    @Override
    public <N> Packet is(FrontPacket pac, N ns) {
        return is(pac);
    }
    
    
    @Override
    public <X,N> C<X> find(FrontPacket pac, N ns, BNFParser<? extends X>... parsers) {
        Packet r = is(pac,ns);
        if (r == null) {
            return null;
        }
        return subBuild(new C(r), ns, matchParser(parsers));
    }
}
