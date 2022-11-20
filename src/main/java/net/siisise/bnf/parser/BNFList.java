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

import java.util.ArrayList;
import java.util.List;
import net.siisise.bnf.BNF;
import net.siisise.bnf.BNFReg;

/**
 * リスト系のものを一度まとめてからパース後特定型にして返す.
 * @param <T> 最終形式 抽出型
 * @param <M> 要素の形式
 */
public abstract class BNFList<T, M> extends BNFBuildParser<T, M> {

    /**
     * BNF系で自動的に呼ばれる形のコンストラクタ。
     * 継承する場合はそのままの形で
     * @param rule 処理対象のABNF構文
     * @param base subrulenameのParser駆動用
     * @param subrulenames 抽出するサブ要素
     */
    protected BNFList(BNF rule, BNFReg base, String... subrulenames) {
        super(rule, base, subrulenames);
    }

    /**
     * サブ要素だけ抽出したリストをつくりbuild(List)へ渡す変換処理。
     * @param sret サブ要素のリスト
     * @return 処理結果
     */
    @Override
    protected T build(BNF.Match<M> sret, Object ns) {
        List<M> mlist = new ArrayList<>();
        for ( String sub : subName ) {
            List<M> sublist = sret.get(sub);
            if ( sublist != null ) {
                mlist.addAll(sublist);
            }
        }
        return build(mlist, ns);
    }

    /**
     * 副ルールを抽出してリストで渡される処理 name space付きの場合
     * @param list 抽出結果
     * @param ns name space
     * @return 処理結果 
     */
    protected T build(List<M> list, Object ns) {
        return build(list);
    }

    /**
     * 構築過程
     * @param list nullなし 並び順はsubcn順の中で並び順になるので注意
     * @return 処理結果
     */
    protected abstract T build(List<M> list);
}
