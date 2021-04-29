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
     * ユーザ側名前空間参照用
     * ABNFでは使うがJSONでは未使用など
     */
    protected final Object reg;

    /**
     * regが不要な文字系のところ
     * @param rule 
     */
    protected ABNFBaseParser(ABNF rule) {
        this.rule = rule;
        reg = null;
    }

    /**
     * 上のparserから駆動される想定
     * @param rule 処理対象のABNF rule
     * @param reg ユーザ名前空間参照用 ABNF定義時などにつかう
     * @param base Parser駆動 subns用, ABNFParserのABNFReg
     * @param subns 参照する内側の要素
     */
    protected ABNFBaseParser(ABNF rule, Object reg) {
        this.rule = rule;
        this.reg = reg;
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
