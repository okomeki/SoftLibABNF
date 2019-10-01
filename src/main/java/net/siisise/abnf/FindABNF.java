package net.siisise.abnf;

import net.siisise.io.Packet;

/**
 * findの分離
 * サブ要素を持つ方
 */
public abstract class FindABNF extends AbstractABNF {

    @Override
    public Packet is(Packet src) {
        C ret = findx(src);
        if (ret == null) {
            return null;
        }
        return ret.ret;
    }

    /**
     * findx に繋がない実装なので低速
     * @param <X>
     * @param pac
     * @param parsers
     * @return 
     */
/*
    @Override
    public <X> C<X> findx(Packet pac, ABNFParser<? extends X>... parsers) {
//        System.out.println("findx:" +getName()+":"+ strd(pac));
        List<String> names = new ArrayList<>();
        for (ABNFParser<? extends X> p : parsers) {
            names.add(p.getBNF().getName());
        }
        B<X> ret = find(pac, names.toArray(new String[parsers.length]));
        if (ret == null) {
            return null;
        }
//        System.out.println("findx2:" + ret);
        C<X> cret = new C<>(ret.ret);
        for (ABNFParser<? extends X> p : parsers) {
            String pname = p.getBNF().getName();
            List<Packet> subps = ret.subs.get(pname);
            if (subps != null) {
                for (Packet subp : subps) {
//                    System.out.println("findx2s:" + p.getClass().getName() + " : " + strd(subp));
                    cret.add(pname, p.parse(subp));
                }
            }
        }
        return cret; // sub(cret,parsers);
    }
*/
}
