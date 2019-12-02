package net.siisise.abnf.rfc;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.parser5234.ABNF5234;

/**
 * RFC 5322 Internet Message Format.
 * 動作未確認
 * REG obs省略版
 * OBS 全機能版
 */
public class IMF5322 {

    /**
     * obsを省いた定義
     */
    public static ABNFReg REG = new ABNFReg(ABNF5234.BASE);

    // 3.2.1.
//    static ABNF quotedPair = REG.rule("quoted-pair", ABNF.bin('\\').pl(ABNF5234.VCHAR.or(ABNF5234.WSP)));
    static ABNF quotedPair = REG.rule("quoted-pair", "(\"\\\" (VCHAR / WSP)) / obs-qp");
    // 3.2.2.
    static ABNF FWS = REG.rule("FWS", "([*WSP CRLF] 1*WSP) / obs-FWS");
    static ABNF ctext = REG.rule("ctext", "%d33-39 / %d42-91 / %d93-126 / obs-ctext");
    static ABNF comment = REG.rule("comment", "\"(\" *([FWS] ccontent) [FWS] \")\"");
    static ABNF ccontent = REG.rule("ccontent", ctext.or(quotedPair, comment));
    static ABNF CFWS = REG.rule("CFWS", "(1*([FWS] comment) [FWS]) / FWS");
    // 3.2.3. Atom
    static ABNF atext = REG.rule("atext", ABNF5234.ALPHA.or(ABNF5234.DIGIT, ABNF.list("!#$%&'*+-/=?^_`{|}~")));
    static ABNF atom = REG.rule("atom", CFWS.c().pl(atext.ix(),CFWS.c()));
    static ABNF dotAtomText = REG.rule("dot-atom-text", "1*atext *(\".\" 1*atext)");
    static ABNF dotAtom = REG.rule("dot-atom", "[CFWS] dot-atom-text [CFWS]");
    static ABNF specials = REG.rule("specials", ABNF.list("()<>[]:;@\\,.").or(ABNF5234.DQUOTE));

    // 3.2.4. Quoted Strings
//    static ABNF qtext = REG.rule("qtext", "%d33 / %d35-91 / %d93-126");
    static ABNF qtext = REG.rule("qtext", "%d33 / %d35-91 / %d93-126 / obs-qtext");
    static ABNF qcontent = REG.rule("qcontent", qtext.or(quotedPair));
    static ABNF quotedString = REG.rule("quoted-string", "[CFWS] DQUOTE *([FWS] qcontent) [FWS] DQUOTE [CFWS]");

    // 4.1. Miscellaneous Obsolete Tokens
    static ABNF obsNoWsCtl = REG.rule("obs-NO-WS-CTL", "%d1-8 / %d11 / %d12 / %d14-31 / %d127");
    static ABNF obsCtext = REG.rule("obs-ctext", obsNoWsCtl);
    static ABNF obsQtext = REG.rule("obs-qtext", obsNoWsCtl);
    static ABNF obsUtext = REG.rule("obs-utext", ABNF.bin(0x0).or(obsNoWsCtl, ABNF5234.VCHAR));
    static ABNF obsQp = REG.rule("obs-qp", "\"\\\" (%d0 / obs-NO-WS-CTL / LF / CR)");
    static ABNF obsBody = REG.rule("obs-body", "*((*LF *CR *((%d0 / text) *LF *CR)) / CRLF)");
//    static ABNF obsUnstruct = OBS.rule("obs-unstruct", "*((*LF *CR *(obs-utext *LF *CR)) / FWS)");
    static ABNF obsUnstruct = REG.rule("obs-unstruct", "*( (*CR 1*(obs-utext / FWS)) / 1*LF ) *CR"); // Errata ID: 1905
    static ABNF obsPhrase = REG.rule("obs-phrase", "word *(word / \".\" / CFWS)");
    static ABNF obsPhraseList = REG.rule("obs-phrase-list", "[phrase / CFWS] *(\",\" [phrase / CFWS])");

