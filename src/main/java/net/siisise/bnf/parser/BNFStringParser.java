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

import net.siisise.abnf.ABNF;
import net.siisise.bnf.BNF;
import net.siisise.bnf.BNFReg;

/**
 * 文字列を抽出するタイプ
 */
public class BNFStringParser extends BNFBuildParser<String,ABNF> {

    public BNFStringParser(BNF rule, BNFReg base) {
        super(rule, base);
    }

    /**
     * find専用なのでなにもしていない
     * @param str 解析結果
     * @return 構築結果
     */
    @Override
    public String build(ABNF.C str) {
        return str(str.sub);
    }
    
}

