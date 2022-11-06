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
 *
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
    public <X,N> C<X> buildFind(ReadableBlock pac, N ns, BNFParser<? extends X>... subps) {
        return longfind(pac, ns, 0, subps);
    }

    /**
     * 詳細検索
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
            return new C();
        }
        int flen = pac.size();
        
        do {
            // 1つめ 指定サイズまでに制限する
            ReadableBlock frontPac = pac.readBlock(flen);

            C firstret = list[start].find(frontPac, ns, subparsers);
            pac.back(frontPac.size());

            if (firstret == null || list.length - start == 1) { // 一致しないか最後ならここで戻り
                return firstret;
            }
            flen = firstret.ret.size();
            // 2つめ以降
            C nextret = longfind(pac, ns, start + 1, subparsers);
            if (nextret != null) {
                // firstret と nextret 両方成立
                mix(firstret, nextret);
                return firstret;
            }
            // scのみ成立 破棄
            pac.back(firstret.ret.size());
            flen--;

        } while (flen >= 0);
        return null;
    }
}
