package net.siisise.abnf;

import net.siisise.abnf.parser.ABNFParser;
import net.siisise.io.Packet;

/**
 * 軽量一致検索
 * 大抵はこちらで間に合うが繰り返しのあとに同じものがくると解析できないのでABNFplmを使えばいいよ
 * example = *( a / b ) a
 * のようなパターンは無理
 */
public class ABNFpl extends FindABNF {

    protected final ABNF[] list;

    public ABNFpl(ABNF... abnfs) {
        list = abnfs;
        StringBuilder sb = new StringBuilder();
        for (ABNF abnf : list) {
            sb.append(abnf.getName());
            sb.append(" ");
        }
        sb.deleteCharAt(sb.length() - 1);
        name = "( " + sb.toString() + " )";
    }

    /**
     * 
     * @param reg
     * @return 
     */
    @Override
    public ABNFpl copy(ABNFReg reg) {
        ABNF[] cplist = new ABNF[list.length];

        for (int i = 0; i < list.length; i++) {
            cplist[i] = list[i].copy(reg);
        }
        return new ABNFpl(cplist);
    }

    /**
     * 
     * @param <X> 返却予定の型
     * @param pac 解析対象
     * @param parsers サブ解析装置
     * @return 
     */
    @Override
    public <X> C<X> find(Packet pac, ABNFParser<? extends X>... parsers) {
//        System.out.println(getName() + ":" + strd(pac) + ":pl");
        C<X> ret = new ABNF.C();
        ABNFParser[] subparsers;
        boolean n = isName(parsers);
        subparsers = n ? new ABNFParser[0] : parsers;
        
        for (ABNF sub : list) {
            C<X> subret = sub.find(pac, subparsers);
            if (subret == null) {
                pac.backWrite(ret.ret.toByteArray());
                return null;
            }
            mix(ret, subret);
        }
        return n ? sub(ret, parsers) : ret;
    }
}
