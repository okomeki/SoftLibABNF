package net.siisise.bnf;

import net.siisise.block.ReadableBlock;

/**
 *
 */
public class BNFtext extends IsBNF {

    private final String text;
    private final byte[] utf8;

    /**
     *
     * @param ch
     */
    BNFtext(int ch) {
        char[] chars = Character.toChars(ch);
        text = String.valueOf(chars);
        if (ch < 0x7f && ch >= 0x20 && ch != 0x22) {
            name = "\"" + text + "\"";
        } else {
            name = hex(ch);
        }
        utf8 = text.getBytes(UTF8);
    }

    BNFtext(String text) {
        this.text = text;
        name = "\"" + text + "\"";
        utf8 = text.getBytes(UTF8);
    }

    /**
     *
     * @param pac
     * @return 一致した結果
     */
    @Override
    public ReadableBlock is(ReadableBlock pac) {
        if (pac.length() < 1) {
            return null;
        }
        byte[] d = new byte[utf8.length];
        int size = pac.read(d);
        if ( size < utf8.length ) {
            pac.back(size);
            return null;
        }
        String u;
        u = new String(d, UTF8);
        if (u.equalsIgnoreCase(text)) {
            return ReadableBlock.wrap(d);
        }
        pac.back(size);
        return null;
    }

    @Override
    public BNF copy(BNFReg reg) {
        return new BNFtext(text);
    }

    @Override
    public String toJava() {
        return "BNF.text(" +text+ ")";
    }
}
