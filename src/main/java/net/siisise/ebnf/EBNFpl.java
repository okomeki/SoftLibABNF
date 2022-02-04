/*
 * Copyright 2022 Siisise Net.
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
package net.siisise.ebnf;

import net.siisise.bnf.parser.BNFParser;
import net.siisise.io.FrontPacket;

/**
 *
 */
public class EBNFpl extends FindEBNF {

    protected final EBNF[] list;

    public EBNFpl(EBNF... abnfs) {
        list = abnfs;
        StringBuilder names = new StringBuilder();
        for (EBNF abnf : list) {
            names.append(abnf.getName());
            names.append(" ");
        }
        names.deleteCharAt(names.length() - 1);
        name = "( " + names.toString() + " )";
    }

    /**
     * 複製する
     * @param reg 複製先
     * @return 複製
     */
    @Override
    public EBNFpl copy(EBNFReg reg) {
        EBNF[] cplist = new EBNF[list.length];

        for (int i = 0; i < list.length; i++) {
            cplist[i] = list[i].copy(reg);
        }
        return new EBNFpl(cplist);
    }

    /**
     * 
     * @param <X> 返却予定の型
     * @param <N> user name space type ユーザ名前空間型
     * @param pac 解析対象
     * @param ns user name space
     * @param parsers サブ解析装置
     * @return サブ要素を含む解析結果
     */
    @Override
    public <X,N> C<X> buildFind(FrontPacket pac, N ns, BNFParser<? extends X>... parsers) {
        C<X> ret = new C<>();
        
        for (EBNF sub : list) {
            C<X> subret = sub.find(pac, ns, parsers);
            if (subret == null) {
                pac.dbackWrite(ret.ret.toByteArray());
                return null;
            }
            mix(ret, subret);
        }
        return ret;
    }

}