    // 3.2.5. Miscellaneous Tokens
    static ABNF word = REG.rule("word", atom.or(quotedString));
//    static ABNF phrase = REG.rule("phrase", word.ix());
    static ABNF phrase = REG.rule("phrase", word.ix().or(obsPhrase));
//    static ABNF unstructured = REG.rule("unstructured", "(*([FWS] VCHAR) *WSP)");
    static ABNF unstructured = REG.rule("unstructured", "(*([FWS] VCHAR) *WSP) / obs-unstruct");

    // 3.3. Date and Time Specification
    static ABNF dayName = REG.rule("day-name", "\"Mon\" / \"Tue\" / \"Wed\" / \"Thu\" / \"Fri\" / \"Sat\" / \"Sun\"");
//    static ABNF dayOfWeek = REG.rule("day-of-week", "([FWS] day-name)");
    static ABNF dayOfWeek = REG.rule("day-of-week", "([FWS] day-name) / obs-day-of-week");
//    static ABNF day = REG.rule("day", "([FWS] 1*2DIGIT FWS)");
    static ABNF day = REG.rule("day", "([FWS] 1*2DIGIT FWS) / obs-day");
    static ABNF date = REG.rule("date", "day month year");
    static ABNF dateTime = REG.rule("date-time", "[ day-of-week \",\" ] date time [CFWS]");
    static ABNF month = REG.rule("month", "\"Jan\" / \"Feb\" / \"Mar\" / \"Apr\" / "
            + "\"May\" / \"Jun\" / \"Jul\" / \"Aug\" / \"Sep\" / \"Oct\" / \"Nov\" / \"Dec\"");
    static ABNF year = REG.rule("year", "(FWS 4*DIGIT FWS) / obs-year");
    static ABNF hour = REG.rule("hour", "2DIGIT / obs-hour");
    static ABNF minute = REG.rule("minute", "2DIGIT / obs-minute");
    static ABNF second = REG.rule("second", "2DIGIT / obs-second");
    static ABNF timeOfDay = REG.rule("time-of-day", "hour \":\" minute [ \":\" second ]");
    static ABNF zone = REG.rule("zone", "(FWS ( \"+\" / \"-\" ) 4DIGIT) / obs-zone");
    static ABNF time = REG.rule("time", "time-of-day zone");

    // 4.4. Obsolete Addressing
    static ABNF obsAngleAddr = REG.rule("obs-angle-addr", "[CFWS] \"<\" obs-route addr-spec \">\" [CFWS]");
    static ABNF obsRoute = REG.rule("obs-route", "obs-domain-list \":\"");
    static ABNF obsDomainList = REG.rule("obs-domain-list", "*(CFWS / \",\") \"@\" domain *(\",\" [CFWS] [\"@\" domain])");
    static ABNF obsMboxList = REG.rule("obs-mbox-list", "*([CFWS] \",\") mailbox *(\",\" [mailbox / CFWS])");
    static ABNF obsAddrList = REG.rule("obs-addr-list", "*([CFWS] \",\") address *(\",\" [address / CFWS])");
    static ABNF obsGroupList = REG.rule("obs-group-list", "1*([CFWS] \",\") [CFWS]");
    static ABNF obsLocalPart = REG.rule("obs-local-part", "word *(\".\" word)");
    static ABNF obsDomain = REG.rule("obs-domain", "atom *(\".\" atom)");
    static ABNF obsDtext = REG.rule("obs-dtext", obsNoWsCtl.or(quotedPair));

    // 3.4.1. Addr-Spac Specification
//    static ABNF dtext = REG.rule("dtext", "%d33-90 / %d94-126");
    static ABNF dtext = REG.rule("dtext", "%d33-90 / %d94-126 / obs-dtext");
    static ABNF domainLiteral = REG.rule("domain-literal", "[CFWS] \"[\" *([FWS] dtext) [FWS] \"]\" [CFWS]");
    static ABNF domain = REG.rule("domain", dotAtom.or(domainLiteral, obsDomain));
    public static ABNF localPart = REG.rule("local-part", dotAtom.or(quotedString, obsLocalPart));
    public static ABNF addrSpec = REG.rule("addr-spec", "local-part \"@\" domain");

