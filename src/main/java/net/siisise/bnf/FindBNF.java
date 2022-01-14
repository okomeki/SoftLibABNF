package net.siisise.bnf;

import net.siisise.bnf.parser.BNFParser;
import net.siisise.io.FrontPacket;
import net.siisise.io.Packet;

/**
 * isとfindの実装が必要だが
 * find側の実装だけで済ませたい処理.
 */
public abstract class FindBNF extends AbstractBNF {

    /**
     * 単純な比較をfind で処理する.
     * @param <N> user name space type
     * @param src
     * @param ns user name space 未使用時はnull可能
     * @return 一致した場合
     */
    @Override
    public <N> Packet is(FrontPacket src, N ns) {
        C ret = find(src,ns);
        if (ret == null) {
            return null;
        }
        return ret.ret;
    }
    
    /**
     * 単純な比較をfind で処理する.
     * @param src
     * @return
     */
    @Override
    public Packet is(FrontPacket src) {
        return is(src, null);
    }
    
    /**
     *
     * @param <X>
     * @param <N> user name space type 名前空間型
     * @param pac
     * @param ns user name space ユーザ名前空間
     * @param parsers
     * @return
     */
    @Override
    public <X,N> C<X> find(FrontPacket pac, N ns, BNFParser<? extends X>... parsers) {
        BNFParser<? extends X> mp = matchParser(parsers);
        C<X> ret = buildFind(pac, mp == null ? parsers : new BNFParser[0]);
        return ret != null ? subBuild(ret, ns, mp) : null;
    }
    
    abstract <X,N> C<X> buildFind(FrontPacket pac, N ns, BNFParser<? extends X>... parsers);
}
