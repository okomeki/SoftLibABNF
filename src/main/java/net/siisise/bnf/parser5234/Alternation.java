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
import net.siisise.bnf.BNFbin;
import net.siisise.bnf.BNFmap;
import net.siisise.bnf.BNFor;
import net.siisise.bnf.parser.BNFList;

/**
 * or 相当.
 * 全解釈を解決するために最長一致を採用しておく.
 * アナログな解釈と異なる場合もあるかもしれない
 */
public class Alternation extends BNFList<BNF, BNF> {

    public Alternation(BNF rule, BNFReg base) {
        super(rule, base, "concatenation");
    }

    @Override
    protected BNF build(List<BNF> list) {
        if (list.size() == 1) {
            return list.get(0);
        }
    //    ABNF map = new ABNFmap();
    //    return map.or(list.toArray(new BNF[list.size()]));
    
        shortMap(list);
        return new BNFor(list.toArray(new BNF[list.size()]));
    }

    private void shortMap(List<BNF> list) {
        for ( int i = 0; i < list.size() - 1; i++ ) {
            BNF bnf1 = list.get(i);
            if ( bnf1 instanceof BNFbin ) {
                int ch1 = ((BNFbin)bnf1).ch();
                if ( ch1 < 0 ) {
                    break;
                }
                int chn = ch1;
                int j = i;
                while ( (j + 1 < list.size()) ) {
                    BNF bnf2 = list.get(j+1);
                    if ( !(bnf2 instanceof BNFbin) ) {
                        break;
                    }
                    int ch2 = ((BNFbin)bnf2).ch();
                    if ( chn+1 != ch2 ) {
                        break;
                    }
                    chn++;
                    j++;
                }
                if ( ch1 + 5 < chn ) { // 何文字以上かは判定速度で決めたい
                    BNFmap map = new BNFmap();
                    while ( i <= j ) {
                        BNFbin b = (BNFbin)list.remove(i);
                        map = (BNFmap) map.or(b);
                        j--;
                    }
                    list.add(i, map);
                } else if ( ch1 < chn ) {
                    i+= chn - ch1;
                }
            }
        }
    }
}
