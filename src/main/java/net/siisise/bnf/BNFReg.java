package net.siisise.bnf;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import net.siisise.abnf.ABNFrule;
import net.siisise.block.ReadableBlock;
import net.siisise.bnf.parser.BNFPacketParser;
import net.siisise.bnf.parser.BNFParser;
import net.siisise.io.FrontPacket;

/**
 * ABNFのルールのままBNFに持ってきたもの
 * @param <B>
 */
public class BNFReg<B extends BNF> {

    protected final Map<String, B> reg = new HashMap<>();
    protected final Map<String, Class<? extends BNFParser>> CL = new HashMap<>();

    /**
     * BNF の Parser
     */
    protected BNFCC<B> bnfReg;
    /**
     * rulename の制約
     * bnfParser側を使え?
     */
    protected BNF rn;

    /**
     * 名前の参照を先に済ませる
     */
    public class BNFRef extends AbstractBNF<B> {

        BNFRef(String rulename) {
            this.name = rulename;
        }

        /**
         * 複製する.
         *
         * @param reg 複製先
         * @return 複製
         */
        @Override
        public B copy(BNFReg<B> reg) {
            return reg.ref(name);
        }

        @Override
        public ReadableBlock is(ReadableBlock src, Object ns) {
            return reg.get(name).is(src,ns);
        }

        @Override
        public ReadableBlock is(ReadableBlock src) {
            return reg.get(name).is(src);
        }

        @Override
        public <X> Match<X> find(ReadableBlock pac, Object ns, BNFParser<? extends X>... parsers) {
            return reg.get(name).find(pac, ns, parsers);
        }
        
        @Override
        public String toJava() {
            return name;
        }
    }
    
    /**
     * 名前空間作成.いろいろ未定。
     * up の定義を複製する
     * HTTP7230では拡張の実験をしている
     *
     * @param up 前提とする定義など継承もと
     * @param bnfParser ruleをparseするParserの種類 ABNF5234.REG,RFC 7405, RFC 7230など微妙に違うとき。利用しないときのみ省略したい
     */
    public BNFReg(BNFReg<B> up, BNFCC<B> bnfParser) {
        if (up != null) {
            //reg = new HashMap<>(up.reg); // 複製しておくのが簡単
            up.CL.forEach((key,val) -> {
                CL.put(key, val);
            });
            up.reg.forEach((key,val) -> { // 循環参照対策が必要
                reg.put(key, (B)val.copy(this));
            });
        }
        bnfReg = bnfParser;
        rn = bnfParser == null ? null : bnfParser.reg.get(bnfParser.rulename);
    }

    /**
     * 参照リンク優先。 間接参照のため、あとの定義でいろいろ変わるときに便利。
     *
     * @param rulename rulename
     * @return rulenameへの参照
     */
    public B ref(String rulename) {
        return (B)new BNFRef(rulename);
    }

    /**
     * 直リンク優先 差し替えが困難
     * rulenameに該当するものがない場合は参照を返す。
     *
     * @param rulename rulename
     * @return REGに登録されているrulenameの値
     */
    public BNF href(String rulename) {
        BNF bnf = (BNF) reg.get(rulename);
        if (bnf == null) {
            bnf = new BNFRef(rulename);
        }
        return bnf;
    }

    /**
     * ユーザ側のParser(JSONなど)を駆動する。 BASEのみで参照先がないなど
     *
     * @param <T> 解析型
     * @param rulename 解析装置付き構文の名。駆動コマンドのようなもの
     * @param src パース対象ソース
     * @return 解析後の実体
     */
    public <T> T parse(String rulename, String src) {
        return (T) parser(rulename).parse(src);
    }

    /**
     * rulename のABNFでparseするとT型の結果に.
     * @param <T> 解析型
     * @param rulename 解析装置付き構文の名。駆動コマンドのようなもの
     * @param src パース対象ソース
     * @param ns 名前空間
     * @return 解析後の実体
     */
    public <T> T parse(String rulename, String src, Object ns) {
        return (T) parser(rulename).parse(src, ns);
    }

