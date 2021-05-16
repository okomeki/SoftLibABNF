package net.siisise.abnf;

import net.siisise.bnf.parser.BNFParser;
import net.siisise.io.FrontPacket;

/**
 * 軽量一致検索。
 * 大抵はこちらで間に合うが繰り返しのあとに同じものがくると解析できないのでABNFplmを使えばいいよ
 * example = *( a / b ) a
 * のようなパターンは無理
 */
public class ABNFpl extends FindABNF {

    protected final ABNF[] list;

    public ABNFpl(ABNF... abnfs) {
        list = abnfs;
        StringBuilder names = new StringBuilder();
        for (ABNF abnf : list) {
            names.append(abnf.getName());
            names.append(" ");
        }
        names.deleteCharAt(names.length() - 1);
        name = "( " + names.toString() + " )";
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
    public <X> C<X> buildFind(FrontPacket pac, BNFParser<? extends X>... parsers) {
        C<X> ret = new C<>();
        
        for (ABNF sub : list) {
            C<X> subret = sub.find(pac, parsers);
            if (subret == null) {
                pac.backWrite(ret.ret.toByteArray());
                return null;
            }
            mix(ret, subret);
        }
        return ret;
    }
}
