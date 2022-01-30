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
package net.siisise.abnf.parser;

import java.nio.charset.StandardCharsets;
import net.siisise.abnf.ABNF;
import net.siisise.abnf.AbstractABNF;
import net.siisise.io.FrontPacket;

/**
 * ABNF パース後の変換処理。
 * ABNFに一致したときに処理が走るので基本的に例外返しがない。
 * 
 * @param <T> 戻り型
 * @param <M> 中間形式
 */
public abstract class ABNFBaseParser<T, M> implements ABNFParser<T> {

    protected final ABNF rule;

    /**
     * 上のparserから駆動される想定
     * @param rule 処理対象のABNF rule
     */
    protected ABNFBaseParser(ABNF rule) {
        this.rule = rule;
    }

    @Override
    public ABNF getBNF() {
        return rule;
    }

    /**
     * 入り口
     * @param str 解析対象文字列
     * @return 変換されたデータ 不一致の場合はnull
     */
    @Override
    public T parse(String str) {
        return parse(AbstractABNF.pac(str));
    }
    
    @Override
    public <N> T parse(String str, N ns) {
        return parse(AbstractABNF.pac(str), ns);
    }

    @Override
    public <N> T parse(FrontPacket pac, N ns) {
        return parse(pac);
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
