package net.siisise.abnf.rfc;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser5234.ABNF5234;

/**
 * まだない
 * @author okome
 */
public class URN8141 {
    static ABNFReg REG = new ABNFReg();
    
    static ABNF alphanum = REG.rule("alphanum",ABNF5234.ALPHA.or(ABNF5234.DIGIT));
    static ABNF fragment = REG.rule("fragment",URI3986.fragment);
    static ABNF pchar = REG.rule("pchar",URI3986.pchar);
    
    static ABNF DigitNonZero = REG.rule("DigitNonZero","\"1\" / \"2\" / \"3\" / \"4\" / \"5\" / \"6\" / \"7\" / \"8\" / \"9\"");
    static ABNF Digit = REG.rule("Digit","\"0\" / DigitNonZero");
    static ABNF Number = REG.rule("Number","DigitNonZero 0*Digit");
    static ABNF InformalNamespaceName = REG.rule("InformalNamespaceName","\"urn-\" Number");
    
    static ABNF fComponent = REG.rule("f-component","fragment");
    static ABNF qComponent = REG.rule("q-component","pchar *( pchar / \"/\" / \"?\" )");
    static ABNF rComponent = REG.rule("r-component","pchar *( pchar / \"/\" / \"?\" )");
    static ABNF rqComponents = REG.rule("rq-components","[ \"?+\" r-component ] [ \"?=\" q-component ]");
    static ABNF NSS = REG.rule("NSS","pchar *(pchar / \"/\")");
    static ABNF ldh = REG.rule("ldh","alphanum / \"-\"");
    static ABNF NID = REG.rule("NID","(alphanum) 0*30(ldh) (alphanum)"); // 難しい
    static ABNF assignedName = REG.rule("assigned-name","\"urn\" \":\" NID \":\" NSS");
    static ABNF namestring = REG.rule("namestring","assigned-name [ rq-components ] [ \"#\" f-component ]");
}
