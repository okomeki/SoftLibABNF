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

import java.util.ArrayList;
import java.util.List;
import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;

/**
 * リスト系のものを一度まとめてからパース後特定型にして返す.
 * @param <T> 最終形式 抽出型
 * @param <M> 要素の形式
 */
public abstract class ABNFList<T, M> extends ABNFBuildParser<T, M> {

    /**
     * ABNF系で自動的に呼ばれる形のコンストラクタ。
     * 継承する場合はそのままの形で
     * @param rule 処理対象のABNF構文
     * @param base subrulenameのParser駆動用
     * @param subrulenames 抽出するサブ要素
     */
    protected ABNFList(ABNF rule, ABNFReg base, String... subrulenames) {
        super(rule, base, subrulenames);
    }

    /**
     * サブ要素だけ抽出したリストをつくりbuild(List)へ渡す変換処理。
     * @param sret サブ要素のリスト
     * @return 処理結果
     */
    @Override
    protected <N> T build(ABNF.C<M> sret, N ns) {
        List<M> mlist = new ArrayList<>();
        for ( String sub : subName ) {
            List<M> s = sret.get(sub);
            if ( s != null ) {
                mlist.addAll(s);
            }
        }
        return build(mlist, ns);
    }
    
    protected <N> T build(List<M> val, N ns) {
        return build(val);
    }

    /**
     * 構築過程
     * @param val nullなし 並び順はsubcn順の中で並び順になるので注意
     * @return 処理結果
     */
    protected abstract T build(List<M> val);
}
