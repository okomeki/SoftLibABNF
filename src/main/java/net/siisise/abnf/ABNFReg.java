package net.siisise.abnf;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.siisise.abnf.parser.ABNFParser;
import net.siisise.abnf.parser5234.ABNF5234;
import net.siisise.abnf.parser5234.Elements;
import net.siisise.abnf.parser5234.Rule;
import net.siisise.io.Packet;

/**
 * ABNFの名前担当
 *
 * @author okome
 */
public class ABNFReg {

    Map<String, ABNF> reg = new HashMap<>();
    
    ABNFReg parseReg;

    public Map<String,Class<? extends ABNFParser>> CL = new HashMap<>();
    
    /**
     * 名前の参照を先に済ませる
     */
    public class ABNFRef extends AbstractABNF {

        ABNFRef(String name) {
            this.name = name;
        }

        @Override
        public B find(Packet pac, String... names) {
//            System.out.println("ref:" + name);
            B ret = reg.get(name).find(pac, names);
            if (ret == null) {
                return null;
            }
            return sub(ret, names);
        }

        @Override
        public ABNF copy(ABNFReg reg) {
            return reg.ref(name);
        }

    }

    public ABNFReg() {
    }

    /**
     * いろいろ未定
     * @param up 
     * @param exParser 
     */
    public ABNFReg(ABNFReg up, ABNFReg exParser) {
        //reg = new HashMap<>(up.reg); // 複製しておくのが簡単
        up.CL.keySet().forEach((key) -> {
            CL.put(key, up.CL.get(key));
        });
        up.reg.keySet().forEach((key) -> {
            reg.put(key, up.reg.get(key).copy(this));
        });
        parseReg = exParser;
    }
    
    public ABNFReg(ABNFReg up) {
        this(up,null);
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
     * 直リンク優先
     * 差し替えが困難
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
     * @param name
     * @param cl
     * @param abnf
     * @return 
     */
    public ABNF rule(String name, Class<? extends ABNFParser> cl, ABNF abnf) {
        abnf = rule(name,abnf);
        CL.put(name, cl);
        return abnf;
    }

    /**
     * 
     * @param rule name = value
     * @return 
     */
    public ABNF rule(String rule) {
        ABNF abnf;
        if ( parseReg == null ) {
            abnf = new Rule(this).parse(rule);
        } else {
            abnf = parse("rule",rule);
        }
        return rule(abnf.getName(), abnf);
    }
    
    private <T extends ABNF> T parse(String name, String rl) {
        try {
            return (T) parseReg.CL.get(name).getConstructor(this.getClass(), parseReg.getClass()).newInstance(this, parseReg).parse(rl);
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
        ABNF abnf;
        if ( parseReg == null ) {
            abnf = new Elements(this).parse(elements);
        } else {
            abnf = parse("elements",elements);
        }
        return rule(name, abnf);
    }

    public List<ABNF> rulelist(String rulelist) {
        List<ABNF> list = parse("rulelist",rulelist);
        list.forEach((abnf) -> {
            reg.put(abnf.getName(), abnf);
        });
        return list;
    }

}
