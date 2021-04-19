package net.siisise.abnf.parser;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.abnf.AbstractABNF;
import net.siisise.io.FrontPacket;

/**
 * ABNF パース後の変換処理。
 * ABNFに一致したときに処理が走るので基本的に例外返しがない。
 * 
 * @param <T> 戻り型
 * @param <M> 中間形式
 */
public abstract class ABNFBaseParser<T, M> implements ABNFParser<T> {

    protected ABNF rule;
    /** 名前空間参照用 */
    protected ABNFReg reg;
    /** サブパーサ展開用 */
    protected ABNFReg base;
    protected ABNFParser<? extends M>[] subs;
    private Class<? extends ABNFParser<? extends M>>[] subpClass;
    protected String[] subName;

    /**
     * regが不要な文字系のところ
     * @param rule 
     */
    protected ABNFBaseParser(ABNF rule) {
        this.rule = rule;
    }

    /**
     * 判定には使わない文字列やPacketがほしいあれ
     * @param rule
     * @param reg 
     */
    protected ABNFBaseParser(ABNF rule, ABNFReg reg) {
        this.rule = rule;
        this.reg = reg;
    }
    
    /**
     * 
     * @param rule 処理対象のABNF構文
     * @param reg 名前空間参照用
     * @param base Parser駆動用
     * @param subns 
     */
    protected ABNFBaseParser(ABNF rule, ABNFReg reg, ABNFReg base, String... subns) {
        this.rule = rule;
        this.reg = reg;
        setSub(base, subns);
    }

    private void setSub(ABNFReg base, String... subns) {
        this.base = base;
        subpClass = new Class[subns.length];
        subName = new String[subns.length];
        
        for ( int i = 0; i < subns.length; i++ ) {
            subName[i] = subns[i];
            subpClass[i] = (Class<? extends ABNFParser<? extends M>>) base.CL.get(subName[i]);
        }
    }

    protected void inst() {
        if (subs == null && subpClass != null) {
            try {
                subs = new ABNFParser[subpClass.length];
                for (int i = 0; i < subpClass.length; i++) {
                    if ( base != null ) {
//                        System.out.println(subName[i]);
                        subpClass[i] = (Class<? extends ABNFParser<? extends M>>)base.CL.get(subName[i]);
                        Constructor<? extends ABNFParser<? extends M>> cnst = subpClass[i].getConstructor(ABNF.class,ABNFReg.class,ABNFReg.class);
                        subs[i] = cnst.newInstance(base.href(subName[i]),reg,base);
                    } else {
                        subs[i] = subpClass[i].getConstructor(ABNFReg.class).newInstance(reg);
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
        return rule;
    }

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
     * findは比較してない
     * @param bnf
     * @return 
     */
    protected ABNFPacketParser pacp(ABNF bnf) {
        return new ABNFPacketParser(bnf);
    }

    /**
     * findがざるな簡易版
     * @param bnf
     * @return 
     */
    protected ABNFStringParser strp(ABNF bnf) {
        return new ABNFStringParser(bnf);
    }
    
    /**
     * pacp() を省略してABNFで指定したいだけの版
     * @param pac
     * @param defs
     * @return 
     */
    protected ABNF.C find(FrontPacket pac, ABNF... defs) {
        ABNFParser[] ps = new ABNFParser[defs.length];
        for ( int i = 0; i < defs.length; i++ ) {
            ps[i] = pacp(defs[i]);
        }
        return rule.find(pac, ps);
    }
    
    protected static String str(FrontPacket pac) {
        return new String(pac.toByteArray(), ABNF.UTF8);
    }

    protected static String strd(FrontPacket pac) {
        byte[] b = pac.toByteArray();
        pac.backWrite(b);
        return new String(b, ABNF.UTF8);
    }
}
