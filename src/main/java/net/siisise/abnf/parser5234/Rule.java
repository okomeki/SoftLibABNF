package net.siisise.abnf.parser5234;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.ABNFor;
import net.siisise.abnf.parser.ABNFBaseParser;
import net.siisise.io.FrontPacket;

/**
 *
 */
public class Rule extends ABNFBaseParser<ABNF, Object> {

    public Rule(ABNF rule, ABNFReg reg, ABNFReg base) {
        super(rule, reg, base, "rulename", "elements");
    }

    @Override
    public ABNF parse(FrontPacket pac) {
        inst();
        ABNF.C<Object> ret = rule.find(pac, subs[0], strp(ABNF5234.definedAs), subs[1]);
        if (ret == null) {
            return null;
        }
        String name = ((ABNF) ret.get(subs[0].getBNF()).get(0)).getName();
        String defined = (String) ret.get(ABNF5234.definedAs).get(0);
        ABNF elements = (ABNF) ret.get(subs[1].getBNF()).get(0);

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
