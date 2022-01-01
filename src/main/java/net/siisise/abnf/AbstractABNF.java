package net.siisise.abnf;

import net.siisise.bnf.AbstractBNF;

/**
 * 簡単なプレ実装
 */
public abstract class AbstractABNF extends AbstractBNF implements ABNF {

    @Override
    public ABNF name(String name) {
        return new ABNFor(name, this); // ?
    }

    /**
     * *(a / b ) a の様なものが解析できないが速そう
     * @param val
     * @return 
     */
    @Override
    public ABNF pl(ABNF... val) {
        if (val.length == 0) {
            return this;
        }
        ABNF[] list = new ABNF[val.length + 1];
        list[0] = this;
        System.arraycopy(val, 0, list, 1, val.length);
        return new ABNFpl(list);
    }

    /**
     * *( a / b ) a にも対応
     * @param val
     * @return 
     */
    @Override
    public ABNF plm(ABNF... val) {
        if (val.length == 0) {
            return this;
        }
        ABNF[] list = new ABNF[val.length + 1];
        list[0] = this;
        System.arraycopy(val, 0, list, 1, val.length);
        return new ABNFplm(list);
    }

    /**
     * Unicode単位で比較する若干速いのかもしれない版 plm
     * @param val
     * @return 
     */
    @Override
    public ABNF plu(ABNF... val) {
        if (val.length == 0) {
            return this;
        }
        ABNF[] list = new ABNF[val.length + 1];
        list[0] = this;
        System.arraycopy(val, 0, list, 1, val.length);
        return new ABNFplu(list);
    }
    
    public ABNF mn(ABNF val) {
        return new ABNFmn(this, val);
    }

    @Override
    public ABNF or(ABNF... val) {
        ABNF[] list = new ABNF[val.length + 1];
        list[0] = this;
        System.arraycopy(val, 0, list, 1, val.length);
        return new ABNFor(list);
    }

    /**
     *
     * @param <X>
     * @param ret
     * @param sub
     */
    static <X> void mix(C<X> ret, C<X> sub) {
        ret.ret.write(sub.ret.toByteArray());
        sub.subs.forEach((key,val) -> {
            val.forEach((v) -> {
                ret.add(key, v);
            });
        });
    }

    @Override
    public ABNF x(int min, int max) {
        return new ABNFx(min, max, this);
    }

    @Override
    public ABNF x() {
        return x(0, -1);
    }

    @Override
    public ABNF ix() {
        return x(1, -1);
    }

    @Override
    public ABNF c() {
        return x(0, 1);
    }

    protected String hex(int ch) {
        if (ch < 0x100) {
            return "%x" + Integer.toHexString(0x100 + ch).substring(1);
        } else if (ch < 0x10000) {
            return "%x" + Integer.toHexString(0x10000 + ch).substring(1);
        } else {
            return "%x" + Integer.toHexString(0x1000000 + ch).substring(1);
        }
    }
}
