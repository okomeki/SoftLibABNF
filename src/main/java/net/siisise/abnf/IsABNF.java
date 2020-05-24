package net.siisise.abnf;

import net.siisise.abnf.parser.ABNFParser;
import net.siisise.io.FrontPacket;
import net.siisise.io.Packet;

/**
 * findの分離
 * サブ要素を持たない方
 */
public abstract class IsABNF extends AbstractABNF {

    /**
     * sub要素のない場合の軽い対応
     * @param <X>
     * @param pac
     * @param parsers
     * @return 
     */
    @Override
    public <X> C<X> find(FrontPacket pac, ABNFParser<? extends X>... parsers) {
        Packet r = is(pac);
        if (r == null) {
            return null;
        }
        return sub(new C(r), parsers);
    } 
}
