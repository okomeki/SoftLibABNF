/*
 * Copyright 2021-2022 Siisise Net.
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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import net.siisise.abnf.parser5234.ABNF5234;
import net.siisise.block.ReadableBlock;
import net.siisise.bnf.BNFCC;
import net.siisise.bnf.BNFReg;
import net.siisise.bnf.parser.BNFParser;
import net.siisise.io.FrontPacket;
import net.siisise.io.StreamFrontPacket;

/**
 * ABNFの名前担当、Parserの機能もあり。
 * Namespace
 * BNFCCに役割分担できそうなのでBNFRegに統合してしまう計画.
 *
 * rule: 基本は RFC 5234 に準拠するが、一部改変したParserの対応も可能
 */
public class ABNFReg extends BNFCC<ABNF> {

    /**
     * 名前の参照を先に済ませる
     */
    public class ABNFRef extends AbstractABNF {

        ABNFRef(String rulename) {
            this.name = rulename;
        }

        @Override
        public ReadableBlock is(ReadableBlock src) {
            return reg.get(name).is(src);
        }
        
        @Override
        public ReadableBlock is(ReadableBlock src, Object ns) {
            return reg.get(name).is(src, ns);
        }

        @Override
        public <X> Match<X> find(ReadableBlock pac, Object ns, BNFParser<? extends X>... parsers) {
            return reg.get(name).find(pac, ns, parsers);
        }

        /**
         * 参照だけ複製する.
         *
         * @param reg 複製先
         * @return 複製
         */
        @Override
        public ABNF copy(BNFReg<ABNF> reg) {
            return reg.ref(name);
        }
        
        @Override
        public String toJava() {
            return "REG.ref(\"" + name + "\")";
        }
    }

    /**
     * 標準のABNF Parserを構築.
     */
    public ABNFReg() {
        this((BNFReg) null, ABNF5234.REG);
    }

    /**
     * ファイルに定義を書いておけばプログラム不要説(パーサは必要)。
     * ファイルではなくURLで渡すと幅が広がる
     *
     * @param url ABNF定義ファイルのURL
     * @param up 前提とする定義など継承もと, 参照先とか include元とか
     * @throws IOException 入力エラー全般
     */
    public ABNFReg(URL url, BNFReg up) throws IOException {
        this(up);
        rulelist(url);
    }

    /**
     * 名前空間作成.
     *
     * @param up 前提とする定義など継承もと, 参照先とか include元とか
     */
    public ABNFReg(BNFReg up) {
        this(up, ABNF5234.REG);
    }

    /**
     * ファイルに定義を書いておけばプログラム不要説(パーサは必要)。
     *
     * @param url ABNF定義ファイルのURL
     * @param up 前提とする定義など継承もと, 参照先とか include元とか
     * @param ruleParser ruleをparseするParserの種類 ABNF5234.REG,RFC 7405, RFC
     * @throws IOException
     */
    public ABNFReg(URL url, BNFReg up, BNFCC ruleParser) throws IOException {
        this(up, ruleParser);
        rulelist(url);
    }

    /**
     * 名前空間作成. いろいろ未定 up の定義を複製する HTTP7230では拡張の実験をしている
     * BNF Parserは作れない.
     * @param up 前提とする定義など継承もと
     * @param ruleParser ruleをparseするParserの種類 ABNF5234.REG,RFC 7405, RFC
     * 7230など微妙に違うとき。利用しないときのみ省略したい
     */
    public ABNFReg(BNFReg up, BNFCC ruleParser) {
        super(up, ruleParser, null, null, null, null);
    }

    /**
     * BNF Parser系を定義する場合に利用する
     * BNF parser 以外は rulelist, rule, rulename, elements が不要
     *
     * @param up 前提とする定義など継承もと, 参照先とか include元とか
     * @param ruleParser ruleの解析に使うBNFの実装 Javaのみで組む場合はnullも可
     * @param rulelist rulelist として使用する BNF name
     * @param rule rule BNF name
     * @param rulename rulename BNF name
     * @param elements elements BNF name
     */
    public ABNFReg(BNFReg up, BNFCC ruleParser, String rulelist, String rule, String rulename, String elements) {
        super(up, ruleParser, rulelist, rule, rulename, elements);
    }

