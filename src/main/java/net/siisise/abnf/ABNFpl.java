package net.siisise.abnf;

import net.siisise.io.Packet;
import net.siisise.io.PacketA;

/**
 *
 * @author okome
 */
public class ABNFpl extends AbstractABNF {

    private final ABNF[] list;

    public ABNFpl(ABNF[] list) {
        this.list = list;
        StringBuilder sb = new StringBuilder();
        for (ABNF a : list) {
            sb.append(a.getName());
            sb.append(" ");
        }
        sb.deleteCharAt(sb.length() - 1);
        name = "( " + sb.toString() + " )";
    }

    @Override
    public ABNFpl copy(ABNFReg reg) {
        ABNF[] l = new ABNF[this.list.length];

        for (int i = 0; i < list.length; i++) {
            l[i] = this.list[i].copy(reg);
        }
        return new ABNFpl(l);
    }

    @Override
    public B find(Packet pac, String... names) {
//        System.out.println(getName() + ":" + strd(pac) + ":pl");
        B ret = new ABNF.B(new PacketA());
        String[] subnames;
        subnames = isName(names) ? new String[0] : names;

        for (ABNF sub : list) {
            B subret = sub.find(pac, subnames);
            if (subret == null) {
                pac.backWrite(ret.ret.toByteArray());
                return null;
            }
            mix(ret, subret);
        }
        return sub(ret, names);
    }
    /*
    @Override
    public <X> C<X> findx(Packet pac, ABNFParser<? extends X>... parsers) {
//        System.out.println(getName() + ":" + strd(pac) + ":pl");
        C ret = new ABNF.C(new PacketA());
        String[] subnames;
        for ( ABNFParser<? extends X> p : parsers ) {
            
        }
        
        subnames = isName(names) ? new String[0] : names;

        for (ABNF sub : list) {
            C subret = sub.findx(pac, subnames);
            if (subret == null) {
                pac.backWrite(ret.ret.toByteArray());
                return null;
            }
            mix(ret, subret);
        }
        return sub(ret, parsers);
    }
     */
 /*    
    @Override
    public <X> C<X> findx2(Packet pac, ABNFParser<? extends X>... parsers) {
        System.out.println(getName() + ":" + strd(pac) + ":pl");
        C<X> ret = new ABNF.C(new PacketA());

        for (ABNF sub : list) {
            C subret = sub.findx(pac, parsers);
            if (subret == null) {
                pac.backWrite(ret.ret.toByteArray());
                return null;
            }
            mix(ret, subret);
        }
        return sub(ret, parsers);
    }
     */
}
