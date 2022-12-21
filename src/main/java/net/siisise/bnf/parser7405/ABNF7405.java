/*
 * Copyright 2021 Siisise Net.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.siisise.bnf.parser7405;

import net.siisise.abnf.parser5234.Element;
import net.siisise.bnf.BNF;
import net.siisise.bnf.BNFCC;
import net.siisise.bnf.parser5234.ABNF5234;

/**
 * RFC 7405
 * https://tools.ietf.org/html/rfc7405
 * 差分だけでどうにかしたい
 */
public class ABNF7405 {

    public static final BNFCC<BNF> REG = BNFCC.abnf2bnf(ABNF5234.copyREG());

    static final BNF DQUOTE = ABNF5234.DQUOTE;

    static final BNF quotedString = REG.rule("quoted-string", QuotedString.class, DQUOTE.pl(BNF.range(0x20,0x21).or1(BNF.range(0x23,0x7e)).x(),DQUOTE));
    static final BNF caseInsensitiveString = REG.rule("case-insensitive-string", CaseInsensitiveString.class, BNF.text("%i").c().pl(quotedString));
    static final BNF caseSensitiveString = REG.rule("case-sensitive-string", CaseSensitiveString.class, BNF.text("%s").pl(quotedString));
    static final BNF charVal = REG.rule("char-val", CharVal7405.class, caseInsensitiveString.or1(caseSensitiveString));

    // ここだけで差し替え
    static final BNF element = REG.rule("element", Element.class, REG.ref("rulename").or(REG.ref("group"),
            REG.ref("option"), REG.ref("char-val"), REG.ref("num-val"), REG.ref("prose-val")));
}
