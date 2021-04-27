package net.siisise.abnf.parser7405;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser5234.ABNF5234;
import net.siisise.abnf.parser5234.Element;

/**
 *
 * https://tools.ietf.org/html/rfc7405
 * 差分だけでどうにかしたい
 *
 */
public class ABNF7405 {

    public static final ABNFReg REG = new ABNFReg(ABNF5234.copyREG());

    static final ABNF caseInsensitiveString = REG.rule("case-insensitive-string", "[ \"%i\" ] quoted-string");
    static final ABNF caseSensitiveString = REG.rule("case-sensitive-string", "\"%s\" quoted-string");
    static final ABNF charVal = REG.rule("char-val", CharVal7405.class, caseInsensitiveString.or(caseSensitiveString));
    static final ABNF quotedString = REG.rule("quoted-string", QS.class, "DQUOTE *(%x20-21 / %x23-7E) DQUOTE");

    // ここだけで差し替え
    static final ABNF element = REG.rule("element", Element.class, REG.ref("rulename").or(REG.ref("group"),
            REG.ref("option"), REG.ref("char-val"), REG.ref("num-val"), REG.ref("prose-val")));
}
