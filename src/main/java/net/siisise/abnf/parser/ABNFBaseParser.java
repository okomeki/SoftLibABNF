package net.siisise.abnf.parser;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
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
    protected String[] subName;

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

    /**
     * @deprecated 参照をもっと弱くする
     * @param def
     * @param reg
     * @param subc 
     */
    public ABNFBaseParser(ABNF def, ABNFReg reg, Class<? extends ABNFParser<? extends M>>... subc) {
        this.def = def;
        this.reg = reg;
        this.subc = subc;
    }

    /**
     * 
     * @param def
     * @param reg 名前空間参照用
     * @param base Parser駆動用
     * @param subns 
     */
    public ABNFBaseParser(ABNF def, ABNFReg reg, ABNFReg base, String... subns) {
        this.def = def;
        this.reg = reg;
        this.base = base;
        subc = new Class[subns.length];
        subName = new String[subns.length];
        
        for ( int i = 0; i < subns.length; i++ ) {
            subName[i] = subns[i];
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
                    if ( base != null ) {
//                        System.out.println(subName[i]);
                        subc[i] = (Class<? extends ABNFParser<? extends M>>)base.CL.get(subName[i]);
                        Constructor<? extends ABNFParser<? extends M>> cnst = subc[i].getConstructor(ABNF.class,ABNFReg.class,ABNFReg.class);
                        subs[i] = cnst.newInstance(base.href(subName[i]),reg,base);
                    } else {
                        subs[i] = subc[i].getConstructor(ABNFReg.class).newInstance(reg);
//                      subs[i] = subc[i].getConstructor().newInstance();
                    }
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
//                System.out.println("");
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

    /**
     * pacを解析、完了した部分を先頭から削除する
     * 先頭一致で解析するのでpacにデータが残る場合もある
     * 失敗した(一致しなかった)場合、pacは元の状態、戻り値はnullとなる
     * @param pac 解析対象データ
     * @return 変換されたデータ 不一致の場合はnull
     */
    @Override
    public abstract T parse(Packet pac);

    /**
     * 入り口
     * @param str 解析対象文字列
     * @return 変換されたデータ 不一致の場合はnull
     */
    public T parse(String str) {
        return parse(AbstractABNF.pac(str));
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
