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

    /**
     * regが不要な文字系のところ
     * @param def 
     */
    protected ABNFBaseParser(ABNF def) {
        this.def = def;
    }

    protected ABNFBaseParser(ABNF def, ABNFReg reg) {
        this.def = def;
        this.reg = reg;
    }

    /**
     * 
     * @param def
     * @param reg 名前空間参照用
     * @param base Parser駆動用
     * @param subns 
     */
    protected ABNFBaseParser(ABNF def, ABNFReg reg, ABNFReg base, String... subns) {
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
    @Override
    public T parse(String str) {
        return parse(AbstractABNF.pac(str));
    }

    /**
     * たぶん名前空間を使わない簡易パーサの生成
     * かなにかに改名する?
     * @param bnf
     * @return 
     */
    protected ABNFPacketParser x(ABNF bnf) {
        return new ABNFPacketParser(bnf, reg);
    }
    
    /**
     * x() を省略してABNFで指定したいだけの版
     * def側へ移したいがregが依存している
     * @param pac
     * @param defs
     * @return 
     */
    protected ABNF.C find(Packet pac, ABNF... defs) {
        ABNFParser[] ps = new ABNFParser[defs.length];
        for ( int i = 0; i < defs.length; i++ ) {
            ps[i] = x(defs[i]);
        }
        return def.find(pac, ps);
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
