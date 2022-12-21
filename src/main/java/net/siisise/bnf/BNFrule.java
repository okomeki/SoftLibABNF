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
 * 名前専用にする
 * @param <B> 適度にBNF固定
 */
public class BNFrule<B extends BNF> extends AbstractBNF<B> {
    
    B bnf;
    
    public BNFrule(String name, B elements) {
        this.name = name;
        bnf = elements;
    }

    @Override
    public ReadableBlock is(ReadableBlock src, Object ns) {
        return bnf.is(src,ns);
    }

    @Override
    public ReadableBlock is(ReadableBlock src) {
        return bnf.is(src);
    }

    @Override
    public <X> Match<X> find(ReadableBlock pac, Object ns, BNFParser<? extends X>... parsers) {
        BNFParser mp = matchParser(parsers);
        Match<X> mc = bnf.find(pac, ns, parsers);
        if ( mc != null ) {
            subBuild(mc,ns,mp);
        }
        return mc;
    }

    @Override
    public B copy(BNFReg reg) {
        return (B)new BNFrule(name, bnf.copy(reg));
    }

    @Override
    public String toJava() {
        return name;// + " = " + bnf.toJava();
    }

    public String toJavaLine() {
        return ".rule(\"" + name + "\"," + bnf.toJava() + ")";
    }
}
