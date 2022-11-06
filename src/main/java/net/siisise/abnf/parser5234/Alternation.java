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

import java.util.List;
import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFbin;
import net.siisise.abnf.ABNFmap;
import net.siisise.abnf.ABNFor;
import net.siisise.bnf.BNF;
import net.siisise.bnf.BNFReg;
import net.siisise.bnf.parser.BNFList;

/**
 * or 相当.
 * 先頭一致なのでアナログな解釈と異なる場合もあるかもしれない
 */
public class Alternation extends BNFList<ABNF, ABNF> {

    public Alternation(BNF rule, BNFReg base) {
        super(rule, base, "concatenation");
    }

    @Override
    protected ABNF build(List<ABNF> list) {
        if (list.size() == 1) {
            return list.get(0);
        }
        shortMap(list);
        return new ABNFor(list.toArray(new ABNF[list.size()]));
    }

    /**
     * まとめて速くなればいいなくらいの処理.
     * @param list 
     */
    private void shortMap(List<ABNF> list) {
        for ( int i = 0; i < list.size() - 1; i++ ) {
            ABNF bnf1 = list.get(i);
            if ( bnf1 instanceof ABNFbin ) {
                int ch1 = ((ABNFbin)bnf1).ch();
                if ( ch1 < 0 ) {
                    break;
                }
                int chn = ch1;
                int j = i;
                while ( (j + 1 < list.size()) ) {
                    ABNF bnf2 = list.get(j+1);
                    if ( !(bnf2 instanceof ABNFbin) ) {
                        break;
                    }
                    int ch2 = ((ABNFbin)bnf2).ch();
                    if ( chn+1 != ch2 ) {
                        break;
                    }
                    chn++;
                    j++;
                }
                if ( ch1 + 5 < chn ) { // 何文字以上かは判定速度で決めたい
                    ABNFmap map = new ABNFmap();
                    while ( i <= j ) {
                        ABNFbin b = (ABNFbin)list.remove(i);
                        map = (ABNFmap) map.or(b);
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
