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
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.List;
import net.siisise.abnf.parser5234.ABNF5234;
import net.siisise.bnf.BNF;
import net.siisise.bnf.BNFCC;
import net.siisise.bnf.BNFReg;
import net.siisise.bnf.parser.BNFPacketParser;
import net.siisise.bnf.parser.BNFParser;
import net.siisise.io.FrontPacket;
import net.siisise.io.StreamFrontPacket;

/**
 * ABNFの名前担当、Parserの機能もあり。
 * Namespace
 *
 * rule: 基本は RFC 5234 に準拠するが、一部改変したParserの対応も可能
 */
public class ABNFReg extends BNFCC {

    /**
     * 名前の参照を先に済ませる
     */
    public class ABNFRef extends FindABNF {

        ABNFRef(String rulename) {
            this.name = rulename;
        }

        @Override
        protected <X,N> C<X> buildFind(FrontPacket pac, N ns, BNFParser<? extends X>... parsers) {
            return reg.get(name).find(pac, ns, parsers);
        }

        /**
         * 複製する.
         *
         * @param reg 複製先
         * @return 複製
         */
        @Override
        public ABNF copy(ABNFReg reg) {
            return reg.ref(name);
        }

    }

    public ABNFReg() {
        this((BNFReg) null, ABNF5234.REG);
    }

    /**
     * 名前空間作成. いろいろ未定 up の定義を複製する HTTP7230では拡張の実験をしている
     *
     * @param up 前提とする定義など継承もと
     * @param bnfParser ruleをparseするParserの種類 ABNF5234.REG,RFC 7405, RFC
     * 7230など微妙に違うとき。利用しないときのみ省略したい
     */
    public ABNFReg(BNFReg up, BNFCC bnfParser) {
        super(up, bnfParser, null, null, null, null);
    }

    /**
     * BNF Parser系を定義する場合に利用する
     *
     * @param up
     * @param bnfParser
     * @param rulelist rulelist BNF name
     * @param rule rule BNF name
     * @param rulename rulename BNF name
     * @param elements elements BNF name
     */
    public ABNFReg(BNFReg up, BNFCC bnfParser, String rulelist, String rule, String rulename, String elements) {
        super(up, bnfParser, rulelist, rule, rulename, elements);
    }

    /**
     * 名前空間作成.
     *
     * @param up 前提とする定義など継承もと
     */
    public ABNFReg(BNFReg up) {
        this(up, ABNF5234.REG);
    }

    /**
     *
     * @param url
     * @param up
     * @param exParser
     * @throws IOException
     */
    public ABNFReg(URL url, BNFReg up, BNFCC exParser) throws IOException {
        this(up, exParser);
        rulelist(url);
    }

    /**
     * ファイルに定義を書いておけばプログラム不要説(パーサは必要)。
     * ファイルではなくURLで渡すと幅が広がる
     *
     * @param url ABNF定義ファイルのURL
     * @param up 前提とする定義など継承もと
     * @throws IOException 入力エラー全般
     */
    public ABNFReg(URL url, BNFReg up) throws IOException {
        this(up);
        rulelist(url);
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
    public ABNF href(String rulename) {
        ABNF bnf = (ABNF) reg.get(rulename);
        if (bnf == null) {
            bnf = new ABNFRef(rulename);
        }
        return bnf;
    }

    /**
     * rule 1行のパース. 最後の改行は省略可能
     *
     * @param rule name = value 改行を省略可能に改変している
     * @return rule 1行をABNFにしたもの
     */
    @Override
    public ABNF rule(String rule) {
        ABNF abnf = bnfReg.parse(bnfReg.rule, rule + "\r\n", this);
        return rule(abnf.getName(), abnf);
    }

    /**
     * ABNFをパースする。
     * 名前とelementsを個別に渡せると何かと楽かもしれないと思うので作った。
     * ToDo: ABNF5234 へ
     *
     * @param rulename ABNF構文の名
     * @param elements ABNF式
     * @return 解析されたABNF
     */
    public ABNF rule(String rulename, String elements) {
        return rule(rulename, elements(elements));
    }

    @Override
    public ABNF elements(String elements) {
        return (ABNF) bnfReg.parse(bnfReg.elements, elements, this);
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
        list.forEach((abnf) -> {
            reg.put(abnf.getName(), abnf);
        });
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
        if (list == null) {
            return null;
        }
        list.forEach((abnf) -> {
            reg.put(abnf.getName(), abnf);
        });
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

    /**
     * 特殊なので使わない方がいい。
     *
     * @deprecated なくなるかも
     * @param pac 解析対象
     * @param rulename rulename
     * @param subrulenames サブ要素rulename
     * @return 仮型
     */
    public ABNF.C find(FrontPacket pac, String rulename, String... subrulenames) {
        ABNF rule = href(rulename);

        BNFParser[] cll = new BNFParser[subrulenames.length];
        for (int i = 0; i < subrulenames.length; i++) {
            cll[i] = parser(subrulenames[i]);
        }
        return rule.find(pac, cll);
    }

    /**
     * ruleで指定されたclassを基にしてParserを生成する.
     *
     * @param <T> Parserが返す解析型
     * @param rulename 解析装置付き構文の名。駆動コマンドのようなもの
     * @return Parser実体
     */
    @Override
    public <T> BNFParser<T> parser(String rulename) {
        ABNF rule = (ABNF) reg.get(rulename);
        Class<? extends BNFParser> rulep = CL.get(rulename);
        if (rulep == null) {
            return (BNFParser<T>) new BNFPacketParser(rule);
        }
        try {
            Constructor<? extends BNFParser> cnst;
            cnst = (Constructor<? extends BNFParser<T>>) rulep.getConstructor(BNF.class, BNFReg.class);
            return cnst.newInstance(rule, this);
        } catch (NoSuchMethodException ex) {
/*
            try {
                Constructor<? extends BNFParser> cnst;
                cnst = (Constructor<? extends BNFParser<T>>) rulep.getConstructor(BNF.class, BNFReg.class);
                return cnst.newInstance(rule, this);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException ex2) {
                throw new java.lang.UnsupportedOperationException(ex2);
            }
*/
            throw new java.lang.UnsupportedOperationException(ex);
        } catch (SecurityException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException ex) {
            throw new java.lang.UnsupportedOperationException(ex);
        }
    }
}
