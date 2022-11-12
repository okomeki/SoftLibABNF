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
package net.siisise.abnf.parser5234;

import java.nio.charset.StandardCharsets;
import net.siisise.abnf.ABNF;
import net.siisise.block.ReadableBlock;
import net.siisise.bnf.BNF;
import net.siisise.bnf.BNFReg;
import net.siisise.bnf.parser.BNFBaseParser;

/**
 * 文字.
 */
public class CharVal extends BNFBaseParser<ABNF> {

    /**
     * 文字
     * @param rule 文字のルール
     * @param base つかわない
     */
    public CharVal(BNF rule, BNFReg base) {
        super(rule);
    }

    @Override
    public ABNF parse(ReadableBlock pac) {
        ReadableBlock quoted = rule.is(pac);
        if (quoted == null) {
            return null;
        }
        ReadableBlock plane = quoted.sub(1,quoted.length() - 2);
        return ABNF.text(new String(plane.toByteArray(), StandardCharsets.UTF_8));
    }
}
