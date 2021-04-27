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

    protected final ABNF rule;
    /**
     * ユーザ側名前空間参照用
     * ABNFでは使うがJSONでは未使用など
     */
    protected final Object reg;
    /** ABNF Parser側 名前空間 */
    protected final ABNFReg base;
    protected ABNFParser<? extends M>[] subs;
    private Class<? extends ABNFParser<? extends M>>[] subpClass;
    protected String[] subName;

    /**
     * regが不要な文字系のところ
     * @param rule 
     */
    protected ABNFBaseParser(ABNF rule) {
        this.rule = rule;
        reg = null;
        base = null;
    }

    /**
     *  
     * @param rule 処理対象のABNF rule
     * @param reg ユーザ名前空間参照用 ABNF定義時などにつかう
     * @param base Parser駆動用 ABNFParserのABNFReg
     * @param subns 
     */
    protected ABNFBaseParser(ABNF rule, Object reg, ABNFReg base, String... subns) {
        this.rule = rule;
        this.reg = reg;
        this.base = base;
        setSub(subns);
    }

    private void setSub(String... subns) {
        subpClass = new Class[subns.length];
        subName = subns;
        
//        for ( int i = 0; i < subns.length; i++ ) {
//            subpClass[i] = (Class<? extends ABNFParser<? extends M>>) base.CL.get(subName[i]);
//        }
    }

    protected void inst() {
        if (subs == null && subpClass != null) {
            try {
                subs = new ABNFParser[subpClass.length];
                for (int i = 0; i < subpClass.length; i++) {
                    if ( base != null ) {
//                        System.out.println(subName[i]);
                        subpClass[i] = (Class<? extends ABNFParser<? extends M>>)base.CL.get(subName[i]);
                        if ( subpClass[i] == null ) {
                            subs[i] = (ABNFParser)new ABNFPacketParser(base.href(subName[i]));
                        } else {
                            Constructor<? extends ABNFParser<? extends M>> cnst;
                            if ( reg == null ) {
                                cnst = subpClass[i].getConstructor(ABNF.class, ABNFReg.class);
                                subs[i] = cnst.newInstance(base.href(subName[i]),base);
                            } else {
                                cnst = subpClass[i].getConstructor(ABNF.class, reg.getClass(), ABNFReg.class);
                                subs[i] = cnst.newInstance(base.href(subName[i]),reg,base);
                            }
                        }
                    } else {
                        subs[i] = subpClass[i].getConstructor(ABNFReg.class).newInstance(reg);
                    }
                }
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
                Logger.getLogger(ABNFBaseParser.class.getName()).log(Level.SEVERE, null, ex);
                throw new java.lang.UnsupportedOperationException(ex);
            }
//                System.out.println("");
            
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

    protected static String str(FrontPacket pac) {
        return new String(pac.toByteArray(), ABNF.UTF8);
    }

    protected static String strd(FrontPacket pac) {
        byte[] b = pac.toByteArray();
        pac.backWrite(b);
        return new String(b, ABNF.UTF8);
    }
}
