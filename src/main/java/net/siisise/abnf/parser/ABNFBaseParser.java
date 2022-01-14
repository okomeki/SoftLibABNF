package net.siisise.abnf.parser;

import net.siisise.abnf.ABNF;
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
     * 上のparserから駆動される想定
     * @param rule 処理対象のABNF rule
     */
    protected ABNFBaseParser(ABNF rule) {
        this.rule = rule;
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
    
    @Override
    public <N> T parse(String str, N ns) {
        return parse(AbstractABNF.pac(str), ns);
    }

    @Override
    public <N> T parse(FrontPacket pac, N ns) {
        return parse(pac);
    }

    protected static String str(FrontPacket pac) {
        return new String(pac.toByteArray(), ABNF.UTF8);
    }

    protected static String strd(FrontPacket pac) {
        byte[] b = pac.toByteArray();
        pac.dbackWrite(b);
        return new String(b, ABNF.UTF8);
    }
}
