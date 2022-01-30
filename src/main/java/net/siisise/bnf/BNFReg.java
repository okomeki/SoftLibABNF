package net.siisise.bnf;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import net.siisise.bnf.parser.BNFPacketParser;
import net.siisise.bnf.parser.BNFParser;
import net.siisise.io.FrontPacket;
import net.siisise.io.StreamFrontPacket;

/**
 *
 */
public class BNFReg {

    protected final Map<String, BNF> reg = new HashMap<>();
    protected final Map<String, Class<? extends BNFParser>> CL = new HashMap<>();

    /**
     * BNF の Parser
     */
    protected BNFReg bnfReg;
    /**
     * rulename の制約
     */
    protected BNF rn;

    /**
     * ユーザ側のParser(JSONなど)を駆動する。 BASEのみで参照先がないなど
     *
     * @param <T> 解析型
     * @param rulename 解析装置付き構文の名。駆動コマンドのようなもの
     * @param src パース対象ソース
     * @return 解析後の実体
     */
    public <T> T parse(String rulename, String src) {
        return (T) parser(rulename).parse(src);
    }

    public <T> T parse(String rulename, String src, Object ns) {
        return (T) parser(rulename).parse(src, ns);
    }

    public <T> T parse(String rulename, byte[] src) {
        FrontPacket pac = new StreamFrontPacket(new ByteArrayInputStream(src));
        return (T) parser(rulename).parse(pac);
    }

    public <T> T parse(String rulename, byte[] src, Object ns) {
        FrontPacket pac = new StreamFrontPacket(new ByteArrayInputStream(src));
        return (T) parser(rulename).parse(pac, ns);
    }

    /**
     * ユーザ側のParser(JSONなど)を駆動する。 BASEのみで参照先がないなど
     *
     * @param <T> 解析型
     * @param rulename 解析装置付き構文の名。駆動コマンドのようなもの
     * @param pac パース対象ソース
     * @return 解析後の実体
     */
    public <T> T parse(String rulename, FrontPacket pac) {
        return (T) parser(rulename).parse(pac);
    }

    public <T> T parse(String rulename, FrontPacket pac, Object ns) {
        return (T) parser(rulename).parse(pac, ns);
    }

    public <T> BNFParser<T> parser(String rulename) {
        CL.get(rulename);
        BNF rule = reg.get(rulename);
        Class<? extends BNFParser> rulep = CL.get(rulename);
        if (rulep == null) {
            return (BNFParser<T>) new BNFPacketParser(rule);
        }
        try {
            Constructor<? extends BNFParser> cnst;
            cnst = (Constructor<? extends BNFParser<T>>) rulep.getConstructor(BNF.class, BNFReg.class);
            return cnst.newInstance(rule, this);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException ex) {
            throw new java.lang.UnsupportedOperationException(ex);
        }
    }
}
