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
package net.siisise.bnf.parser5234;

import net.siisise.abnf.parser7405.*;
import net.siisise.abnf.parser5234.Element;
import net.siisise.bnf.BNF;
import net.siisise.bnf.BNFCC;

/**
 *
 * https://tools.ietf.org/html/rfc7405
 * 差分だけでどうにかしたい
 *
 */
public class ABNF7405 {

    public static final BNFCC<BNF> REG = BNFCC.abnf(ABNF5234.copyREG());

    static final BNF caseInsensitiveString = REG.rule("case-insensitive-string", IS.class, "[ \"%i\" ] quoted-string");
    static final BNF caseSensitiveString = REG.rule("case-sensitive-string", SS.class, "\"%s\" quoted-string");
    static final BNF charVal = REG.rule("char-val", CharVal7405.class, caseInsensitiveString.or(caseSensitiveString));
    static final BNF quotedString = REG.rule("quoted-string", QS.class, "DQUOTE *(%x20-21 / %x23-7E) DQUOTE");

    // ここだけで差し替え
    static final BNF element = REG.rule("element", Element.class, REG.ref("rulename").or(REG.ref("group"),
            REG.ref("option"), REG.ref("char-val"), REG.ref("num-val"), REG.ref("prose-val")));
}
