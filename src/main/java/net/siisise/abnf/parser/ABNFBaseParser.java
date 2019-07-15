package net.siisise.abnf.parser;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.AbstractABNF;
import net.siisise.io.Packet;

/**
 *
 * @author okome
 * @param <T> 戻り型
 * @param <M>
 */
public abstract class ABNFBaseParser<T, M> implements ABNFParser<T> {

    protected ABNF def;
    /** 名前空間参照用 */
    protected ABNFReg reg;
    /** サブパーサ展開用 */
    protected ABNFReg base;
    protected ABNFParser<? extends M>[] subs;
    protected Class<? extends ABNFParser<? extends M>>[] subc;

    protected ABNFBaseParser(ABNF def, ABNFParser<M>... subs) {
        this.def = def;
        this.subs = subs;
    }

    protected ABNFBaseParser(ABNF def, ABNFReg reg, ABNFParser<M>... subs) {
        this.def = def;
        this.reg = reg;
        this.subs = subs;
    }

    public ABNFBaseParser(ABNF def, ABNFReg reg) {
        this.def = def;
        this.reg = reg;
    }

    public ABNFBaseParser(ABNF def, ABNFReg reg, Class<? extends ABNFParser<? extends M>>... subc) {
        this.def = def;
        this.reg = reg;
        this.subc = subc;
    }

    /**
     * 
     * @param def
     * @param reg
     * @param base
     * @param subns 
     */
    public ABNFBaseParser(ABNF def, ABNFReg reg, ABNFReg base, String... subns) {
        this.def = def;
        this.reg = reg;
        this.base = base;
        subc = new Class[subns.length];
        
        for ( int i = 0; i < subns.length; i++ ) {
            subc[i] = (Class<? extends ABNFParser<? extends M>>) base.CL.get(subns[i]);
        }
    }

//    protected ABNFBaseParser(ABNF def) {
//        this.def = def;
//    }
    protected void inst() {
        if (subs == null && subc != null) {
            try {
                subs = new ABNFParser[subc.length];
                for (int i = 0; i < subc.length; i++) {
                    subs[i] = subc[i].getConstructor(ABNFReg.class).newInstance(reg);
//                    subs[i] = subc[i].getConstructor().newInstance();
                }
            } catch (InstantiationException ex) {
                Logger.getLogger(ABNFBaseParser.class.getName()).log(Level.SEVERE, null, ex);
                throw new java.lang.UnsupportedOperationException();
            } catch (IllegalAccessException ex) {
                Logger.getLogger(ABNFBaseParser.class.getName()).log(Level.SEVERE, null, ex);
                throw new java.lang.UnsupportedOperationException();
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(ABNFBaseParser.class.getName()).log(Level.SEVERE, null, ex);
                throw new java.lang.UnsupportedOperationException();
            } catch (InvocationTargetException ex) {
                Logger.getLogger(ABNFBaseParser.class.getName()).log(Level.SEVERE, null, ex);
                throw new java.lang.UnsupportedOperationException();
            } catch (NoSuchMethodException ex) {
                Logger.getLogger(ABNFBaseParser.class.getName()).log(Level.SEVERE, null, ex);
                throw new java.lang.UnsupportedOperationException();
            } catch (SecurityException ex) {
                Logger.getLogger(ABNFBaseParser.class.getName()).log(Level.SEVERE, null, ex);
                throw new java.lang.UnsupportedOperationException();
            }
        }
    }

    @Override
    public ABNF getBNF() {
        return def;
    }

    @Override
    public abstract T parse(Packet pac);

    public T parse(String val) {
        return parse(AbstractABNF.pac(val));
    }

    protected ABNFPacketParser x(ABNF def) {
        return new ABNFPacketParser(def, reg);
    }

    protected static String str(Packet pac) {
        try {
            return new String(pac.toByteArray(), "utf-8");
        } catch (UnsupportedEncodingException ex) {
            throw new java.lang.UnsupportedOperationException();
        }
    }

    protected static String strd(Packet pac) {
        byte[] b = pac.toByteArray();
        pac.backWrite(b);
        try {
            return new String(b, "utf-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ABNFBaseParser.class.getName()).log(Level.SEVERE, null, ex);
            throw new java.lang.UnsupportedOperationException();
        }
    }
}