    /**
     * 
     * @param <T> 解析型
     * @param rulename 解析装置付き構文の名。駆動コマンドのようなもの
     * @param src パース対象ソース
     * @return 解析後の実体
     */
    public <T> T parse(String rulename, byte[] src) {
        return (T) parser(rulename).parse(ReadableBlock.wrap(src));
    }

    /**
     * 
     * @param <T> 解析型
     * @param rulename 解析装置付き構文の名。駆動コマンドのようなもの
     * @param src パース対象ソース
     * @param ns 名前空間
     * @return 解析後の実体
     */
    public <T> T parse(String rulename, byte[] src, Object ns) {
        return (T) parser(rulename).parse(ReadableBlock.wrap(src), ns);
    }

    /**
     * ユーザ側のParser(JSONなど)を駆動する。 BASEのみで参照先がないなど
     *
     * @param <T> 解析型
     * @param rulename 解析装置付き構文の名。駆動コマンドのようなもの
     * @param pac パース対象ソース
     * @return 解析後の実体
     */
    public <T> T parse(String rulename, FrontPacket pac) {
        return (T) parser(rulename).parse(pac);
    }

    public <T> T parse(String rulename, FrontPacket pac, Object ns) {
        return (T) parser(rulename).parse(pac, ns);
    }

    /**
     * ユーザ側のParser(JSONなど)を駆動する。 BASEのみで参照先がないなど
     *
     * @param <T> 解析型
     * @param rulename 解析装置付き構文の名。駆動コマンドのようなもの
     * @param pac パース対象ソース
     * @return 解析後の実体
     */
    public <T> T parse(String rulename, ReadableBlock pac) {
        return (T) parser(rulename).parse(pac);
    }

    public <T> T parse(String rulename, ReadableBlock pac, Object ns) {
        return (T) parser(rulename).parse(pac, ns);
    }

    /**
     * ruleで指定されたclassを基にしてParserを生成する.
     *
     * @param <T> Parserが返す解析型
     * @param rulename 解析装置付き構文の名。駆動コマンドのようなもの
     * @return Parser実体
     */
    public <T> BNFParser<T> parser(String rulename) {
        BNF rule = reg.get(rulename);
        Class<? extends BNFParser> rulep = CL.get(rulename);
        if (rulep == null) {
            return (BNFParser<T>) new BNFPacketParser(rule);
        }
        try {
            Constructor<? extends BNFParser> cnst;
            cnst = (Constructor<? extends BNFParser<T>>) rulep.getConstructor(BNF.class, BNFReg.class);
            return cnst.newInstance(rule, this);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException ex) {
            throw new java.lang.UnsupportedOperationException(ex);
        }
    }

    /**
     * ruleの登録。
     * ref の参照先を変えないよう書き換えたい
     *
     * @param <E>
     * @param rulename rulename
     * @param elements rule の element
     * @return rule elements にrulenameをつけたABNF
     */
    public <E extends B> E rule(String rulename, E elements) {
        // ABNF5234の初期化時はnullなので無視できるようにする
        if ( bnfReg != null && !bnfReg.isRulename(rulename)) {
            System.err.println("BNF:" + rulename + " BNFの名称には利用できません");
        }

        if (!rulename.equals(elements.getName())) {
            elements = (E)elements.name(rulename);
        }
        reg.put(rulename, elements);
        return elements;
    }

    /**
     * ruleの登録。
     * 主要なところにParse結果をオブジェクトに変換する機能を埋め込むと、いろいろ楽。
     * パースされたABNFにParserを紐づけて登録する。
     *
     * @param <E>
     * @param rulename ABNFの名
     * @param parser ソースまたは子の要素を渡され対象オブジェクトに組み上げる機能
     * @param elements ruleのelements部分
     * @return 名前つき rule
     */
    public <E extends B> E rule(String rulename, Class<? extends BNFParser> parser, E elements) {
        elements = rule(rulename, elements);
        CL.put(rulename, parser);
        return elements;
    }
    
