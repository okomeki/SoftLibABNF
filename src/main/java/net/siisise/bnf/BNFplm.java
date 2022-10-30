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

import net.siisise.bnf.parser.BNFParser;
import net.siisise.io.FrontPacket;
import net.siisise.io.Packet;
import net.siisise.io.PacketA;
import net.siisise.pac.ReadableBlock;

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
        return longfind(pac, ns, list, subps);
    }

    /**
     * 詳細検索
     *
     * @param <X> 戻り型例
     * @param <N> name space type 名前空間型
     * @param pac source 解析対象
     * @param ns user name space
     * @param list サブ要素
     * @param subparsers サブ要素パーサ
     * @return ざっくり戻り
     */
    protected <X,N> C<X> longfind(FrontPacket pac, N ns, BNF[] list, BNFParser<? extends X>[] subparsers) {
        return longfind(rb(pac),ns,list,subparsers);
        /*
        if (list.length == 0) {
            return new C();
        }
        int flen = pac.size();
        
        do {
            // 1つめ 指定サイズまでに制限する
            Packet frontPac = new PacketA();
            byte[] data = new byte[flen];
            pac.read(data, 0, flen);
            frontPac.write(data, 0, flen);
            C firstret = list[0].find(frontPac, ns, subparsers);
            pac.dbackWrite(frontPac.toByteArray());

            if (firstret == null || list.length == 1) { // 一致しないか最後ならここで戻り
                return firstret;
            }
            flen = firstret.ret.size();
            // 2つめ以降
            BNF[] slist = new BNF[list.length - 1];
            System.arraycopy(list, 1, slist, 0, slist.length);
            C nextret = longfind(pac, ns, slist, subparsers);
            if (nextret != null) {
                // firstret と nextret 両方成立
                mix(firstret, nextret);
                return firstret;
            }
            // scのみ成立 破棄
            byte[] sdata = firstret.ret.toByteArray();
            pac.dbackWrite(sdata);
            flen--;

        } while (flen >= 0);
        return null;
        */
    }

    /**
     * 詳細検索
     *
     * @param <X> 戻り型例
     * @param <N> name space type 名前空間型
     * @param pac source 解析対象
     * @param ns user name space
     * @param list サブ要素
     * @param subparsers サブ要素パーサ
     * @return ざっくり戻り
     */
    protected <X,N> C<X> longfind(ReadableBlock pac, N ns, BNF[] list, BNFParser<? extends X>[] subparsers) {
        if (list.length == 0) {
            return new C();
        }
        int flen = pac.size();
        
        do {
            // 1つめ 指定サイズまでに制限する
            Packet frontPac = new PacketA();
            byte[] data = new byte[flen];
            pac.read(data, 0, flen);
            frontPac.write(data, 0, flen);
            C firstret = list[0].find(frontPac, ns, subparsers);
            pac.back(frontPac.size());

            if (firstret == null || list.length == 1) { // 一致しないか最後ならここで戻り
                return firstret;
            }
            flen = firstret.ret.size();
            // 2つめ以降
            BNF[] slist = new BNF[list.length - 1];
            System.arraycopy(list, 1, slist, 0, slist.length);
            C nextret = longfind(pac, ns, slist, subparsers);
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
