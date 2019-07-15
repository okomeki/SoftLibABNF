package net.siisise.abnf.rfc;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;

/**
 * JavaのURI相当
 * @author okome
 */
public class URI2732 {
    static final ABNFReg REG = new ABNFReg(URI2396.REG);
    
    static final ABNF IPv6address = REG.rule("IPv6address",IPv62373.IPv6address);
    
    static final ABNF IPv6reference = REG.rule("ipv6reference","\"[\" IPv6address \"]\"");
    static final ABNF host = REG.rule("host","hostname | IPv4address | IPv6reference");
    static final ABNF reserved = REG.rule("reserved",ABNF.list(";/?:@&=+$,[]"));
    static final ABNF unwise = REG.rule("unwise",ABNF.list("{}|\\^`"));
 
//    public static void main(String[] argv) {
//        System.out.println(URIreference.eq("http://user@siisise.net/a/b/c"));
//    }
}