    // 3.4. Address Specification
    static ABNF angleAddr = REG.rule("angle-addr", "[CFWS] \"<\" addr-spec \">\" [CFWS] / obs-angle-addr");
    static ABNF displayName = REG.rule("display-name", phrase);
    public static ABNF nameAddr = REG.rule("name-addr", "[display-name] angle-addr");
    public static ABNF mailbox = REG.rule("mailbox", nameAddr.or(addrSpec));
    static ABNF group = REG.rule("group", "display-name \":\" [group-list] \";\" [CFWS]");
    public static ABNF address = REG.rule("address", mailbox.or(group));
    static ABNF mailboxList = REG.rule("mailbox-list", "(mailbox *(\",\" mailbox)) / obs-mbox-list");
    static ABNF addressList = REG.rule("address-list", "(address *(\",\" address)) / obs-addr-list");
    static ABNF groupList = REG.rule("group-list", "mailbox-list / CFWS / obs-group-list");
    // 3.5. Overall Message Syntax
    static ABNF text = REG.rule("text", "%d1-9 / %d11 / %d12 / %d14-127");
    static ABNF body = REG.rule("body", "(*(*998text CRLF) *998text) / obs-body");
    public static ABNF message = REG.rule("message", "(fields / obs-fields) [CRLF body]");
    static ABNF fields = REG.rule("fields", "*(trace"
            + "  *optional-field /"
            + "  *(resent-date /"
            + "   resent-from /"
            + "   resent-sender /"
            + "   resent-to /"
            + "   resent-cc /"
            + "   resent-bcc /"
            + "   resent-msg-id))"
            + " *(orig-date /"
            + " from /"
            + " sender /"
            + " reply-to /"
            + " to /"
            + " cc /"
            + " bcc /"
            + " message-id /"
            + " in-reply-to /"
            + " references /"
            + " subject /"
            + " comments /"
            + " keywords /"
            + " optional-field)");
    // 3.6.1. The Origination Date Field
    static ABNF origDate = REG.rule("orig-date", "\"Date:\" date-time CRLF");
    // 3.6.2. Originator Fields
    // from, sender, replyToは差し替え対象なので直利用しない方がいい
    static ABNF from = REG.rule("from", "\"From:\" mailbox-list CRLF");
    static ABNF sender = REG.rule("sender", "\"Sender:\" mailbox CRLF");
    static ABNF replyTo = REG.rule("reply-to", "\"Reply-To:\" address-list CRLF");
    // 3.6.3. Destination Address Fields
    static ABNF to = REG.rule("to", "\"To:\" address-list CRLF");
    static ABNF cc = REG.rule("cc", "\"Cc:\" address-list CRLF");
    static ABNF bcc = REG.rule("bcc", "\"Bcc:\" [address-list / CFWS] CRLF");
    // 3.6.4.
    static ABNF msgId = REG.rule("msg-id", "[CFWS] \"<\" id-left \"@\" id-right \">\" [CFWS]");
    static ABNF messageId = REG.rule("message-id", "\"Message-ID:\" msg-id CRLF");
    static ABNF inReplyTo = REG.rule("in-reply-to", "\"In-Reply-To:\" 1*msg-id CRLF");
    static ABNF references = REG.rule("references", "\"References:\" 1*msg-id CRLF");
    static ABNF idLeft = REG.rule("id-left", "dot-atom-text / obs-id-left");
    static ABNF idRight = REG.rule("id-right", "dot-atom-text / no-field-literal / obs-id-right");
    static ABNF noFoldLiteral = REG.rule("no-fold-literal", "\"[\" *dtext \"]\"");


    // 3.6.5. Informational Fields
    static ABNF subject = REG.rule("subject", "\"Subject:\" unstructured CRLF");
    static ABNF comments = REG.rule("comments", "\"Comments:\" unstructured CRLF");
    static ABNF keywords = REG.rule("keywords", "\"Keywords:\" phrase *(\",\" phrase) CRLF");

