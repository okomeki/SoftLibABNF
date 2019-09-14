package net.siisise.abnf.rfc;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser5234.ABNF5234;

/**
 * 廃止されているけど作ってみる
 * @deprecated TelURI3966
 * @author okome
 */
public class TelURL2806 {
    public static ABNFReg REG = new ABNFReg(ABNF5234.REG);
    
    static final ABNF dtmfDigit = REG.rule("dtmf-digit","\"*\" / \"#\" / \"A\" / \"B\" / \"C\" / \"D\"");
    static final ABNF waitForDialTone = REG.rule("wait-for-dial-tone","\"w\"");
    static final ABNF oneSecondPause = REG.rule("one-second-pause","\"p\"");
    static final ABNF pauseCharacter = REG.rule("pause-character",oneSecondPause.or(waitForDialTone));
    static final ABNF visualSeparator = REG.rule("visual-separator","\"-\" / \".\" / \"(\" / \")\"");
    static final ABNF phonedigit = REG.rule("phonedigit", ABNF5234.DIGIT.or(visualSeparator));
    static final ABNF quotedString = REG.rule("quoted-string","%x22 *( \"\\\" CHAR / (%x20-21 / %x23-7E / %x80-FF )) %x22");
                        // Characters in URLs must follow escaping rules
                        // as explained in [RFC2396]
                        // See sections 1.2 and 2.5.11
    static final ABNF tokenChar = REG.rule("token-char","(%x21 / %x23-27 / %x2A-2B / %x2D-2E / %x30-39" +
" / %x41-5A / %x5E-7A / %x7C / %x7E)"); // ; Characters in URLs must follow escaping rules
                       // ; as explained in [RFC2396]
                       // ; See sections 1.2 and 2.5.11
    static final ABNF futureExtension = REG.rule("future-extension","\";\" 1*(token-char) [\"=\" ((1*(token-char)" +
" [\"?\" 1*(token-char)]) / quoted-string )]");// See section 2.5.11 and [RFC2543];
    static final ABNF providerHostname = REG.rule("provider-hostname","domain"); // in RFC 1035
    static final ABNF providerTag = REG.rule("provider-tag","\"tsp\"");
    static final ABNF serviceProvider = REG.rule("service-provider","\";\" provider-tag \"=\" provider-hostname");
    static final ABNF privatePrefix = REG.rule("private-prefix","(%x21-22 / %x24-27 / %x2C / %x2F / %x3A /" +
                                        " %x3C-40 / %x45-4F / %x51-56 / %x58-60 / %x65-6F / %x71-76 / %x78-7E)" +
                                        " *(%x21-3A / %x3C-7E)"); // RFC 2396 1.2 and 2.5.2
    static final ABNF localNetworkPrefix = REG.rule("local-network-prefix","1*(phonedigit / dtmf-digit / pause-character)");
    static final ABNF globalNetworkPrefix = REG.rule("global-network-prefix","\"+\" 1*phonedigit");
    static final ABNF networkPrefix = REG.rule("network-prefix",globalNetworkPrefix.or(localNetworkPrefix));
    static final ABNF phoneContextIdent = REG.rule("phone-context-ident",networkPrefix.or(privatePrefix));
    static final ABNF phoneContextTag = REG.rule("phone-context-tag",ABNF.text("phone-context"));
    static final ABNF areaSpecifier = REG.rule("area-specifier","\";\" phone-context-tag \"=\" phone-context-ident");
    static final ABNF postDial = REG.rule("post-dial","\";postd=\" 1*(phonedigit / dtmf-digit / pause-character)");
    static final ABNF isdnSubaddress = REG.rule("isdn-subaddress","\";isub=\" 1*phonedigit");
    static final ABNF localPhoneNumber = REG.rule("local-phone-number","1*(phonedigit / dtmf-digit / pause-character) [isdn-subaddress]" +
                    " [post-dial] area-specifier *(area-specifier / service-provider / future-extension)");
    static final ABNF basePhoneNumber = REG.rule("base-phone-number",phonedigit.ix());
    static final ABNF globalPhoneNumber = REG.rule("global-phone-number","\"+\" base-phone-number [isdn-subaddress] [post-dial] *(area-specifier / service-provider / future-extension)");
    static final ABNF telephoneSubscriber = REG.rule("telephone-subscriber",globalPhoneNumber.or(localPhoneNumber));
    static final ABNF telephoneScheme = REG.rule("telephone-scheme",ABNF.text("tel"));
    public static final ABNF telephoneUrl = REG.rule("telephone-url",telephoneScheme.pl(ABNF.text(':'),telephoneSubscriber));

    static final ABNF t33Subaddress = REG.rule("t33-subaddress","\";tsub=\" 1*phonedigit");
    static final ABNF faxLocalPhone = REG.rule("fax-local-phone","1*(phonedigit / dtmf-digit /" +
"                         pause-character) [isdn-subaddress]" +
"                         [t33-subaddress] [post-dial]" +
"                         area-specifier" +
"                         *(area-specifier / service-provider /" +
"                         future-extension)");
    static final ABNF faxGlobalPhone = REG.rule("fax-global-phone","\"+\" base-phone-number [isdn-subaddress]" +
"                         [t33-subaddress] [post-dial]" +
"                         *(area-specifier / service-provider /" +
"                         future-extension)");
    static final ABNF faxSubscriber = REG.rule("fax-subscriber","fax-global-phone / fax-local-phone");
    static final ABNF faxScheme = REG.rule("fax-scheme","\"fax\"");
    /**
     * @deprecated TelURI3966 では廃止
     */
    public static final ABNF faxUrl = REG.rule("fax-url","fax-scheme \":\" fax-subscriber");

    static final ABNF modemType = REG.rule("modem-type","1*(ALPHA / DIGIT / \"-\" / \"+\")");
    static final ABNF vendorName = REG.rule("vendor-name","1*(ALPHA / DIGIT / \"-\" / \"+\")");
    static final ABNF stopBits = REG.rule("stop-bits","\"1\" / \"2\"");
    static final ABNF parity = REG.rule("parity","\"n\" / \"e\" / \"o\" / \"m\" / \"s\"");
    static final ABNF dataBits = REG.rule("data-bits","\"7\" / \"8\"");
    static final ABNF acceptedModem = REG.rule("accepted-modem","\"V21\" / \"V22\" / \"V22b\" / \"V23\" / \"V26t\" / \"V32\" / " +
            "\"V32b\" / \"V34\" / \"V90\" / \"V110\" / \"V120\" / \"B103\" / \"B212\" / \"X75\" / " +
            "\"vnd.\" vendor-name \".\" modem-type");
    static final ABNF dataCapabilities = REG.rule("data-capabilities","accepted-modem [\"?\" data-bits parity stop-bits]");
    static final ABNF recommendedParams = REG.rule("recommended-params","\";rec=\" data-capabilities");
    static final ABNF modemParams= REG.rule("modem-params","\";type=\" data-capabilities");
    static final ABNF remoteHost = REG.rule("remote-host","telephone-subscriber *(modem-params / recommended-params)");
    static final ABNF modemScheme = REG.rule("modem-scheme","\"modem\"");
    /**
     * @deprecated TelURI3966 では廃止
     */
    public static final ABNF modemUrl = REG.rule("modem-url","modem-scheme \":\" remote-host");

}
