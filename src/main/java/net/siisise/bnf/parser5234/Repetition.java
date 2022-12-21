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

import java.util.List;
import net.siisise.bnf.BNF;
import net.siisise.bnf.BNFReg;
import net.siisise.bnf.parser.BNFBuildParser;

/**
 * リピートはorに展開せよ?
 */
public class Repetition extends BNFBuildParser<BNF, Object> {

    /**
     * abnfの他、ABNF5234のrepeat も参照する
     *
     * @param rule ルールABNF
     * @param base ABNF名前空間Reg
     */
    public Repetition(BNF rule, BNFReg base) {
        super(rule, base, "repeat", "element");
    }

    /**
     * ABNFでパースしたあれをこうする
     * @param ret ABNF解析済みの適度なデータ
     * @return リピートなABNF
     */
    @Override
    protected BNF build(BNF.Match<Object> ret) {
        List<Object> rep = ret.get("repeat");
        BNF ele = (BNF) ret.get("element").get(0);

        if (rep != null) {
            return repeat((String)rep.get(0), ele);
        }
        return ele;
    }

    private BNF repeat(String rep, BNF element) {
        if (rep.contains("*")) {
            int off = rep.indexOf("*");
            String l = rep.substring(0, off);
            String r = rep.substring(off + 1);
            int min = l.isEmpty() ? 0 : Integer.parseInt(l);
            int max = r.isEmpty() ? -1 : Integer.parseInt(r);
            return element.x(min, max);
        } else {
            int r = Integer.parseInt(rep);
            return element.x(r, r);
        }
    }

}
