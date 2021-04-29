package net.siisise.abnf.parser5234;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.parser.ABNFBaseParser;
import net.siisise.io.FrontPacket;
import net.siisise.io.Packet;
import net.siisise.io.PacketA;
import net.siisise.lang.CodePoint;

/**
 * 内部で使用する数値用Parser。
 * 数値の抽出でだいたい共通の部分。
 *
 */
class NumSub extends ABNFBaseParser<ABNF, ABNF> {

    private static final ABNF hf = ABNF.bin('-');
    private static final ABNF dot = ABNF.bin('.');

    private final ABNF nrule;
    private final int dig;
    private final char a, b;

    NumSub(ABNF rule, ABNF numrule, int dig, char a, char b) {
        super(rule);
        nrule = numrule.ix();
        this.dig = dig;
        this.a = a;
        this.b = b;
    }

    @Override
    public ABNF parse(FrontPacket pac) {
        int c = pac.read();
        if (c != a && c != b) {
            pac.backWrite(c);
            return null;
        }
        int val = num(pac);

        FrontPacket r = hf.is(pac);
        if (r != null) {
            int max = num(pac);
            return ABNF.range(val, max);
        }

        r = dot.is(pac);
        if (r != null) {
            Packet data = new PacketA();
            data.write(CodePoint.utf8(val));
            do {
                data.write(CodePoint.utf8(num(pac)));
                r = dot.is(pac);
            } while (r != null);
            return ABNF.bin(str(data));
        }
        return ABNF.bin(val);
    }

    private int num(FrontPacket pac) {
        FrontPacket num = nrule.is(pac);
        return Integer.parseInt(str(num), dig);
    }
}
