package net.siisise.abnf.parser5234;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.ABNFor;
import net.siisise.abnf.parser.ABNFBaseParser;
import net.siisise.io.FrontPacket;

/**
 *
 */
public class Rule extends ABNFBaseParser<ABNF, ABNF> {

    public Rule(ABNF rule, ABNFReg reg, ABNFReg base) {
        super(rule, reg, base, "elements");
    }

    @Override
    public ABNF parse(FrontPacket pac) {
        inst();
        ABNF.C<Object> ret = rule.find(pac, strp(ABNF5234.rulename), strp(ABNF5234.definedAs), subs[0]);
        if (ret == null) {
            return null;
        }
        String name = (String)ret.get(ABNF5234.rulename).get(0);
        String defined = (String)ret.get(ABNF5234.definedAs).get(0);
        ABNF elements = (ABNF) ret.get(subs[0].getBNF()).get(0);

        if (defined.equals("=/")) {
            ABNF val = reg.href(name);
            if (val instanceof ABNFReg.ABNFRef) {
                throw new java.lang.UnsupportedOperationException();
            }
            if (!(val instanceof ABNFor)) {
                val = new ABNFor(name, val);
            }
            ((ABNFor) val).add(elements);
            return val;
        } else {
            return elements.name(name);
        }
    }
}
