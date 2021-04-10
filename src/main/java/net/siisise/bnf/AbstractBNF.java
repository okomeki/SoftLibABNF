package net.siisise.bnf;

import net.siisise.bnf.parser.BNFParser;
import net.siisise.io.Packet;
import net.siisise.io.PacketA;

/**
 *
 */
public abstract class AbstractBNF implements BNF {

    /**
     * 名前、またはABNFに近いもの (まだ抜けもある)
     */
    String name;

    @Override
    public String getName() {
        if (name == null) {
            return "???";
        }
        return name;
    }

    public BNF name(String name) {
        return new BNFor(name, this); // ?
    }

    protected boolean isName(BNFParser<?>[] parsers) {
        for (BNFParser p : parsers ) {
            if (name.equals(p.getBNF().getName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean is(String val) {
        return is(pac(val)) != null;
    }

    public static Packet pac(String str) {
        Packet p = new PacketA();
        p.write(str.getBytes(UTF8));
        return p;
    }

    /**
     * 
     * @param <X>
     * @param cret
     * @param parsers
     * @return 
     */
    <X> C<X> sub(C<X> cret, BNFParser<? extends X>... parsers) {
        for (BNFParser<? extends X> p : parsers) {
            String pname = p.getBNF().getName();
            if (pname.equals(name)) {
                cret.subs.clear();
                byte[] data = cret.ret.toByteArray();
                cret.ret.backWrite(data);
                cret.add(name, p.parse(new PacketA(data)));
            }
        }
        return cret;
    }
}
