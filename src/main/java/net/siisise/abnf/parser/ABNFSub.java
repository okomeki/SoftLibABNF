package net.siisise.abnf.parser;

import java.util.List;
import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;

/**
 * 要素を抽出するだけ
 * @param <T> 解析後抽出できる型
 */
public class ABNFSub<T> extends ABNFList<T, T> {

    /**
     * 
     * @param rule 処理対象のABNF構文
     * @param reg 名前空間参照用
     * @param base Parser駆動用
     * @param subcn 抽出する内側のABNF要素の名
     */
    protected ABNFSub(ABNF rule, ABNFReg reg, ABNFReg base, String subcn) {
        super(rule, reg, base, subcn);
    }

    /**
     * 
     * @param val 抽出された内側のABNF要素一覧
     * @return 処理結果。型は任意
     */
    @Override
    protected T parse(List<T> val) {
        return val.get(0);
    }

}