    static ABNF resentDate = REG.rule("resent-date", "\"Resent-Date:\" date-time CRLF");
    static ABNF resentFrom = REG.rule("resent-from", "\"Resent-From:\" mailbox-list CRLF");
    static ABNF resentSender = REG.rule("resent-sender", "\"Resent-Sender:\" mailbox CRLF");
    static ABNF resentTo = REG.rule("resent-to", "\"Resent-To:\" address-list CRLF");
    static ABNF resentCc = REG.rule("resent-cc", "\"Resent-Cc:\" address-list CRLF");
    static ABNF resentBcc = REG.rule("resent-bcc", "\"Resent-Bcc:\" [address-list / CFWS] CRLF");
    static ABNF resentMsgId = REG.rule("resent-msg-id", "\"Resent-Message-ID:\" msg-id CRLF");
    // 3.6.7. Trace Fields
    static ABNF path = REG.rule("path", "angle-addr / ([CFWS] \"<\" [CFWS] \">\" [CFWS])");
    static ABNF Return = REG.rule("return", "\"Return-Path:\" path CRLF");
    static ABNF receivedToken = REG.rule("received-token", word.or(angleAddr, addrSpec, domain));
//    static ABNF received = REG.rule("received", "\"Received:\" *received-token \";\" date-time CRLF");
    static ABNF received = REG.rule("received", "\"Received:\" [1*received-token / CFWS] \";\" date-time CRLF"); // Errata ID: 3979
    static ABNF trace = REG.rule("trace", "[return] 1*received");

    // 3.6.8. Optional Fields
    static ABNF ftext = REG.rule("ftext", "%d33-57 / %d59-126");
    static ABNF fieldName = REG.rule("field-name", ftext.ix());
    static ABNF optionalFields = REG.rule("optional-field", "field-name \":\" unstructured CRLF");
    // 4. Obsolete Syntax
    // 4.2. Obsolete Folding White Space
//    static ABNF obsFWS = OBS.rule("obs-FWS", "1*WSP *(CRLF 1*WSP)");
    static ABNF obsFWS = REG.rule("obs-FWS", "1*([CRLF] WSP)"); // Errata ID: 1908

