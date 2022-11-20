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
public class BNFpl extends FindBNF {

    protected final BNF[] list;

    public BNFpl(BNF... bnfs) {
        list = bnfs;
        StringBuilder names = new StringBuilder();
        for (BNF abnf : list) {
            names.append(abnf.getName());
            names.append(" ");
        }
        names.deleteCharAt(names.length() - 1);
        name = "( " + names.toString() + " )";
    }

    /**
     * 
     * @param <X> 返却予定の型
     * @param pac 解析対象
     * @param ns user name space
     * @param parsers サブ解析装置
     * @return サブ要素を含む解析結果
     */
    @Override
    public <X> Match<X> buildFind(ReadableBlock pac, Object ns, BNFParser<? extends X>... parsers) {
        Match<X> ret = new Match<>(pac);
        
        for (BNF sub : list) {
            Match<X> subret = sub.find(pac, ns, parsers);
            if (subret == null) {
                pac.back(pac.backLength() - ret.st);
                return null;
            }
            mix(ret, subret);
        }
        return ret;
    }

    @Override
    public BNF copy(BNFReg reg) {
        BNF[] cplist = new BNF[list.length];

        for (int i = 0; i < list.length; i++) {
            cplist[i] = list[i].copy(reg);
        }
        return new BNFpl(cplist);
    }

    
}
