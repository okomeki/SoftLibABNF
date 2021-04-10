package net.siisise.abnf.parser;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.io.FrontPacket;

/**
 * Packetに分割するだけ。
 * ABNFで指定の対象クラスには変換しない。
 * みかんせい?
 */
public class ABNFPacketParser extends ABNFBaseParser<FrontPacket, ABNF> {

    public ABNFPacketParser(ABNF def, ABNFReg reg) {
        super(def, reg);
    }

    @Override
    public FrontPacket parse(FrontPacket pac) {
        return pac;
    }

}