    // 4.3. Obsolete Date and Time
    static ABNF obsDayOfWeek = REG.rule("obs-day-of-week", "[CFWS] day-name [CFWS]");
    static ABNF obsDay = REG.rule("obs-day", "[CFWS] 1*2DIGIT [CFWS]");
    static ABNF obsYear = REG.rule("obs-year", "[CFWS] 2*DIGIT [CFWS]");
    static ABNF obsHour = REG.rule("obs-hour", "[CFWS] 2DIGIT [CFWS]");
    static ABNF obsMinute = REG.rule("obs-minute", "[CFWS] 2DIGIT [CFWS]");
    static ABNF obsSecond = REG.rule("obs-second", "[CFWS] 2DIGIT [CFWS]");
    static ABNF obsZone = REG.rule("obs-zone", "\"UT\" / \"GMT\" / \"EST\" / \"EDT\" / \"CST\" / \"CDT\" / "
            + "\"MST\" / \"MDT\" / \"PST\" / \"PDT\" / %d65-73 / %d75-90 / %d97-105 / %d107-122");
    // 4.5. Obsolete Header Fields
    static ABNF obsFields = REG.rule("obs-fields", "*(obs-return /"
            + " obs-received /"
            + " obs-orig-date /"
            + " obs-from /"
            + " obs-sender /"
            + " obs-reply-to /"
            + " obs-to /"
            + " obs-cc /"
            + " obs-bcc /"
            + " obs-message-id /"
            + " obs-in-reply-to /"
            + " obs-references /"
            + " obs-subject /"
            + " obs-comments /"
            + " obs-keywords /"
            + " obs-resent-date /"
            + " obs-resent-from /"
            + " obs-resent-send /"
            + " obs-resent-rply /"
            + " obs-resent-to /"
            + " obs-resent-cc /"
            + " obs-resent-bcc /"
            + " obs-resent-mid /"
            + " obs-optional)");
    // 4.5.1.
    static ABNF obsOrigDate = REG.rule("obs-orig-date", "\"Date\" *WSP \":\" date-time CRLF");
    // 4.5.2.
    static ABNF obsFrom = REG.rule("obs-from", "\"From\" *WSP \":\" mailbox-list CRLF");
    static ABNF obsSender = REG.rule("obs-sender", "\"Sender\" *WSP \":\" mailbox CRLF");
    static ABNF obsReplyTo = REG.rule("obs-reply-to", "\"Reply-To\" *WSP \":\" address-list CRLF");
    // 4.5.3.
    static ABNF obsTo = REG.rule("obs-to", "\"To\" *WSP \":\" address-list CRLF");
    static ABNF obsCc = REG.rule("obs-cc", "\"Cc\" *WSP \":\" address-list CRLF");
    static ABNF obsBcc = REG.rule("obs-bcc", "\"Bcc\" *WSP \":\" (address-list / (*([CFWS] \",\") [CFWS])) CRLF");
    // 4.5.4. Obsolete Identification Fields
    static ABNF obsMessageId = REG.rule("obs-message-id", "\"Message-ID\" *WSP \":\" msg-id CRLF");
    static ABNF obsInReplyTo = REG.rule("obs-in-reply-to", "\"In-Reply-To\" *WSP \":\" *(phrase / msg-id) CRLF");
    static ABNF obsReferences = REG.rule("obs-references", "\"References\" *WSP \":\" *(phrase / msg-id) CRLF");
    static ABNF obsIdLeft = REG.rule("obs-id-left", localPart);
    static ABNF obsIdRight = REG.rule("obs-id-right", domain);
    // 4.5.5. Obsolete Informational FIelds
    static ABNF obsSubject = REG.rule("obs-subject", "\"Subject\" *WSP \":\" unstructired CRLF");
    static ABNF obsComments = REG.rule("obs-comments", "\"Comments\" *WSP \":\" unstructured CRLF");
    static ABNF obsKeywords = REG.rule("obs-keywords", "\"Keywords\" *WSP \":\" obs-phrase-list CRLF");
    // 4.5.6. Obsolete Resent Fields
    static ABNF obsResentFrom = REG.rule("obs-resent-from", "\"Resent-From\" *WSP \":\" mailbox-list CRLF");
    static ABNF obsResentSend = REG.rule("obs-resent-send", "\"Resent-Sender\" *WSP \":\" mailbox CRLF");
    static ABNF obsResentDate = REG.rule("obs-resent-date", "\"Resent-Date\" *WSP \":\" date-time CRLF");
    static ABNF obsResentTo = REG.rule("obs-resent-to", "\"Resent-To\" *WSP \":\" address-list CRLF");
    static ABNF obsResentCc = REG.rule("obs-resent-cc", "\"Resent-Cc\" *WSP \":\" address-list CRLF");
    static ABNF obsResentBcc = REG.rule("obs-resent-bcc", "\"Resent-Bcc\" *WSP \":\" (address-list / (*([CFWS] \",\") [CFWS])) CRLF");
    static ABNF obsResentMid = REG.rule("obs-resent-mid", "\"Resent-Message-ID\" *WSP \":\" msg-id CRLF");
    static ABNF obsResentRply = REG.rule("obs-resent-rply", "\"Resent-Reply-To\" *WSP \":\" address-list CRLF");
    // 4.5.7. Obsolete Trace Fields
    static ABNF obsReturn = REG.rule("obs-return", "\"Return-Path\" *WSP \":\" path CRLF");
//    static ABNF obsReceived = OBS.rule("obs-received", "\"Received\" *WSP \":\" *received-token CRLF");
    static ABNF obsReceived = REG.rule("obs-received", "\"Received\" *WSP \":\" [1*received-token / CFWS] CRLF"); // Errata ID: 3979
    // 4.5.8. Obsolete optional fields
    static ABNF obsOptional = REG.rule("obs-optional", "field-name *WSP \":\" unstructured CRLF");
}
