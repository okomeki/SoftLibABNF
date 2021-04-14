package net.siisise.abnf;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.siisise.abnf.parser.ABNFParser;
import net.siisise.abnf.parser5234.ABNF5234;
import net.siisise.bnf.parser.BNFParser;
import net.siisise.io.FrontPacket;
import net.siisise.io.FrontPacketF;
import net.siisise.io.Packet;
import net.siisise.io.PacketA;

/**
 * ABNFの名前担当、Parserの機能もあり。
 * Namespace
 *
 * rule: 基本は RFC 5234 に準拠するが、一部改変したParserの対応も可能
 */
public class ABNFReg {

    Map<String, ABNF> reg = new HashMap<>();

    /**
     * rule, elements, rulelistが解釈できるABNFのparser
     * rulenameも?
     */
    private final ABNFReg parseReg;
    private final ABNF rn;

    public Map<String, Class<? extends ABNFParser>> CL = new HashMap<>();

    /**
     * 名前の参照を先に済ませる
     */
    public class ABNFRef extends FindABNF {

        ABNFRef(String rulename) {
            this.name = rulename;
        }

        @Override
        public C find(FrontPacket pac, BNFParser... parsers) {
            C ret = reg.get(name).find(pac, parsers);
            if (ret == null) {
                return null;
            }
            return sub(ret, parsers);
        }

        @Override
        public ABNF copy(ABNFReg reg) {
            return reg.ref(name);
        }

    }

    public ABNFReg() {
        parseReg = ABNF5234.REG;
        rn = ABNF5234.rulename;
        
    }

    /**
     * いろいろ未定
     * up の定義を複製する
     * HTTP7230では拡張の実験をしている
     *
     * @param up
     * @param exParser Parserの種類 ABNF5234.REG,RFC 7405, RFC 7230など微妙に違うとき。利用しないときのみ省略したい
     */
    public ABNFReg(ABNFReg up, ABNFReg exParser) {
        if (up != null) {
            //reg = new HashMap<>(up.reg); // 複製しておくのが簡単
            up.CL.keySet().forEach((key) -> {
                CL.put(key, up.CL.get(key));
            });
            up.reg.keySet().forEach((key) -> { // 循環参照対策が必要
                reg.put(key, up.reg.get(key).copy(this));
            });
        }
        parseReg = exParser;
        rn = ( exParser == null ) ? null : exParser.reg.get("rulename");
    }

    public ABNFReg(ABNFReg up) {
        this(up, ABNF5234.REG);
    }

    public ABNFReg(URL url, ABNFReg up, ABNFReg exParser) throws IOException {
        this(up, exParser);
        rulelist(url);
    }

    /**
     * ファイルに定義を書いておけばプログラム不要説(パーサは必要)。
     * ファイルではなくURLで渡すと幅が広がる
     *
     * @param url ABNF定義ファイルのURL
     * @param up 前提とする定義など
     * @throws IOException
     */
    public ABNFReg(URL url, ABNFReg up) throws IOException {
        this(up);
        rulelist(url);
    }

    /**
     * 参照リンク優先。
     * 間接参照のため、あとの定義でいろいろ変わるときに便利。
     *
     * @param rulename
     * @return rulenameへの参照
     */
    public ABNF ref(String rulename) {
        return new ABNFRef(rulename);
    }

    /**
     * 直リンク優先 差し替えが困難
     * rulenameに該当するものがない場合は参照を返す。
     *
     * @param rulename
     * @return REGに登録されているrulenameの値
     */
    public ABNF href(String rulename) {
        ABNF bnf = reg.get(rulename);
        if (bnf == null) {
            bnf = new ABNFRef(rulename);
        }
        return bnf;
    }

    /**
     * ref の参照先を変えないよう書き換えたい
     *
     * @param rulename
     * @param elements
     * @return rule
     */
    public ABNF rule(String rulename, ABNF elements) {
        // ABNF5234の初期化時はnullなので無視できるようにする
        if ( rn != null && !rn.eq(rulename)) {
            System.err.println("ABNF:" + rulename + " ABNFの名称には利用できません");
        }

        if (!rulename.equals(elements.getName())) {
            elements = elements.name(rulename);
        }
        reg.put(rulename, elements);
        return elements;
    }

