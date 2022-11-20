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

import java.util.List;
import net.siisise.block.ReadableBlock;
import net.siisise.bnf.BNF;
import net.siisise.bnf.BNFReg;

/**
 * 一致するものを選択する (分岐).
 * 含まれている要素(名前)なので全体一致ではなくていい
 * @param <T> 中間型、戻り型
 */
public class BNFSelect<T> extends BNFBuildParser<T, T> {

    protected BNFSelect(BNF rule, BNFReg base, String...  casenames) {
        super(rule, base, casenames);
    }

    /**
     * select っぽいことをする。
     * casenames 内に該当するものがあればそれを返す なければ otherに振る
     * @param src 解析済みデータ列
     * @return 処理結果
     */
    @Override
    protected T build(BNF.Match<T> src, Object ns) {
        for (String sub : subName) {
            List<T> r = src.get(sub);
            if (r != null) {
                return r.get(0);
            }
        }
        return other(src.sub, ns);
    }
    
    protected <N> T other(ReadableBlock src, N ns) {
        return other(src);
    }

    /**
     * 拡張する猶予.
     * @param src source 解析対象
     * @return 処理結果
     */
    protected T other(ReadableBlock src) {
        return null;
    }

}

