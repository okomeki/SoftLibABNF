package net.siisise.abnf;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.siisise.abnf.parser.ABNFParser;
import net.siisise.abnf.parser5234.ABNF5234;
import net.siisise.io.Packet;

/**
 * ABNFの名前担当、Parserの機能もあり
 * Namespace
 * 
 * rule: 基本は RFC 5234 に準拠するが、一部改変したParserの対応も可能
 *
 * @author okome
 */
public class ABNFReg {

    Map<String, ABNF> reg = new HashMap<>();

    ABNFReg parseReg;

    public Map<String, Class<? extends ABNFParser>> CL = new HashMap<>();

    /**
     * 名前の参照を先に済ませる
     */
    public class ABNFRef extends FindABNF {

        ABNFRef(String name) {
            this.name = name;
        }

        @Override
        public C find(Packet pac, ABNFParser... parsers) {
            C ret = reg.get(name).find(pac, parsers);
            if ( ret == null ) {
                return null;
            }
            return sub(ret, parsers);
        }

        @Override
        public ABNF copy(ABNFReg reg) {
            return reg.ref(name);
        }

    }

    public ABNFReg() {
        parseReg = ABNF5234.REG;
    }

    /**
     * いろいろ未定
     * up の定義を複製する
     * HTTP7230では拡張の実験をしている
     * @param up
     * @param exParser Parserの種類 ABNF5234.REG,RFC 7405, RFC 7230など微妙に違うとき。利用しないときのみ省略したい
     */
    public ABNFReg(ABNFReg up, ABNFReg exParser) {
        if ( up != null ) {
            //reg = new HashMap<>(up.reg); // 複製しておくのが簡単
            up.CL.keySet().forEach((key) -> {
                CL.put(key, up.CL.get(key));
            });
            up.reg.keySet().forEach((key) -> { // 循環参照対策が必要
                reg.put(key, up.reg.get(key).copy(this));
            });
        }
        parseReg = exParser;
    }

    public ABNFReg(ABNFReg up) {
        this(up, ABNF5234.REG);
    }

    /**
     * 参照リンク優先
     *
     * @param name
     * @return
     */
    public ABNF ref(String name) {
        ABNF bnf = reg.get(name);
        if (bnf == null || !(bnf instanceof ABNFRef)) {
            bnf = new ABNFRef(name);
        }
        return bnf;
    }

    /**
     * 直リンク優先 差し替えが困難
     *
     * @param name
     * @return
     */
    public ABNF href(String name) {
        ABNF bnf = reg.get(name);
        if (bnf == null) {
            bnf = new ABNFRef(name);
        }
        return bnf;
    }

    /**
     * ref の参照先を変えないよう書き換えたい
     *
     * @param name
     * @param abnf
     * @return
     */
    public ABNF rule(String name, ABNF abnf) {
        if (ABNF5234.rulename != null && !ABNF5234.rulename.eq(name)) {
            System.err.println("ABNF:" + name + " ABNFの名称には利用できません");
        }
        System.out.println("rule: " + name + ":" + abnf.toString());
        if (!name.equals(abnf.getName())) {
            abnf = abnf.name(name);
        }
        reg.put(name, abnf);
        return abnf;
    }

    /**
     * 仮
     *
     * @param name
     * @param cl
     * @param abnf
     * @return
     */
    public ABNF rule(String name, Class<? extends ABNFParser> cl, ABNF abnf) {
        abnf = rule(name, abnf);
        CL.put(name, cl);
        return abnf;
    }

    public ABNF rule(String name, Class<? extends ABNFParser> cl, String rule) {
        return rule(name, cl, rule(name, rule));
    }

    /**
     * 
     * @param rule name = value 改行を省略可能に改変している
     * @return
     */
    public ABNF rule(String rule) {
        ABNF abnf = baseParse("rule", rule + "\r\n");
        return rule(abnf.getName(), abnf);
    }

    /**
     * Parser構築用
     * 参照空間とABNF Parserが別に存在する場合が多い
     * 裏側(ABNF Parser)を駆動する
     * @param name
     * @param src
     * @return 
     */
    public ABNF baseParse(String name, String src) {
        try {
            Constructor<? extends ABNFParser> cnst = parseReg.CL.get(name).getConstructor(ABNF.class,ABNFReg.class, ABNFReg.class);
            return (ABNF) cnst.newInstance(parseReg.reg.get(name),this, parseReg).parse(src);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException ex) {
            throw new java.lang.UnsupportedOperationException(ex);
        }
    }
    
    List<ABNF> listParse(String name, String src) {
        try {
            Constructor<? extends ABNFParser> cnst = parseReg.CL.get(name).getConstructor(ABNF.class,ABNFReg.class, ABNFReg.class);
            return (List<ABNF>) cnst.newInstance(parseReg.reg.get(name),this, parseReg).parse(src);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException ex) {
            throw new java.lang.UnsupportedOperationException(ex);
        }
    } 

    /**
     * ユーザ側のParser(JSONなど)を駆動する
     * BASEのみで参照先がないなど
     * @param <T>
     * @param name
     * @param src
     * @return 
     */
    public <T> T parse(String name, String src) {
        try {
//            System.out.println(name);
            Constructor<? extends ABNFParser> c = CL.get(name).getConstructor(ABNF.class, ABNFReg.class,ABNFReg.class);
            return (T) c.newInstance(reg.get(name),this,this).parse(src);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException ex) {
            throw new java.lang.UnsupportedOperationException(ex);
        }
    }

    /**
     * ToDo: ABNF5234 へ
     *
     * @param name
     * @param elements
     * @return
     */
    public ABNF rule(String name, String elements) {
        ABNF abnf = baseParse("elements", elements);
        return rule(name, abnf);
    }

    public List<ABNF> rulelist(String rulelist) {
        List<ABNF> list = listParse("rulelist", rulelist);
        list.forEach((abnf) -> {
            reg.put(abnf.getName(), abnf);
        });
        return list;
    }

}
