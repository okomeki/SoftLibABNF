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
package net.siisise.bnf;

import net.siisise.block.ReadableBlock;
import net.siisise.bnf.parser.BNFParser;

/**
 * 完全一致検索。
 * 重いかもしれない
 * example = *( a / b ) a に対応したもの
 */
public class BNFplm extends BNFpl {

    public BNFplm(BNF[] list) {
        super(list);
    }

    /**
     * 複製する.
     * @param reg 複製先
     * @return 複製
     */
    @Override
    public BNFpl copy(BNFReg reg) {
        BNF[] l = new BNF[list.length];

        for (int i = 0; i < list.length; i++) {
            l[i] = this.list[i].copy(reg);
        }
        return new BNFplm(l);
    }

    @Override
    public <X> Match<X> buildFind(ReadableBlock pac, Object ns, BNFParser<? extends X>... subps) {
        return longfind(pac, ns, 0, subps);
    }

    /**
     * 詳細検索
     *
     * @param <X> 戻り型例
     * @param pac source 解析対象
     * @param ns user name space
     * @param start
     * @param subparsers サブ要素パーサ
     * @return ざっくり戻り
     */
    protected <X> Match<X> longfind(ReadableBlock pac, Object ns, int start, BNFParser<? extends X>[] subparsers) {
        if (list.length == start) {
            return new Match(pac).end(pac);
        }
        long frontMax = pac.length();
        long of = pac.backLength();

        do {
            // 1つめ 指定サイズまでに制限する
            ReadableBlock frontPac = pac.readBlock(frontMax);

            Match<X> firstret = list[start].find(frontPac, ns, subparsers);
            pac.back(frontPac.length());
            if ( firstret == null ) {
                return null;
            }
            firstret.st = of;
            firstret.end(pac);
            if (list.length - start == 1) { // 一致しないか最後ならここで戻り
                return firstret;
            }
            frontMax = pac.backLength() - of;
            // 2つめ以降
            Match<X> nextret = longfind(pac, ns, start + 1, subparsers);
            if (nextret != null) {
                // firstret と nextret 両方成立
                mix(firstret, nextret);
                return firstret;
            }
            // scのみ成立 破棄
            pac.seek(of);
            frontMax--;

        } while (frontMax >= 0);
        return null;
    }
}
