package net.siisise.bnf;

import net.siisise.bnf.parser.BNFParser;
import net.siisise.io.FrontPacket;

/**
 *
 */
public class BNFor extends FindBNF {

    private final BNF[] list;

    public BNFor(BNF... abnfs) {
        list = abnfs;
        name = toName(abnfs);
    }

    public BNFor(String n, BNF... abnfs) {
        name = n;
        list = abnfs;
    }

    static String toName(BNF[] abnfs) {
        StringBuilder sb = new StringBuilder();
        //if ( list.length > 1) {
        sb.append("( ");
        //}
        for (BNF v : abnfs) {
            String n = v.getName();
            if (v instanceof BNFor && n.startsWith("( ") && n.endsWith(" )")) {
                n = n.substring(2, n.length() - 2);
                sb.append(n);
            } else {
                sb.append(v.getName());
            }
            sb.append(" / ");
        }
        sb.delete(sb.length() - 3, sb.length());
        //if ( list.length > 1 ) {
        sb.append(" )");
        //}
        return sb.toString();
    }

    /**
     *
     * @param <X> return object type
     * @param <N> user name space type
     * @param pac
     * @param ns user name space
     * @param parsers
     * @return
     */
    @Override
    public <X,N> C<X> buildFind(FrontPacket pac, N ns, BNFParser<? extends X>... parsers) {
        BNF.C ret = null;

        for (BNF sub : list) {

            C subret = sub.find(pac, ns, parsers);
            if (subret != null) {
                byte[] data = subret.ret.toByteArray();
                pac.backWrite(data);
                if (ret == null || ret.ret.length() < data.length) {
                    subret.ret.backWrite(data);
//                    if (ret != null) {
//                        System.out.println("+******DUUPP****+" + subret + "(" + sub.toString() + ")");
//                    }
                    ret = subret;
                }
            }
        }
        if (ret == null) {
            return null;
        }

        byte[] e = new byte[(int) ret.ret.length()];
        pac.read(e);
        return ret;
    }
}
