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
package net.siisise.abnf;

import net.siisise.bnf.AbstractBNF;
import net.siisise.bnf.BNF;
import net.siisise.bnf.BNFReg;

/**
 * 簡単なプレ実装
 */
public abstract class AbstractABNF extends AbstractBNF<ABNF> implements ABNF {

    @Override
    public ABNF name(String name) {
        return new ABNFor(name, this); // ?
    }

    /**
     * Concatenation の単純版
     * *(a / b ) a の様なものが解析できないが速そう
     * @param val plus  する ABNF列
     * @return 簡易に比較するABNF
     */
    @Override
    public ABNF pl(BNF... val) {
        if (val.length == 0) {
            return this;
        }
        BNF[] list = new BNF[val.length + 1];
        list[0] = this;
        System.arraycopy(val, 0, list, 1, val.length);
        return new ABNFpl(list);
    }

    /**
     * やや厳密に対応する足し算版
     * *( a / b ) a にも対応
     * @param val plus する ABNF列
     * @return 厳密に比較できるABNF
     */
    @Override
    public ABNF plm(BNF... val) {
        if (val.length == 0) {
            return this;
        }
        ABNF[] list = new ABNF[val.length + 1];
        list[0] = this;
        System.arraycopy(val, 0, list, 1, val.length);
        return new ABNFplm(list);
    }

    /**
     * Unicode単位で比較する若干速いのかもしれない版 plm
     * @param val plus する ABNF列
     * @return unicodeで比較されるABNF処理
     */
    @Override
    public ABNF plu(BNF... val) {
        if (val.length == 0) {
            return this;
        }
        BNF[] list = new BNF[val.length + 1];
        list[0] = this;
        System.arraycopy(val, 0, list, 1, val.length);
        return new ABNFplu(list);
    }
    
    @Override
    public ABNF mn(BNF val) {
        return new ABNFmn(this, val);
    }

    @Override
    public ABNF or(BNF... val) {
        BNF[] list = new BNF[val.length + 1];
        list[0] = this;
        System.arraycopy(val, 0, list, 1, val.length);
        return new ABNFor(list);
    }

    @Override
    public ABNF x(int min, int max) {
        return new ABNFx(min, max, this);
    }

    @Override
    public ABNF copy(BNFReg reg) {
        return copy((ABNFReg)reg);
    }

}
