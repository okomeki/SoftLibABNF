package net.siisise.abnf;

import net.siisise.io.Packet;
import net.siisise.io.PacketA;

/**
 *
 * @author okome
 */
public class ABNFx extends AbstractABNF {

    // a 初期値: 0
    // b 初期値: -1
    int a, b;
    ABNF abnf;

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
    public B find(Packet pac, String... names) {
        if (isName(names)) { // ないかも
            B p = find(pac);
            if (p == null) {
                return null;
            }
            return sub(p, names);
        }

        B ret = new B(new PacketA());
        for (int i = 0; b == -1 || i < b; i++) {
//            System.out.println(abnf+":" + strd(ret.ret)+"%"+strd(pac));
            B sub = abnf.find(pac, names);
            if (sub == null) {
                if (i < a) {
                    byte[] data = ret.ret.toByteArray();
                    pac.backWrite(data);
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
