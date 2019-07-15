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

    /**
     * 
     * @param reg ABNF Parserの定義は不要のはず
     */
    public Rule(ABNFReg reg) {
        super(ABNF5234.rule, reg, Elements.class);
    }

    public Rule(ABNFReg reg, ABNFReg base) {
        super(ABNF5234.rule, reg, base, "elements");
    }

    @Override
    public ABNF parse(Packet pac) {
        inst();
        ABNF.C<Object> ret = def.findx(pac, x(ABNF5234.rulename), x(ABNF5234.definedAs), subs[0]);
        if (ret == null) {
            return null;
        }
        String name = str((Packet) ret.get(ABNF5234.rulename).get(0));
        String defined = str((Packet) ret.get(ABNF5234.definedAs).get(0));
        ABNF f = (ABNF) ret.get(subs[0].getBNF()).get(0);

        if (defined.equals("=/")) {
            ABNF val = reg.ref(name);
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
