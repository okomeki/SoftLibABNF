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

import net.siisise.abnf.ABNF;
import net.siisise.abnf.ABNFReg;
import net.siisise.block.ReadableBlock;
import net.siisise.bnf.BNF;
import net.siisise.bnf.BNFReg;
import net.siisise.bnf.parser.BNFBaseParser;

/**
 * ルール名.
 * ルールへの参照として機能する
 */
public class Rulename extends BNFBaseParser<ABNF> {

    public Rulename(BNF rule, BNFReg base) {
        super(rule);
    }

    /**
     * 
     * @param pac データ
     * @param ns 名前空間
     * @return 名前で指定されたルールへの参照
     */
    @Override
    public ABNF parse(ReadableBlock pac, Object ns) {
        ReadableBlock name = rule.is(pac);
        if (name == null) {
            return null;
        }
        return ((ABNFReg)ns).ref(str(name));
    }

    @Override
    public ABNF parse(ReadableBlock pac) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
