package net.siisise.abnf.parser5234;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.ABNFor;
import net.siisise.abnf.parser.ABNFBuildParser;
import net.siisise.io.FrontPacket;

public class Rule extends ABNFBuildParser<ABNF, Object> {

    public Rule(ABNF rule, ABNFReg reg, ABNFReg base) {
        super(rule, reg, base, "rulename", "defined-as", "elements");
    }

    @Override
    protected ABNF build(ABNF.C<Object> ret) {
        String rulename = ((ABNF) ret.get("rulename").get(0)).getName();
        String defined = str((FrontPacket) ret.get("defined-as").get(0));
        ABNF elements = (ABNF) ret.get("elements").get(0);

        if (defined.equals("=/")) {
            ABNF rule = ((ABNFReg) reg).href(rulename);
            if (rule instanceof ABNFReg.ABNFRef) {
                throw new java.lang.UnsupportedOperationException();
            }
            if (!(rule instanceof ABNFor)) {
                rule = new ABNFor(rulename, rule);
            }
            ((ABNFor) rule).add(elements);
            return rule;
        } else {
            return elements.name(rulename);
        }
    }
}