    /**
     * 参照リンク優先。
     * 間接参照のため、あとの定義でいろいろ変わるときに便利。
     *
     * @param rulename rulename
     * @return rulenameへの参照
     */
    @Override
    public ABNF ref(String rulename) {
        return new ABNFRef(rulename);
    }

    /**
     * 直リンク優先 差し替えが困難
     * rulenameに該当するものがない場合は参照を返す。
     *
     * @param rulename rulename
     * @return REGに登録されているrulenameの値
     */
    @Override
    public ABNF href(String rulename) {
        ABNF bnf = (ABNF) reg.get(rulename);
        if (bnf == null) {
            bnf = new ABNFRef(rulename);
        }
        return bnf;
    }

    /**
     * rulelistの一括読み込みと登録。
     * パースされ、regに登録します。
     * RFC 5234の場合、文字コードUTF-8、改行コード CR LF のみ有効です。
     *
     * @param rulelist abnf一覧
     * @return ABNF化された一覧
     */
    public List<ABNF> rulelist(String rulelist) {
        List<ABNF> list = bnfReg.parse(bnfReg.rulelist, rulelist, this);
        if (list != null) {
            list.forEach((abnf) -> {
                reg.put(abnf.getName(), abnf);
            });
        }
        return list;
    }

    /**
     * rulelistの読み込みと登録。
     * RFC 5234の場合、文字コードUTF-8、改行コード CR LF のみ有効です。
     *
     * @param rulelist 元になるストリーム、Packet
     * @return rule の List
     */
    public List<ABNF> rulelist(FrontPacket rulelist) {
        List<ABNF> list = bnfReg.parse("rulelist", rulelist, this);
        if (list != null) {
            list.forEach((abnf) -> {
                reg.put(abnf.getName(), abnf);
            });
        }
        return list;
    }

    /**
     * rulelistの読み込みと登録。
     * RFC 5234の場合、文字コードUTF-8、改行コード CR LF のみ有効です。
     *
     * @param rulelist 元になるストリーム、Packet
     * @return rule の List
     */
    public List<ABNF> rulelist(ReadableBlock rulelist) {
        List<ABNF> list = bnfReg.parse("rulelist", rulelist, this);
        if (list != null) {
            list.forEach((abnf) -> {
                reg.put(abnf.getName(), abnf);
            });
        }
        return list;
    }

    /**
     * テキストからのabnf一覧一括読み込み。
     * ファイルだと狭いのでURLとした。
     * regに登録します。
     * RFC 5234の場合、文字コードUTF-8、改行コード CR LF のみ有効です。
     *
     * @param url abnf一覧のテキストが存在するURL
     * @return 解析されたABNF一覧
     * @throws IOException 読み込みException系
     */
    public List<ABNF> rulelist(URL url) throws IOException {
        InputStream in = url.openStream();
        List<ABNF> rl = rulelist(new StreamFrontPacket(in));
        in.close();
        return rl;
    }

    String javaLine(String ruleName, String regName, ABNFrule rule) {
        StringBuilder src = new StringBuilder();
            src.append("\r\n    static final ABNF ");
            src.append(ruleName.replace('-', '_')).append(" = ").append(regName);
            src.append(".rule(\"").append(ruleName).append("\",");
            src.append(rule.bnf.toJava());
            src.append(");");
        return src.toString();
    }

    /**
     * Java っぽいコードを出力する.
     * @param regName 静的変数名 REG
     * @return Javaっぽいなにか
     */
    @Override
    public String toJava(String regName) {
        StringBuilder src = new StringBuilder();
        src.append("class Example {");
        src.append("\r\n    static final ABNFReg ").append(regName).append(" = new ABNFReg();");
        src.append("\r\n");
        
        for (String ruleName : reg.keySet() ) {
            ABNFrule bnf = (ABNFrule)reg.get(ruleName);
            src.append(javaLine(ruleName, regName, bnf));
        }
        src.append("\r\n}");
        
        return src.toString();
    }
}
