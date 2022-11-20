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
package net.siisise.abnf;

import net.siisise.abnf.parser5234.ABNF5234;
import net.siisise.bnf.BNFReg;

/**
 * CC は parse担当.
 * BNFReg と BNFCCに役割分担をする
 * 自己定義があるので分割できないかもしれない. 
 */
public class ABNFCC extends ABNFReg {
    
    public ABNFCC(BNFReg up) {
        this(up, ABNF5234.REG);
    }
    
    public ABNFCC(BNFReg up, ABNFCC cc) {
        super(up, cc, "rulelist", "rule", "rulename", "elements");
    }
    
}
