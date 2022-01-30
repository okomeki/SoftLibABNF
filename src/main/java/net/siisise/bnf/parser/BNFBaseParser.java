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

import java.nio.charset.StandardCharsets;
import net.siisise.abnf.AbstractABNF;
import net.siisise.bnf.BNF;
import net.siisise.io.FrontPacket;

public abstract class BNFBaseParser<T> implements BNFParser<T> {
    
    protected BNF rule;

    /**
     * 上のparserから駆動される想定
     * @param rule 処理対象のABNF rule
     */
    protected BNFBaseParser(BNF rule) {
        this.rule = rule;
    }

    @Override
    public BNF getBNF() {
        return rule;
    }

    @Override
    public T parse(FrontPacket pac, Object ns) {
        return parse(pac);
    }

    @Override
    public T parse(String str, Object ns) {
        return parse(AbstractABNF.pac(str), ns);
    }

    /**
     * 入り口
     * @param src 解析対象文字列
     * @return 変換されたデータ 不一致の場合はnull
     */
    @Override
    public T parse(String src) {
        return parse(AbstractABNF.pac(src));
    }
    
    protected static String str(FrontPacket pac) {
        return new String(pac.toByteArray(), StandardCharsets.UTF_8);
    }

    protected static String strd(FrontPacket pac) {
        byte[] b = pac.toByteArray();
        pac.dbackWrite(b);
        return new String(b, StandardCharsets.UTF_8);
    }
}
