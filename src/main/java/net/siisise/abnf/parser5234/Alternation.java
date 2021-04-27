package net.siisise.abnf.parser5234;

import java.util.List;
import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.ABNFbin;
import net.siisise.abnf.ABNFmap;
import net.siisise.abnf.ABNFor;
import net.siisise.abnf.parser.ABNFList;

/**
 * or 相当.
 * 先頭一致なのでアナログな解釈と異なる場合もあるかもしれない
 */
public class Alternation extends ABNFList<ABNF, ABNF> {

    public Alternation(ABNF rule, ABNFReg reg, ABNFReg base) {
        super(rule, reg, base, "concatenation");
    }

    @Override
    protected ABNF build(List<ABNF> list) {
        if (list.size() == 1) {
            return list.get(0);
        }
        shortMap(list);
        return new ABNFor(list.toArray(new ABNF[list.size()]));
    }

    private void shortMap(List<ABNF> list) {
        for ( int i = 0; i < list.size() - 1; i++ ) {
            ABNF bnf1 = list.get(i);
            if ( bnf1 instanceof ABNFbin ) {
                int ch1 = ((ABNFbin)bnf1).ch();
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
