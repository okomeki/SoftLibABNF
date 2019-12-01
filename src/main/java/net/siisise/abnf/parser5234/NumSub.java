package net.siisise.abnf.parser5234;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.parser.ABNFBaseParser;
import net.siisise.io.Packet;
import net.siisise.io.PacketA;
import net.siisise.lang.CodePoint;

/**
 * 内部で使用する数値用Parser。
 * 数値の抽出でだいたい共通の部分。
 * 
 */
public class NumSub extends ABNFBaseParser<ABNF,ABNF> {
    static ABNF hf = ABNF.bin('-');
    static ABNF dot = ABNF.bin('.');
    
    ABNF form;
    int dig;
    char a,b;

    NumSub(ABNF def, ABNF fm, int dig, char a, char b) {
        super(def);
        form = fm;
        this.dig = dig;
        this.a = a;
        this.b = b;
    }

    @Override
    public ABNF parse(Packet pac) {
        int d = pac.read();
        if ( d != a && d != b ) {
            pac.backWrite(d);
            return null;
        }
        int val = num(pac);

        Packet r = hf.is(pac);
        if ( r != null ) {
            int n = num(pac);
            return ABNF.range(val,n);
        }

        r = dot.is(pac);
        if ( r != null ) {
            Packet data = new PacketA();
            data.write(CodePoint.utf8(val));
            do {
                data.write(CodePoint.utf8(num(pac)));
                r = dot.is(pac);
            } while ( r != null );
            return ABNF.bin(str(data));
        }
        return ABNF.bin(val);
    }
    
    int num(Packet pac) {
        Packet num = form.ix().is(pac);
        return Integer.parseInt(str(num),dig);
    }
}
