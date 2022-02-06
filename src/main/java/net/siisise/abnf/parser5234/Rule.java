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
package net.siisise.abnf.parser5234;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.ABNFor;
import net.siisise.bnf.BNF;
import net.siisise.bnf.BNFReg;
import net.siisise.bnf.parser.BNFBuildParser;

public class Rule extends BNFBuildParser<ABNF, Object> {

    /**
     * 
     * @param rule ルールABNF
     * @param base ABNF名前空間Reg
     */
    public Rule(BNF rule, BNFReg base) {
        super(rule, base, "rulename", "defined-as", "elements");
    }

    /**
     * 解析組み立て
     * @param ret 解析ABNFパーツ
     * @param ns user name space 名前空間(ユーザ定義側)
     * @return 解析結果
     */
    @Override
    protected ABNF build(BNF.C<Object> ret, Object ns) {
        String rulename = ((ABNF) ret.get("rulename").get(0)).getName();
        String defined = (String) ret.get("defined-as").get(0);
        ABNF elements = (ABNF) ret.get("elements").get(0);

        if (defined.equals("=/")) {
            ABNF rule = ((ABNFReg)ns).href(rulename);
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
