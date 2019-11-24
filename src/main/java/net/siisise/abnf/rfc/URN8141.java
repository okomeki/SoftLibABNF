package net.siisise.abnf.rfc;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser5234.ABNF5234;

/**
 * Uniform Resource Name.
 * RFC 1737 Functional Requirements for Uniform Resource Names
 * RFC 2141 URN Syntax  8141により廃止
 * RFC 3406 Uniform Resource Names (URN) Namespace Definition Mechanisms 8141により廃止
 * RFC 3986 URI
 * RFC 8141 Uniform Resource Names (URNs)
 * @author okome
 */
public class URN8141 {
    static ABNFReg REG = new ABNFReg();
    
    static ABNF alphanum = REG.rule("alphanum",ABNF5234.ALPHA.or(ABNF5234.DIGIT));
    static ABNF fragment = REG.rule("fragment",URI3986.fragment);
    static ABNF pchar = REG.rule("pchar",URI3986.pchar);
    
    static ABNF DigitNonZero = REG.rule("DigitNonZero",ABNF.range('1','9'));
    static ABNF Digit = REG.rule("Digit","\"0\" / DigitNonZero");
    static ABNF Number = REG.rule("Number","DigitNonZero 0*Digit");
    public static ABNF InformalNamespaceName = REG.rule("InformalNamespaceName","\"urn-\" Number");
    
    static ABNF fComponent = REG.rule("f-component",fragment);
    static ABNF qComponent = REG.rule("q-component","pchar *( pchar / \"/\" / \"?\" )");
    static ABNF rComponent = REG.rule("r-component","pchar *( pchar / \"/\" / \"?\" )");
    static ABNF rqComponents = REG.rule("rq-components","[ \"?+\" r-component ] [ \"?=\" q-component ]");
    /** 名前空間固有文字列 (Namespace Specific String) */
    static ABNF NSS = REG.rule("NSS","pchar *(pchar / \"/\")");
    static ABNF ldh = REG.rule("ldh",alphanum.or(ABNF.bin('-')));
    /** 名前空間識別子 (Namespace Identifier) */
    static ABNF NID = REG.rule("NID","(alphanum) 0*30(ldh) (alphanum)");
    static ABNF assignedName = REG.rule("assigned-name","\"urn\" \":\" NID \":\" NSS");
    public static ABNF namestring = REG.rule("namestring","assigned-name [ rq-components ] [ \"#\" f-component ]");
}
