/*
 * Copyright 2022 Siisise Net.
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
package net.siisise.ebnf;

import net.siisise.bnf.AbstractBNF;
import net.siisise.bnf.BNF;
import net.siisise.bnf.BNFReg;
import net.siisise.bnf.parser.BNFParser;
import net.siisise.io.FrontPacket;

/**
 *
 */
public abstract class AbstractEBNF extends AbstractBNF implements EBNF {

    /**
     * 名前、またはEBNFに近いもの (まだ抜けもある)
     */
    protected String name;

    @Override
    public <X, N> C<X> find(FrontPacket pac, N ns, BNFParser<? extends X>... parsers) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public EBNF copy(EBNFReg reg) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    @Override
    public BNF copy(BNFReg reg) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public EBNF c() {
        return x(0, 1);
    }

    @Override
    public EBNF x() {
        return x(0, -1);
    }

    @Override
    public EBNF ix() {
        return x(1, -1);
    }

    @Override
    public EBNF x(int min, int max) {
        return new EBNFx(min, max, this);
    }

    @Override
    public EBNF pl(BNF... ebnfs) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public EBNF or(BNF... val) {
        BNF[] list = new BNF[val.length + 1];
        list[0] = this;
        System.arraycopy(val, 0, list, 1, val.length);
        return new EBNFor(list);
    }

    @Override
    public EBNF mn(EBNF bnf) {
        return new EBNFmn(this,bnf);
    }
    
    protected String hex(int ch) {
        if (ch < 0x100) {
            return "%x" + Integer.toHexString(0x100 + ch).substring(1);
        } else if (ch < 0x10000) {
            return "%x" + Integer.toHexString(0x10000 + ch).substring(1);
        } else {
            return "%x" + Integer.toHexString(0x1000000 + ch).substring(1);
        }
    }

    /**
     * 結果2を1に混ぜる
     * @param <X> 戻り型
     * @param ret 結果1
     * @param sub 結果2
     */
    static <X> void mix(C<X> ret, C<X> sub) {
        ret.ret.write(sub.ret.toByteArray());
        sub.subs.forEach((key,val) -> {
            val.forEach((v) -> {
                ret.add(key, v);
            });
        });
    }
}
