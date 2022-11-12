/*
 * Copyright 2022 okome.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.siisise.bnf.parser;

import net.siisise.block.ReadableBlock;
import net.siisise.bnf.BNF;
import net.siisise.bnf.BNFReg;

/**
 * subrulenamesで指定した名の部分を中間型で受け取り、変換後の型で渡すだけのお仕事にするための部分実装。
 *
 * @param <T> 変換後の型
 * @param <M> 中間型
 */
public class BNFBuildParser<T, M> extends BNFBaseParser<T> {

    /** ABNF Parser側 名前空間 */
    private final BNFReg base;
    private BNFParser<? extends M>[] subs;
    protected final String[] subName;

    /**
     * 自動
     * @param rule ルールBNF
     * @param base BNF名前空間Reg
     * @param subrulenames サブ要素
     */
    protected BNFBuildParser(BNF rule, BNFReg base, String... subrulenames) {
        super(rule);
        this.base = base;
        subName = subrulenames;
    }

    @Override
    public T parse(ReadableBlock rb) {
        return parse(rb, null);
    }
    
    /**
     * 対象であるかの判定と要素抽出をする
     * @param rb 解析対象
     * @param ns user name space ユーザ名前空間
     * @return 解析結果
     */
    @Override
    public T parse(ReadableBlock rb, Object ns) {
        if (subs == null) {
            subs = new BNFParser[subName.length];
            for (int i = 0; i < subName.length; i++) {
                subs[i] = base.parser(subName[i]);
            }
        }
        BNF.C<M> re = rule.find(rb, ns, subs);
        if (re == null) {
            return null;
        }
        return build(re, ns);
    }

    /**
     * parse処理後の処理.
     * parse処理後
     * ns付きまたはnsなしどちらかを実装すればいい。
     * subName を参照してsrcからサブ要素を取り出せたりする雑な形。
     * @param src 解析対象
     * @param ns user name space 名前空間
     * @return 解析結果
     */
    protected T build(BNF.C<M> src, Object ns) {
        return build(src);
    }

    protected T build(BNF.C<M> src) {
        throw new java.lang.UnsupportedOperationException();
    }
}
