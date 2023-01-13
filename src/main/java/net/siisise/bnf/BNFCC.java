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

import net.siisise.abnf.ABNF;
import net.siisise.abnf.parser5234.ABNF5234;

/**
 * REGとCCの分離実験
 * @param <B>
 */
public class BNFCC<B extends BNF> extends BNFReg<B> {
    
    public final String rulelist;
    public final String rule;
    protected final String rulename;
    public final String elements;

    /**
     * BNF Parser系を定義する場合に利用する
     * 
     * @param up 前提とする定義など継承もと, 参照先とか include元とか
     * @param cc ruleの解析に使うBNFの実装 Javaのみで組む場合はnullも可
     * @param rulelist rulelist として使用する BNF name
     * @param rule rule BNF name
     * @param rulename rulename BNF name
     * @param elements elements BNF name
     */
    protected BNFCC(BNFReg<B> up, BNFCC<B> cc, String rulelist, String rule, String rulename, String elements) {
        super(up, cc);
        this.rulelist = rulelist;
        this.rule = rule;
        this.rulename = rulename;
        this.elements = elements;
    }

    boolean isRulename(String name) {
        return reg.get(rulename).eq(name);
    }

    public static BNFCC<ABNF> abnf(BNFReg<ABNF> up) {
        return abnf(up, ABNF5234.REG);
    }
    
    public static BNFCC<BNF> abnf2bnf(BNFReg<BNF> up) {
        return abnf(up, net.siisise.bnf.parser5234.ABNF5234.REG);
    }

    /**
     * ABNF用
     * @param <E> BNFが無難
     * @param up 取り込む定義
     * @param cc ABNF5234.REG など
     * @return ABNFが使えるよ
     */
    public static <E extends BNF> BNFCC<E> abnf(BNFReg<E> up, BNFCC<E> cc) {
        return new BNFCC<>(up, cc, "rulelist", "rule", "rulename", "elements");
    }
}
