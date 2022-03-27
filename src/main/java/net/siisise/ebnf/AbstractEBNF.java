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

/**
 *
 */
public abstract class AbstractEBNF extends AbstractBNF<EBNF> implements EBNF {

    /**
     * 名前、またはEBNFに近いもの (まだ抜けもある)
     */
    protected String name;
    
    @Override
    public EBNF name(String name) {
        return new EBNFor(name,this);
    }

     @Override
    public EBNF copy(BNFReg reg) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public EBNF x(int min, int max) {
        return new EBNFx(min, max, this);
    }

    @Override
    public EBNF pl(BNF... ebnfs) {
        return new EBNFpl(ebnfs);
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
    
    @Override
    protected String hex(int ch) {
        if (ch < 0x100) {
            return "%x" + Integer.toHexString(0x100 + ch).substring(1);
        } else if (ch < 0x10000) {
            return "%x" + Integer.toHexString(0x10000 + ch).substring(1);
        } else {
            return "%x" + Integer.toHexString(0x1000000 + ch).substring(1);
        }
    }

}
