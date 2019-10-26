package net.siisise.abnf.parser5234;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.ABNFor;
import net.siisise.abnf.parser.ABNFBaseParser;
import net.siisise.io.Packet;

/**
 *
 * @author okome
 */
public class Rule extends ABNFBaseParser<ABNF, ABNF> {

    public Rule(ABNF abnf, ABNFReg reg, ABNFReg base) {
        super(abnf, reg, base, "elements");
    }

    @Override
    public ABNF parse(Packet pac) {
        inst();
        ABNF.C<Object> ret = def.find(pac, x(ABNF5234.rulename), x(ABNF5234.definedAs), subs[0]);
        if (ret == null) {
            return null;
        }
        String name = str((Packet) ret.get(ABNF5234.rulename).get(0));
        String defined = str((Packet) ret.get(ABNF5234.definedAs).get(0));
        ABNF f = (ABNF) ret.get(subs[0].getBNF()).get(0);

        if (defined.equals("=/")) {
            ABNF val = reg.href(name);
            if (val instanceof ABNFReg.ABNFRef) {
                throw new java.lang.UnsupportedOperationException();
            }
            if (!(val instanceof ABNFor)) {
                val = new ABNFor(name, val);
            }
            ((ABNFor) val).add(f);
            return val;
        } else {
            return f.name(name);
        }
    }
}
