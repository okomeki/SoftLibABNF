/*
 * Copyright 2021 Siisise Net.
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
package net.siisise.abnf.parser;

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.io.FrontPacket;

/**
 * subnsで指定した名の部分を中間型で受け取り、変換後の型で渡すだけのお仕事にするための部分実装。
 *
 * @param <T> 変換後の型
 * @param <M> 中間型
 */
public abstract class ABNFBuildParser<T, M> extends ABNFBaseParser<T, M> {

    /** ABNF Parser側 名前空間 */
    private final ABNFReg base;
    private ABNFParser<? extends M>[] subs;
    protected final String[] subName;

    /**
     * 自動
     * @param rule ルールABNF
     * @param base ABNF名前空間Reg
     * @param subrulenames サブ要素
     */
    protected ABNFBuildParser(ABNF rule, ABNFReg base, String... subrulenames) {
        super(rule);
        this.base = base;
        subName = subrulenames;
    }
    
    @Override
    public T parse(FrontPacket pac) {
        return parse(pac, null);
    }

    /**
     * 対象であるかの判定と要素抽出をする
     * @param <N> user name space type
     * @param pac 解析対象
     * @param ns user name space ユーザ名前空間
     * @return 解析結果
     */
    @Override
    public <N> T parse(FrontPacket pac, N ns) {
        if (subs == null) {
            subs = new ABNFParser[subName.length];
            for (int i = 0; i < subName.length; i++) {
                subs[i] = base.parser(subName[i]);
            }
        }
        ABNF.C<M> val = rule.find(pac, ns, subs);
        if (val == null) {
            return null;
        }
        return build(val, ns);
    }

    /**
     * 
     * @param <N> user name space type
     * @param src 解析対象
     * @param ns user name space 名前空間
     * @return 解析結果
     */
    protected <N> T build(ABNF.C<M> src, N ns) {
        return build(src);
    }

    protected T build(ABNF.C<M> src) {
        throw new java.lang.UnsupportedOperationException();
    }
}
