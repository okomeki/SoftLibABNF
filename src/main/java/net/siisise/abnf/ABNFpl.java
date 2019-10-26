package net.siisise.abnf;

import net.siisise.abnf.parser.ABNFParser;
import net.siisise.io.Packet;

/**
 *
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

/*    
    @Override
    public <X> C<X> findx2(Packet pac, ABNFParser<? extends X>... parsers) {
        System.out.println(getName() + ":" + strd(pac) + ":pl");
        C<X> ret = new ABNF.C(new PacketA());

        for (ABNF sub : list) {
            C subret = sub.find(pac, parsers);
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
