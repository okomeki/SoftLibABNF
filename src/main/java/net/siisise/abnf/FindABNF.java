package net.siisise.abnf;

import net.siisise.io.FrontPacket;
import net.siisise.io.Packet;

/**
 * findの分離
 * サブ要素を持つ方
 */
public abstract class FindABNF extends AbstractABNF {

    @Override
    public Packet is(FrontPacket src) {
        C ret = find(src);
        if (ret == null) {
            return null;
        }
        return ret.ret;
    }
}