    /**
     * BNFをパースする。
     * 名前とelementsを個別に渡せると何かと楽かもしれないと思うので作った。
     * ToDo: ABNF5234 へ
     *
     * @param rulename ABNF構文の名
     * @param elements ABNF式
     * @return 解析されたABNF
     */
    public B rule(String rulename, String elements) {
        return rule(rulename, elements(elements));
    }

    /**
     * ruleの登録。
     * ABNFの解析ついでに対応するParserを埋め込む。
     * 該当する場合はparserによって対象オブジェクトに変換する機能をつけたABNFを登録する
     *
     * @param rulename ABNFの名
     * @param parser ABNFから対象オブジェクトに変換する解析装置
     * @param elements ABNF構文
     * @return elementsを解析してrulenameをつけたABNF
     */
    public B rule(String rulename, Class<? extends BNFParser> parser, String elements) {
        return rule(rulename, parser, (B)elements(elements));
    }

    /**
     * element を作る. 名前は持っていないことがある.
     * elements は ABNFの名
     * @param elements bnf系の = から右
     * @return element に変換されたABNF
     */
    public B elements(String elements) {
        return bnfReg.parse(bnfReg.elements, elements, this);
    }

    /**
     * rule 1行のパース.
     * 最後の改行は省略可能
     * @param rule name = value 改行を省略可能に改変している
     * @return rule 1行をABNFにしたもの
     */
    public B rule(String rule) {
        B bnf = bnfReg.parse(bnfReg.rule, rule + "\r\n", this);
        return rule(bnf.getName(), bnf);
    }

    /**
     * @param pac 解析対象
     * @param rulename rulename
     * @param subrulenames サブ要素rulename
     * @return 仮型
     */
    public BNF.Match find(FrontPacket pac, String rulename, String... subrulenames) {
        return find(ReadableBlock.wrap(pac), rulename, subrulenames);
    }
    
    /**
     * @param rb 解析対象
     * @param rulename rulename
     * @param subrulenames サブ要素rulename
     * @return 仮型
     */
    public BNF.Match find(ReadableBlock rb, String rulename, String... subrulenames) {
        BNF rule = href(rulename);

        BNFParser[] cll = new BNFParser[subrulenames.length];
        for (int i = 0; i < subrulenames.length; i++) {
            cll[i] = parser(subrulenames[i]);
        }
        return rule.find(rb, cll);
    }

    String javaLine(String ruleName, String regName, BNFrule rule) {
        StringBuilder src = new StringBuilder();
            src.append("\r\n    static final BNF ");
            src.append(ruleName).append(" = ").append(regName);
            src.append(rule.toJavaLine());
            src.append(";");
        return src.toString();
    }

    String javaLine(String ruleName, String regName, ABNFrule rule) {
        StringBuilder src = new StringBuilder();
            src.append("\r\n    static final BNF ");
            src.append(ruleName).append(" = ").append(regName);
            src.append(rule.toJavaLine());
            src.append(";");
        return src.toString();
    }

    public String toJava(String regName) {
        StringBuilder src = new StringBuilder();
        src.append("class Example {");
        src.append("\r\n    static BNFReg ").append(regName).append(" = new BNFReg();");
        src.append("\r\n");
        
        for (String ruleName : reg.keySet() ) {
            BNF b = reg.get(ruleName);
            if ( b instanceof BNFrule ) {
                BNFrule bnf = (BNFrule)b;
                src.append(javaLine(ruleName, regName, bnf));
            } else if (b instanceof ABNFrule ) {
                ABNFrule bnf = (ABNFrule)b;
                src.append(javaLine(ruleName, regName, bnf));
            }
        }
        src.append("\r\n}");
        
        return src.toString();
    }

}
