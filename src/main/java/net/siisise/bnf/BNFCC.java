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

/**
 * REGとCCの分離実験
 */
public class BNFCC extends BNFReg {
    
    public final String rulelist;
    public final String rule;
    protected final String rulename;
    public final String elements;

    /**
     * BNF Parser系を定義する場合に利用する
     * 
     * @param up 前提とする定義など継承もと, 参照先とか include元とか
     * @param cc ruleの解析に使うBNFの実装
     * @param rulelist rulelist として使用する BNF name
     * @param rule rule BNF name
     * @param rulename rulename BNF name
     * @param elements elements BNF name
     */
    protected BNFCC(BNFReg up, BNFCC cc, String rulelist, String rule, String rulename, String elements) {
        super(up, cc);
        this.rulelist = rulelist;
        this.rule = rule;
        this.rulename = rulename;
        this.elements = elements;
    }

    boolean isRulename(String name) {
        return reg.get(rulename).eq(name);
    }
}
