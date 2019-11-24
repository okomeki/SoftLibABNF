とりあえずのABNF類

staticなものはとりあえずあるだけなので継承されている場合などは使わない方がいい
REGから該当する要素を取り出すのが今は正確

かんたんな使い方

boolean b = IMF5322.addrSpec.eq("okome@example.com");


abnfをリソースファイルで持っておいて読み込むには
文字コード UTF-8 改行コード CRLF

URL url = getClass().getResource("/net/siisise/abnf/rfc/IMF5322.abnf");
ABNFReg reg = new ABNFReg(url, ABNF5234.BASE);
boolean b = reg.ref("addr-spec").eq("okome@example.com");
