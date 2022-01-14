package net.siisise.abnf;

import net.siisise.bnf.parser.BNFParser;
import net.siisise.io.FrontPacket;
import net.siisise.io.Packet;

/**
 * findの分離
 * サブ要素を持たない方
 */
public abstract class IsABNF extends AbstractABNF {
    
    @Override
    public <N> Packet is(FrontPacket pac, N ns) {
        return is(pac);
    }

    /**
     * sub要素のない場合の軽い対応
     * @param <X>
     * @param pac
     * @param ns
     * @param parsers
     * @return 
     */
    @Override
    public <X,N> C<X> find(FrontPacket pac, N ns, BNFParser<? extends X>... parsers) {
        Packet r = is(pac, ns);
        if (r == null) {
            return null;
        }
        return subBuild(new C(r), ns, matchParser(parsers));
    }
}
