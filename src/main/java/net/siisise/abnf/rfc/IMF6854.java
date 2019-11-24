package net.siisise.abnf.rfc;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;

/**
 * IMF5322のFromなど修正
 *
 * @author okome
 */
public class IMF6854 {

    public static ABNFReg REG = new ABNFReg(IMF5322.REG);

    public static ABNF from = REG.rule("from", "\"From:\" (mailbox-list / address-list) CRLF");
    public static ABNF sender = REG.rule("sender", "\"Sender:\" (mailbox / address) CRLF");
    public static ABNF replyTo = REG.rule("reply-to", "\"Reply-To:\" address-list CRLF");

    static ABNF resentFrom = REG.rule("resent-from", "\"Resent-From:\" (mailbox-list / address-list) CRLF");
    static ABNF resentSender = REG.rule("resent-sender", "\"Resent-Sender:\" (mailbox / address) CRLF");
}
