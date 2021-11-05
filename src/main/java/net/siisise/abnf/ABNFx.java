package net.siisise.abnf;

import net.siisise.bnf.parser.BNFParser;
import net.siisise.io.FrontPacket;

/**
 * ループの結果は最長だけでなく、各長さで判定したいこともあり
 *
 */
public class ABNFx extends FindABNF {

    // a 初期値: 0
    // b 初期値: -1
    private final int a, b;
    private final ABNF abnf;

    ABNFx(int a, int b, AbstractABNF abnf) {
        this.a = a;
        this.b = b;
        this.abnf = abnf;
        StringBuilder sb = new StringBuilder();
        if (a == 0 && b == 1) {
            String n = abnf.getName();
            if ((abnf instanceof ABNFor || abnf instanceof ABNFpl)
                    && n.startsWith("( ") && n.endsWith(" )")) {
                n = n.substring(2, n.length() - 2);
            }
            name = sb.append("[ ").append(n).append(" ]").toString();
        } else {
            if (a == b) {
                sb.append(a);
            } else {
                if (a != 0) {
                    sb.append(a);
                }
                sb.append("*");
                if (b >= 0) {
                    sb.append(b);
                }
            }
            name = sb.toString() + abnf.getName();
        }
    }

    @Override
    public ABNFx copy(ABNFReg reg) {
        return new ABNFx(a, b, (AbstractABNF) abnf.copy(reg));
    }

    @Override
    public <X> C<X> buildFind(FrontPacket pac, BNFParser<? extends X>... names) {
        C<X> ret = new C<>();
        for (int i = 0; b == -1 || i < b; i++) {
//            System.out.println(abnf+":" + strd(ret.ret)+"%"+strd(pac));
            C sub = abnf.find(pac, names);
            if (sub == null) {
                if (i < a) {
                    byte[] data = ret.ret.toByteArray();
                    pac.dbackWrite(data);
                    return null;
                } else {
                    return ret;
                }
            }
            mix(ret, sub);
        }
        return ret;
    }
}
