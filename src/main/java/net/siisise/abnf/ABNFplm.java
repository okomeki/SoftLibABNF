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
package net.siisise.abnf;

import net.siisise.block.ReadableBlock;
import net.siisise.bnf.BNF;
import net.siisise.bnf.BNFReg;
import net.siisise.bnf.parser.BNFParser;

/**
 * 完全一致検索。
 * 重いかもしれない
 * example = *( a / b ) a に対応したもの
 *
 */
public class ABNFplm extends ABNFpl {

    public ABNFplm(BNF[] list) {
        super(list);
    }

    /**
     * 複製する.
     * @param reg 複製先
     * @return 複製
     */
    @Override
    public ABNFpl copy(BNFReg<ABNF> reg) {
        BNF[] l = new BNF[list.length];

        for (int i = 0; i < list.length; i++) {
            l[i] = this.list[i].copy(reg);
        }
        return new ABNFplm(l);
    }

    /**
     * 詳細検索.
     * @param <X> 戻り型例
     * @param pac source 解析対象
     * @param ns user name space
     * @param subparsers サブ要素パーサ
     * @return ざっくり戻り
     */
    @Override
    public <X> Match<X> buildFind(ReadableBlock pac, Object ns, BNFParser<? extends X>... subparsers) {
        return longfind(pac, ns, 0, subparsers);
    }

    /**
     * puls用 最長一致詳細検索.
     * buildFindからまたは自己で呼び出す想定
     *
     * @param <X> 戻り型例
     * @param pac source 解析対象
     * @param ns user name space
     * @param start listの開始位置 (内部用)
     * @param subparsers サブ要素パーサ
     * @return ざっくり戻り
     */
    protected <X> Match<X> longfind(ReadableBlock pac, Object ns, int start, BNFParser<? extends X>[] subparsers) {
        if (list.length == start) {
            return new Match(pac);
        }
        // start番目の最大長 (候補) 全体が一致するまで縮めていく
        long frontMax = pac.length();
        long op = pac.backLength();
        do {
            // 1つめ 指定サイズまでに制限する
            // 減らしながら全体が一致する箇所を探る
            ReadableBlock frontPac = pac.readBlock(frontMax);

            Match<X> firstret = list[start].find(frontPac, ns, subparsers);
            pac.back(frontPac.length()); // 残りをpacのpositionに戻す
            if ( firstret == null ) {
                return null;
            }
            
            firstret.st = op; // pac用に開始位置の調整
            if ( list.length - start == 1) { // 一致しないか最後ならここで戻り
                return firstret;
            }
            frontMax = pac.backLength() - op; // 単独の最大なので切り詰める
            // 2つめ以降
            Match nextret = longfind(pac, ns, start + 1, subparsers);
            if (nextret != null) {
                // firstret と nextret 両方成立
                mix(firstret, nextret);
                return firstret;
            }
            // scのみ成立 破棄
            pac.seek(op);
            frontMax--;

        } while (frontMax >= 0);
        return null;
    }
}