    /**
     * 主要なところにParse結果をオブジェクトに変換する機能を埋め込むと、いろいろ楽。
     * パースされたABNFにParserを紐づけて登録する。
     *
     * @param rulename ABNFの名
     * @param parser ソースまたは子の要素を渡され対象オブジェクトに組み上げる機能
     * @param elements
     * @return 名前つき rule
     */
    public ABNF rule(String rulename, Class<? extends ABNFParser> parser, ABNF elements) {
        elements = rule(rulename, elements);
        CL.put(rulename, parser);
        return elements;
    }

    /**
     * ABNFの解析ついでに対応するParserを埋め込む。
     * 該当する場合はparserによって対象オブジェクトに変換する機能をつけたABNFを登録する
     *
     * @param rulename ABNFの名
     * @param parser ABNFから対象オブジェクトに変換する解析装置
     * @param elements ABNF構文
     * @return
     */
    public ABNF rule(String rulename, Class<? extends ABNFParser> parser, String elements) {
        return rule(rulename, parser, elements(elements));
    }

    /**
     *
     * @param rule name = value 改行を省略可能に改変している
     * @return
     */
    public ABNF rule(String rule) {
        ABNF abnf = baseParse("rule", rule + "\r\n");
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

    public ABNF elements(String elements) {
        return baseParse("elements", elements);
    }

    /**
     * 一括読み込みと登録。
     * パースされ、regに登録します。
     * RFC 5234の場合、文字コードUTF-8、改行コード CR LF のみ有効です。
     *
     * @param rulelist abnf一覧
     * @return ABNF化された一覧
     */
    public List<ABNF> rulelist(String rulelist) {
        List<ABNF> list = listParse("rulelist").parse(rulelist);
        list.forEach((abnf) -> {
            reg.put(abnf.getName(), abnf);
        });
        return list;
    }

    public List<ABNF> rulelist(FrontPacket rulelist) {
        List<ABNF> list = listParse("rulelist").parse(rulelist);
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
     * @throws IOException
     */
    public List<ABNF> rulelist(URL url) throws IOException {
        InputStream in = url.openStream();
        List<ABNF> rl = rulelist(new FrontPacketF(in));
        in.close();
        return rl;
    }

    /**
     * ユーザ側のParser(JSONなど)を駆動する
     * BASEのみで参照先がないなど
     *
     * @param <T>
     * @param rulename 解析装置付き構文の名。駆動コマンドのようなもの
     * @param src パース対象ソース
     * @return 解析後の実体
     */
    public <T> T parse(String rulename, String src) {
        return (T) parser(rulename, this).parse(src);
    }

    public <T> T parse(String rulename, byte[] src) {
        Packet pac = new PacketA();
        pac.write(src);
        return (T) parser(rulename, this).parse(pac);
    }

    /**
     * ユーザ側のParser(JSONなど)を駆動する
     * BASEのみで参照先がないなど
     *
     * @param <T>
     * @param rulename 解析装置付き構文の名。駆動コマンドのようなもの
     * @param pac パース対象ソース
     * @return 解析後の実体
     */
    public <T> T parse(String rulename, FrontPacket pac) {
        return (T)parser(rulename, this).parse(pac);
    }
    
    private <T> ABNFParser<T> parser(String rulename, ABNFReg sreg) {
        try {
            Constructor<? extends ABNFParser> cnst = (Constructor<? extends ABNFParser<T>>) CL.get(rulename).getConstructor(ABNF.class, ABNFReg.class, ABNFReg.class);
            return cnst.newInstance(reg.get(rulename), sreg, this);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException ex) {
            throw new java.lang.UnsupportedOperationException(ex);
        }
    }

    /**
     * Parser構築用
     * 参照空間とABNF Parserが別に存在する場合が多い
     * 裏側(ABNF Parser)を駆動する
     *
     * @param rulename parser側の名
     * @param src パースに対象ソース
     * @return
     */
    private ABNF baseParse(String rulename, String src) {
        return (ABNF) parseReg.parser(rulename, this).parse(src);
    }

    /**
     * parseの戻り値が一覧のようなもの。
     *
     * @param rulename parser側の名
     * @param src パース対象ソース
     * @return
     */
    private ABNFParser<List<ABNF>> listParse(String rulename) {
        return parseReg.parser(rulename, this);
    }

}
