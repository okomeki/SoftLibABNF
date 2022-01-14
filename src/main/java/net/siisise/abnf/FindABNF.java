package net.siisise.abnf;

import net.siisise.bnf.parser.BNFParser;
import net.siisise.io.FrontPacket;
import net.siisise.io.Packet;

/**
 * findの分離
 * サブ要素を持つ方
 */
public abstract class FindABNF extends AbstractABNF {

    @Override
    public Packet is(FrontPacket src) {
        return is(src, null);
    }
    
    @Override
    public <N> Packet is(FrontPacket src, N ns) {
        C ret = find(src, ns);
        if (ret == null) {
            return null;
        }
        return ret.ret;
    }
    
    public <X> C<X> find(FrontPacket pac, BNFParser<? extends X>... parsers) {
        return find(pac, null, parsers);
    }

    /**
     * 詰め方の工夫をするターン
     *
     * @param <X>
     * @param pac
     * @param ns
     * @param parsers
     * @return
     */
    @Override
    public <X,N> C<X> find(FrontPacket pac, N ns, BNFParser<? extends X>... parsers) {
        BNFParser<? extends X> mp = matchParser(parsers);
        C<X> ret = buildFind(pac, ns, mp == null ? parsers : new BNFParser[0]);
        return ret != null ? subBuild(ret, ns, mp) : null;
    }

    /**
     * find本体
     *
     * @param <X>
     * @param pac
     * @param parsers
     * @return
     */
    abstract <X,N> C<X> buildFind(FrontPacket pac, N ns, BNFParser<? extends X>... parsers);
}
