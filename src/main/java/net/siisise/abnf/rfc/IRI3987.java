/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.siisise.abnf.rfc;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser5234.ABNF5234;

/**
 *
 * @author okome
 */
public class IRI3987 {

    static final ABNFReg REG = new ABNFReg(ABNF5234.BASE);

    // RFC3986と同一のルール
    static final ABNF pctEncoded = REG.rule("pct-encoded", ABNF.bin('%').pl(ABNF5234.HEXDIG, ABNF5234.HEXDIG));
    static final ABNF genDelims = REG.rule("gen-delims", ABNF.list(":/?#[]@")); // URIと同じ
    static final ABNF subDelims = REG.rule("sub-delims", ABNF.list("!$&'()*+,;=")); // URIと同じ
    static final ABNF reserved = REG.rule("reserved", genDelims.or(subDelims));
    static final ABNF unreserved = REG.rule("unreserved", ABNF5234.ALPHA.or(ABNF5234.DIGIT, ABNF.list("-._~")));
    static final ABNF decOctet = REG.rule("dec-octet", "DIGIT "
            + "  / %x31-39 DIGIT "
            + "  / \"1\" 2DIGIT "
            + "  / \"2\" %x30-34 DIGIT "
            + "  / \"25\" %x30-35");
    static final ABNF IPv4address = REG.rule("IPv4address", "dec-octet \".\" dec-octet \".\" dec-octet \".\" dec-octet");
    static final ABNF h16 = REG.rule("h16", ABNF5234.HEXDIG.x(1, 4));
    static final ABNF ls32 = REG.rule("ls32", "( h16 \":\" h16 ) / IPv4address");
    static final ABNF IPv6address = REG.rule("IPv6address", "6( h16 \":\" ) ls32 "
            + " /                       \"::\" 5( h16 \":\" ) ls32 "
            + " / [               h16 ] \"::\" 4( h16 \":\" ) ls32 "
            + " / [ *1( h16 \":\" ) h16 ] \"::\" 3( h16 \":\" ) ls32 "
            + " / [ *2( h16 \":\" ) h16 ] \"::\" 2( h16 \":\" ) ls32 "
            + " / [ *3( h16 \":\" ) h16 ] \"::\"    h16 \":\"   ls32 "
            + " / [ *4( h16 \":\" ) h16 ] \"::\"              ls32 "
            + " / [ *5( h16 \":\" ) h16 ] \"::\"              h16 "
            + " / [ *6( h16 \":\" ) h16 ] \"::\"");
    static final ABNF IPvFuture = REG.rule("IPvFuture", "\"v\" 1*HEXDIG \".\" 1*( unreserved / sub-delims / \":\" )");
    static final ABNF IPliteral = REG.rule("IP-literal", "\"[\" ( IPv6address / IPvFuture ) \"]\"");
    static final ABNF port = REG.rule("port", ABNF5234.DIGIT.x());
    static final ABNF scheme = REG.rule("scheme", ABNF5234.ALPHA.pl(ABNF5234.ALPHA.or(ABNF5234.DIGIT, ABNF.list("+-.")).x()));
    // ここまでURIと同じ

    static final ABNF iprivate = REG.rule("iprivate", "%xE000-F8FF / %xF0000-FFFFD / %x100000-10FFFD");
    static final ABNF ucschar = REG.rule("ucschar", "%xA0-D7FF / %xF900-FDCF / %xFDF0-FFEF"
            + " / %x10000-1FFFD / %x20000-2FFFD / %x30000-3FFFD"
            + " / %x40000-4FFFD / %x50000-5FFFD / %x60000-6FFFD"
            + " / %x70000-7FFFD / %x80000-8FFFD / %x90000-9FFFD"
            + " / %xA0000-AFFFD / %xB0000-BFFFD / %xC0000-CFFFD"
            + " / %xD0000-DFFFD / %xE1000-EFFFD");
    static final ABNF iunreserved = REG.rule("iunreserved", "ALPHA / DIGIT / \"-\" / \".\" / \"_\" / \"~\" / ucschar");
    static final ABNF ifragment = REG.rule("ifragment", "*( ipchar / \"/\" / \"?\" )");
    static final ABNF iquery = REG.rule("iquery", "*( ipchar / iprivate / \"/\" / \"?\" )");
    static final ABNF ipchar = REG.rule("ipchar", "iunreserved / pct-encoded / sub-delims / \":\" / \"@\"");
    static final ABNF isegmentNzNc = REG.rule("isegment-nz-nc", "1*( iunreserved / pct-encoded / sub-delims / \"@\" )");
    static final ABNF isegmentNz = REG.rule("isegment-nz", ipchar.ix());
    static final ABNF isegment = REG.rule("isegment", ipchar.x());
    static final ABNF ipathEmpty = REG.rule("ipath-empty", ipchar.x(0, 0));
    static final ABNF ipathRootless = REG.rule("ipath-rootless", "isegment-nz *( \"/\" isegment )");
    static final ABNF ipathNoscheme = REG.rule("ipath-noscheme", "isegment-nz-nc *( \"/\" isegment )");
    static final ABNF ipathAbsolute = REG.rule("ipath-absolute", "\"/\" [ isegment-nz *( \"/\" isegment ) ]");
    static final ABNF ipathAbempty = REG.rule("ipath-abempty", "*( \"/\" isegment )");
    static final ABNF ipath = REG.rule("ipath", ipathAbempty.or(ipathAbsolute,ipathNoscheme,ipathRootless,ipathEmpty));
    static final ABNF iregName = REG.rule("ireg-name", "*( iunreserved / pct-encoded / sub-delims )");
    static final ABNF ihost = REG.rule("ihost", IPliteral.or(IPv4address, iregName));
    static final ABNF iuserinfo = REG.rule("iuserinfo", "*( iunreserved / pct-encoded / sub-delims / \":\" )");
    static final ABNF iauthority = REG.rule("iauthority", "[ iuserinfo \"@\" ] ihost [ \":\" port ]");
    static final ABNF irelativePart = REG.rule("irelative-part", "\"//\" iauthority ipath-abempty / ipath-absolute / ipath-noscheme / ipath-empty");
    static final ABNF irelativeRef = REG.rule("irelative-ref", "irelative-part [ \"?\" iquery ] [ \"#\" ifragment ]");
    static final ABNF absoluteIRI = REG.rule("absolute-IRI", "scheme \":\" ihier-part [ \"?\" iquery ]");
    static final ABNF ihierPart = REG.rule("ihier-part", "\"//\" iauthority ipath-abempty / ipath-absolute / ipath-rootless / ipath-empty");
    public static final ABNF IRI = REG.rule("IRI", "scheme \":\" ihier-part [ \"?\" iquery ] [ \"#\" ifragment ]");
    public static final ABNF IRIreference = REG.rule("IRI-reference", IRI.or(irelativeRef));
}
