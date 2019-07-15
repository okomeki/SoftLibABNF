package net.siisise.abnf.rfc;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser5234.ABNF5234;

/**
 * https://triple-underscore.github.io/rfc-others/RFC3986-ja.html
 * @author okome
 */
public class URI3986 {

    static final ABNFReg REG = new ABNFReg(ABNF5234.BASE);

    static final ABNF pctEncoded = REG.rule("pct-encoded", ABNF.bin('%').pl(ABNF5234.HEXDIG, ABNF5234.HEXDIG));
    static final ABNF genDelims = REG.rule("gen-delims", ABNF.list(":/?#[]@"));
    static final ABNF subDelims = REG.rule("sub-delims", ABNF.list("!$&'()*+,;="));
    static final ABNF reserved = REG.rule("reserved", genDelims.or(subDelims));
    static final ABNF unreserved = REG.rule("unreserved", ABNF5234.ALPHA.or(ABNF5234.DIGIT, ABNF.list("-._~")));

    static final ABNF scheme = REG.rule("scheme", ABNF5234.ALPHA.pl(ABNF5234.ALPHA.or(ABNF5234.DIGIT, ABNF.list("+-.")).x()));
    static final ABNF userinfo = REG.rule("userinfo", unreserved.or(pctEncoded, subDelims, ABNF.bin(':')).x());
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
    static final ABNF IPliteral = REG.rule("IP-literal", "\"[\" ( IPv6address / IPvFuture ) \"]\"");
    static final ABNF IPvFuture = REG.rule("IPvFuture", "\"v\" 1*HEXDIG \".\" 1*( unreserved / sub-delims / \":\" )");
    static final ABNF regName = REG.rule("reg-name", unreserved.or(pctEncoded, subDelims).x());
    static final ABNF host = REG.rule("host", IPliteral.or(IPv4address, regName));
    static final ABNF port = REG.rule("port", ABNF5234.DIGIT.x());
    static final ABNF authority = REG.rule("authority", "[ userinfo \"@\" ] host [ \":\" port ]");
    static final ABNF pchar = REG.rule("pchar", "unreserved / pct-encoded / sub-delims / \":\" / \"@\"");
    static final ABNF segment = REG.rule("segment", pchar.x());
    static final ABNF segmentNz = REG.rule("segment-nz", pchar.ix());
    static final ABNF segmentNzNc = REG.rule("segment-nz-nc", "1*( unreserved / pct-encoded / sub-delims / \"@\" )");
    static final ABNF pathAbempty = REG.rule("path-abempty", "*( \"/\" segment )");
    static final ABNF pathAbsolute = REG.rule("path-absolute", "\"/\" [ segment-nz *( \"/\" segment ) ]");
    static final ABNF pathNoscheme = REG.rule("path-noscheme", "segment-nz-nc *( \"/\" segment )");
    static final ABNF pathRootless = REG.rule("path-rootless", "segment-nz *( \"/\" segment )");
    static final ABNF pathEmpty = REG.rule("path-empty", pchar.x(0, 0));
    static final ABNF hierPart = REG.rule("hier-part", "\"//\" authority path-abempty / path-absolute / path-rootless / path-empty");
    static final ABNF path = REG.rule("path", pathAbempty.or(pathAbsolute, pathNoscheme, pathRootless, pathEmpty));
    static final ABNF query = REG.rule("query", "*( pchar / \"/\" / \"?\" )");
    static final ABNF fragment = REG.rule("fragment", "*( pchar / \"/\" / \"?\" )");
    static final ABNF URI = REG.rule("URI", "scheme \":\" hier-part [ \"?\" query ] [ \"#\" fragment ]");

    static final ABNF relativePart = REG.rule("relative-part", "\"//\" authority path-abempty / path-absolute"
            + " / path-noscheme / path-empty");
    static final ABNF relativeRef = REG.rule("relative-ref", "relative-part [ \"?\" query ] [ \"#\" fragment ]");
    static final ABNF URIreference = REG.rule("URI-reference", URI.or(relativeRef));
    static final ABNF absoluteURI = REG.rule("absolute-URI", "scheme \":\" hier-part [ \"?\" query ]");

    public static void main(String[] argv) {
        System.out.println(URIreference.eq("http://siisise.net/uss"));
    }
}
