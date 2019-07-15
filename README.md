てきとーな ABNF ライブラリ

Twitter @okomeki

とりあえず作ってみたので公開する
RFC 5234 ABNF、RFC 7405の拡張を実装したもの

機能1 ABNF Parser
機能2 ABNF 比較とか
機能3 名前参照対応
機能4 オブジェクト/Collection/配列マッピング

class Test {
    static final ABNFReg REG = new ABNFReg();

    static final ABNF 
}


net.siisise.abnf.ABNFReg をstaticで定義して名前の入れ物にできる
net.siisise.abnf.rfc.* サンプルかな

JSON の実装は SoftLibJSON くらいで
