# てきとーな ABNF ライブラリ

Twitter @okomeki

## 概要

ABNFによるABNFのためのABNF
とりあえず作ってみたので公開する
RFC 5234 ABNF、RFC 7405の拡張を実装したもの

### 何ができるのか?

メールアドレスの判定がしたい
URLの解析がしたい
IPv6アドレスの(以下同文)
のようなものから
JSONとかABNFとか使いたい
新しいプログラム言語とか作りたい?
とRFCやABNFでお困りのいろいろを解決します


- 機能1 ABNF Parser
- 機能2 ABNF 比較とか
- 機能3 名前参照対応
- 機能4 オブジェクト/Collection/配列マッピング

とりあえずサンプル net.siisise.abnf.parser5234.ABNF5234.java がABNF Parserの本体でありサンプルであるかもしれません。

ABNFの rule の単位で組み立てていきますが、定義はABNFをStringで書いてもJavaで組んでもおなじです。parser classの組み込みなどが不要な場合は ABNFのrulelistをそのまま読み込むこともできます。
RFC 5234 の 4.ABNFのABNFによる定義から

    rulelist = 1*( rule / *c-wsp c-nl) )
    
があるとします。Javaにすると

    class ABNFSample {
        static final ABNFReg REG = new ABNFReg();
        static final ABNF rulelist = REG.rule( "rulelist = 1*( rule / (*c-wsp c-nl) )");
    }

とりあえずこれでよいです。未定義のruleなども次の行に続けて定義していけば全体が完成します。

    static final ABNF rulelist = REG.rule( "rulelist", "1*( rule / (*c-wsp c-nl) )");

と書く方がおすすめです。rule( String rulename, String elements ) と rule( String rulename, ABNF elements ) のどちらでも使えるのでABNFをJavaのコードで書くこともできます。

    rulelist.eq("文字列"); // 全体一致
    rulelist.is("文字列"); // 先頭一致

で boolean の一致判定ができます。

その他、ABNFとABNFRegをみれば何とかなるのかも。

## 演算子

3.演算子のものをJavaで書くと

### 連接 Rule1 Rule2 #pl(複数)

    foo = %x61 ; a
    bar = %x62 ; b
    mumble = foo bar foo

    ABNF foo = REG.rule("foo", ABNF.bin(0x61));
    ABNF bar = REG.rule("bar", ABNF.bin(0x62));
    ABNF mumble = REG.rule("mumble", foo.pl(bar, foo));

パース手法の違いで pl , plm , plu などがある。繰り返しを厳密に判定するなら plm や plu がいいのだが違いはまた別途。

### 選択肢 Rule1 / Rule2 #or(複数)

    foo / bar
    foo / bar / baz

    foo.or( bar )
    foo.or( bar. baz )
   
### 増分選択肢 Rule1 =/ Rule2 #or(複数)

    ABNF oldrule = REG.rule( "oldrule", oldrule.or( additionalAlternatives ) ); // 特に定義なし

REG.rule() または #name(String)を通さないと名前はつかない。

### 範囲選択肢 %c##-##

    DIGIT = %x30-39

    ABNF digit = REG.rule("digit", ABNF.range(0x30, 0x39));

### 並びグループ

    elem (foo / bar) blat
    elem foo / bar blat
    
    elem.pl( foo.or( bar ), blat )
    elem.pl(foo).or(bar.pl(blat))

### 可変回数反復 *Rule

    <a>*<b>element
    
    element.x(a,b)
    element.ix() // a=1,bの省略 1*element
    element.x() // a,bの省略 *element
    値で省略する場合、a = 0, b = -1 と少々複雑

### 特定回数反復 nRule

    <n>element
    
    element.x(n)
    
### 省略可能並び: [Rule]

    [foo bar]
    
    foo.pl(bar).c()

cが似ていたので

## Parser

booleanだけでは物足りないので ABNF定義1つにつき1つのParserを組み込むことができます。
内側の要素を抽出することもできますが、基本的に埋め込むParser用なので少し特殊な構造です。あとで変更するかもしれません。

    ABNF rule = (略);
    ABNF.C c = rulelist.find(AbstractABNF.pac("文字列"), new ABNFPacketParser(rule, JSON8259Reg.REG));
    List<FrontPacket> rule = c.get(rule);

Stringやbyet[]ではなくPacketという可変長バイト列を用意して使っているのでそのあたりも特殊かもしれません。長さを気にしたくないので各所で使っています。

ParserをABNFに組み込むことができます。

    ABNF rulelist = REG.rule("rulelist", Rulelist.class, "1*( rule / (*c-wsp c-nl) )");

Rulelist.class は net.siisise.abnf.parser.ABNFParser<T> を継承しています。TはParse後に取り出せるclassで何でもよいです。net.siissie.abnf.parser パッケージの中から最適なものを継承して使います。リフレクションで呼び出すのでコンストラクタはパラメータが決まっているところも要注意です。

rulelits全体を読む場合は REG.rulelist( URL ) など他の読み方もいくつか用意しています。複数行を記述する場合、ABNFの改行コードはCRLFのみです。

    List<ABNF> rulelist = REG.rulelist("https://example.com/example.abnf");

ABNFReg が1つの名前空間で、この中で参照を解決していきます。
Javaから参照する場合は REG から参照することも rulelist を使うこともできます。

    ABNF rl = REG.ref("rulelist");
    
として取り出したり、他のrule中で使うことで参照できます。href(Strnig)とref(String)の違いは未定義のものを参照できるかできないかの違いです。上から順番に定義していける場合とネストしている場合があるので使い分けることもあります。



    class Test {
        // 名前空間
        static final ABNFReg REG = new ABNFReg();
        // ルール定義の書式1
        static final ABNF charVal = REG.rule("charVal", CharVal.class, Java式);
        // ルール定義の書式2
        static final ABNF abnf1 = REG.rule("なまえ", parser.class, "定義");
        // ルール定義の書式3
        static final ABNF abnf1 = REG.rule("なまえ", parser.class, "定義");
    }


net.siisise.abnf.ABNFReg をstaticで定義して名前の入れ物にできる
net.siisise.abnf.rfc.* サンプルかな

JSON の実装は SoftLibJSON くらいで
