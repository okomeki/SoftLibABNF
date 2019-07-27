package net.siisise.abnf.rfc;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser.ABNFSelect;

/**
 *
 * @author okome
 */
public class HTTP7230RepetitionSelect extends ABNFSelect {
    
    public HTTP7230RepetitionSelect(ABNF abnf,ABNFReg reg, ABNFReg base) {
        super(abnf,reg,base,"httprepetition","orgrepetition");
    }
    
}
