package net.siisise.abnf.parser;

import net.siisise.abnf.ABNF;
import net.siisise.io.FrontPacket;
import net.siisise.io.Packet;

/**
 * Packetに分割するだけ。
 * ABNFで指定の対象クラスには変換しない。
 * みかんせい?
 */
public class ABNFPacketParser extends ABNFBaseParser<FrontPacket, ABNF> {

    public ABNFPacketParser(ABNF rule) {
        super(rule);
    }

    /**
     * find で使うだけなので判定していない
     * @param pac
     * @return 
     */
    @Override
    public FrontPacket parse(FrontPacket pac) {
        Packet r = rule.is(pac);
        if ( r == null ) {
            return null;
        }
        return r;
    }

}
