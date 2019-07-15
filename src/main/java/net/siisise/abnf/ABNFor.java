package net.siisise.abnf;

import java.util.ArrayList;
import java.util.List;
import net.siisise.abnf.parser.ABNFParser;
import net.siisise.io.Packet;
import net.siisise.io.PacketA;

/**
 *
 * @author okome
 */
public class ABNFor extends AbstractABNF {

    private ABNF[] list;

    public ABNFor(ABNF... abnfs) {
        this.list = abnfs;
        name = toName(abnfs);
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

    public ABNFor(String n, ABNF... list) {
        name = n;
        this.list = list;
    }

    /**
     * text として
     *
     * @param chlist
     */
    public ABNFor(String chlist) {
        Packet src = pac(chlist);
        List<ABNF> abnfs = new ArrayList<>();
        while (src.length() > 0) {
            abnfs.add(new ABNFtext(utf8(src)));
        }
        list = abnfs.toArray(new ABNF[abnfs.size()]);
        name = toName(list);
    }

    public ABNFor(String name, String list) {
        Packet p = pac(list);
        List<ABNF> fs = new ArrayList<>();
        while (p.length() > 0) {
            fs.add(new ABNFtext(utf8(p)));
        }
        this.list = fs.toArray(new ABNF[0]);
        this.name = name;
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
    public B find(Packet pac, String... names) {
//        System.out.println(getName() + ":" + strd(pac) + ":or" + pac.length());
        ABNF.B ret = null;
        String[] subnames;
        subnames = isName(names) ? new String[0] : names;

        for (ABNF sub : list) {
            byte[] data = pac.toByteArray();
            pac.backWrite(data);
            Packet d = new PacketA(data);
//            System.out.println("x:" + getName() +";"+ strd(d));

            B subret = sub.find(d, subnames);
            if (subret != null && (ret == null || ret.ret.length() < subret.ret.length())) {
                if (ret != null) {
                    System.out.println("+******DUUPP****+" + subret + "(" +sub.toString()+ ")");
                }
                ret = subret;
            }
        }
        if (ret == null) {
            return null;
        }
//        System.out.println("or " + getName() + " ret:" + ret);
        byte[] e = new byte[(int) ret.ret.length()];
        pac.read(e);
        return sub(ret, names);
    }

    @Override
    public <X> C<X> findx(Packet pac, ABNFParser<? extends X>... parsers) {
//        System.out.println(getName() + ":" + strd(pac) + ":or" + pac.length());
        ABNF.C ret = null;

        for (ABNF sub : list) {
            byte[] data = pac.toByteArray();
            pac.backWrite(data);
            Packet d = new PacketA(data);
//            System.out.println("x:" + sub.getName() +";"+ strd(d));

            C subret = sub.findx(d, parsers);
            if (subret != null && (ret == null || ret.ret.length() < subret.ret.length())) {
                if (ret != null) {
                    System.out.println("+******DUUPP****+" + subret + "(" +sub.toString()+ ")");
                }
                ret = subret;
            }
        }
        if (ret == null) {
            return null;
        }
//        System.out.println("or " + getName() + " ret:" + ret);
        byte[] e = new byte[(int) ret.ret.length()];
        pac.read(e);
        return sub(ret, parsers);
    }
}
