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
        C ret = find(src);
        if (ret == null) {
            return null;
        }
        return ret.ret;
    }
    
    /**
     * 詰め方の工夫をするターン
     * @param <X>
     * @param pac
     * @param parsers
     * @return 
     */
    @Override
    public <X> C<X> find(FrontPacket pac, BNFParser<? extends X>... parsers) {
        BNFParser<? extends X> mp = matchParser(parsers);
        C<X> ret = buildFind(pac, mp == null ? parsers : new BNFParser[0]);
        return ret != null ? subBuild(ret, mp) : null;
    }
    
    /**
     * find本体
     * @param <X>
     * @param pac
     * @param parsers
     * @return 
     */
    abstract <X> C<X> buildFind(FrontPacket pac, BNFParser<? extends X>... parsers);
}
