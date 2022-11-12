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
    public ABNFpl copy(ABNFReg reg) {
        BNF[] l = new BNF[list.length];

        for (int i = 0; i < list.length; i++) {
            l[i] = this.list[i].copy(reg);
        }
        return new ABNFplm(l);
    }

    @Override
    public <X,N> C<X> buildFind(ReadableBlock pac, N ns, BNFParser<? extends X>... subps) {
        return longfind(pac, ns, 0, subps);
    }

    /**
     * puls用 最長一致詳細検索.
     *
     * @param <X> 戻り型例
     * @param <N> name space type 名前空間型
     * @param pac source 解析対象
     * @param ns user name space
     * @param start
     * @param subparsers サブ要素パーサ
     * @return ざっくり戻り
     */
    protected <X,N> C<X> longfind(ReadableBlock pac, N ns, int start, BNFParser<? extends X>[] subparsers) {
        if (list.length == start) {
            return new C(pac);
        }
        // start番目の最大長 (候補) 全体が一致するまで縮めていく
        long frontMax = pac.length();
        long op = pac.backLength();
        do {
            // 1つめ 指定サイズまでに制限する
            // 減らしながら全体が一致する箇所を探る
            ReadableBlock frontPac = pac.readBlock(frontMax);

            C firstret = list[start].find(frontPac, ns, subparsers);
            pac.back(frontPac.length()); // 残りをpacのpositionに戻す
            if ( firstret == null ) {
                return null;
            }
            
            firstret.st = op;
            //firstret.end(pac);
            if ( list.length - start == 1) { // 一致しないか最後ならここで戻り
                return firstret;
            }
            frontMax = pac.backLength() - op; // 単独の最大なので切り詰める
            // 2つめ以降
            C nextret = longfind(pac, ns, start + 1, subparsers);
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
