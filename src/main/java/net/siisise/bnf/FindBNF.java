package net.siisise.bnf;

import net.siisise.io.FrontPacket;
import net.siisise.io.Packet;

/**
 *
 */
public abstract class FindBNF extends AbstractBNF {

    @Override
    public Packet is(FrontPacket src) {
        C ret = find(src);
        if (ret == null) {
            return null;
        }
        return ret.ret;
    }
}
