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
package net.siisise.abnf;

import net.siisise.block.ReadableBlock;
import net.siisise.bnf.BNFReg;
import net.siisise.bnf.parser.BNFParser;

/**
 * ラベルを明確に分けておいた方が楽そう.
 */
public class ABNFrule extends AbstractABNF {

    ABNF bnf;
    
    public ABNFrule(String name, ABNF elements) {
        this.name = name;
        this.bnf = elements;
    }

    @Override
    public ReadableBlock is(ReadableBlock src, Object ns) {
        return bnf.is(src,ns);
    }

    @Override
    public ReadableBlock is(ReadableBlock src) {
        return bnf.is(src);
    }

    /**
     * ラベル判定処理
     * @param <X> 戻り型
     * @param rb ソース
     * @param ns 名前空間
     * @param parsers サブ要素 sub parser
     * @return ほどよい形
     */
    @Override
    public <X> Match<X> find(ReadableBlock rb, Object ns, BNFParser<? extends X>... parsers) {
        BNFParser<? extends X> mp = matchParser(parsers);
        Match<X> ret = bnf.find(rb, ns,mp == null ? parsers : new BNFParser[0]);
        if ( ret != null ) {
            subBuild(ret,ns,mp);
        }
        return ret;
    }

    @Override
    public ABNF copy(BNFReg<ABNF> reg) {
        return new ABNFrule(name, bnf.copy(reg));
    }

    @Override
    public String toJava() {
        return name.replace('-', '_');// + " = " + bnf.toJava();
    }
    
    public String toJavaLine() {
        return ".rule(\"" + name + "\"," + bnf.toJava() + ")";
    }
}
