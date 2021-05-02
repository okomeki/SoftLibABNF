package net.siisise.abnf;

import java.util.ArrayList;
import java.util.List;
import net.siisise.bnf.parser.BNFParser;
import net.siisise.io.FrontPacket;
import net.siisise.lang.CodePoint;

/**
 *
 */
public class ABNFor extends FindABNF {

    private ABNF[] list;

    public ABNFor(ABNF... abnfs) {
        list = abnfs;
        name = toName(abnfs);
    }

    public ABNFor(String n, ABNF... abnfs) {
        name = n;
        list = abnfs;
    }

    /**
     * text として
     * ABNFmap 推奨
     * @param chlist
     */
    public ABNFor(String chlist) {
        FrontPacket src = pac(chlist);
        List<ABNF> abnfs = new ArrayList<>();
        while (src.length() > 0) {
            abnfs.add(new ABNFtext(CodePoint.utf8(src)));
        }
        list = abnfs.toArray(new ABNF[abnfs.size()]);
        name = toName(list);
    }

    public ABNFor(String name, String list) {
        FrontPacket p = pac(list);
        List<ABNF> fs = new ArrayList<>();
        while (p.length() > 0) {
            fs.add(new ABNFtext(CodePoint.utf8(p)));
        }
        this.list = fs.toArray(new ABNF[0]);
        this.name = name;
    }

    static String toName(ABNF[] abnfs) {
        StringBuilder sb = new StringBuilder();
        //if ( list.length > 1) {
        sb.append("( ");
        //}
        for (ABNF v : abnfs) {
            String n = v.getName();
            if (v instanceof ABNFor && n.startsWith("( ") && n.endsWith(" )")) {
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

    @Override
    public ABNFor copy(ABNFReg reg) {
        ABNF[] l = new ABNF[this.list.length];

        for (int i = 0; i < list.length; i++) {
            l[i] = this.list[i].copy(reg);
        }
        return new ABNFor(name, l);
    }

    public void add(ABNF... val) {
        ABNF[] n = new ABNF[list.length + val.length];
        System.arraycopy(list, 0, n, 0, list.length);
        System.arraycopy(val, 0, n, list.length, val.length);
        if (name.contains("(")) {
            name = toName(n);
        }
        list = n;
    }

    @Override
    public <X> C<X> buildFind(FrontPacket pac, BNFParser<? extends X>... parsers) {
        ABNF.C<X> ret = null;

        for (ABNF sub : list) {

            C<X> subret = sub.find(pac, parsers);
            if (subret != null) {
                byte[] data = subret.ret.toByteArray();
                pac.backWrite(data);
                if (ret == null || ret.ret.length() < data.length) {
                    subret.ret.backWrite(data);
                    //if (ret != null) {
                    //    System.out.println("+******DUUPP****+" + subret + "(" + subBuild.toString() + ")");
                    //}
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
