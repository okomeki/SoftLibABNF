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
package net.siisise.abnf.parser7405;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFCC;
import net.siisise.abnf.parser5234.ABNF5234;
import net.siisise.abnf.parser5234.Element;
import net.siisise.bnf.parser7405.QuotedString;

/**
 * RFC 7405 Case-Sensitive String Support in ABNF
 *
 * https://tools.ietf.org/html/rfc7405
 * 差分だけでどうにかしたい
 */
public class ABNF7405 {

    public static final ABNFCC REG = new ABNFCC(ABNF5234.copyREG());

    static final ABNF quotedString = REG.rule("quoted-string", QuotedString.class, ABNF5234.DQUOTE.pl(ABNF.range(0x20, 0x21).or1(ABNF.range(0x23, 0x7e)).x(), ABNF5234.DQUOTE));
    static final ABNF caseInsensitiveString = REG.rule("case-insensitive-string", CaseInsensitiveString.class, ABNF.text("%i").c().pl(quotedString));
    static final ABNF caseSensitiveString = REG.rule("case-sensitive-string", CaseSensitiveString.class, ABNF.text("%s").pl(quotedString));
    static final ABNF charVal = REG.rule("char-val", CharVal7405.class, caseInsensitiveString.or1(caseSensitiveString));

    // ここだけで差し替え
    static final ABNF element = REG.rule("element", Element.class, REG.ref("rulename").or1(REG.ref("group"),
            REG.ref("option"), REG.ref("char-val"), REG.ref("num-val"), REG.ref("prose-val")));
}
