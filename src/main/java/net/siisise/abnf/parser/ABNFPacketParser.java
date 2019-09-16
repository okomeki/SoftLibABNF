package net.siisise.abnf.parser;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.io.Packet;

/**
 * みかんせい?
 * @author okome
 */
public class ABNFPacketParser extends ABNFBaseParser<Packet, ABNF> {

    public ABNFPacketParser(ABNF def, ABNFReg reg) {
        super(def, reg);
    }

    @Override
    public Packet parse(Packet pac) {
        return pac;
    }

}
