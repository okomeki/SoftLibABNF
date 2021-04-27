package net.siisise.abnf.parser5234;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser.ABNFBaseParser;
import net.siisise.io.FrontPacket;

/**
 *
 */
public class Rulename extends ABNFBaseParser<ABNF, Object> {

    public Rulename(ABNF rule, ABNFReg reg, ABNFReg base) {
        super(rule, reg, base);
    }

    @Override
    public ABNF parse(FrontPacket pac) {
        FrontPacket name = rule.is(pac);
        if (name == null) {
            return null;
        }
        return ((ABNFReg)reg).ref(str(name));
    }

}
