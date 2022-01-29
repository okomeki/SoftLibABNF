/*
 * Copyright 2021 Siisise Net.
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
package net.siisise.bnf;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.siisise.bnf.parser.BNFParser;
import net.siisise.io.FrontPacket;
import net.siisise.io.Packet;
import net.siisise.io.PacketA;

/**
 * 抽象的なBNF全般
 */
public interface BNF {
    
    static final Charset UTF8 = StandardCharsets.UTF_8;

    /**
     * 名前または構文っぽいものの取得。
     * 名前がない場合に完全ではないので注意。
     * @return rule name or syntax 名前または構文らしきもの
     */
    String getName();

    /**
     * 先頭一致でパースする。
     * findとどちらかの実装が必要、片方は省略可能
     * @param <N> user name space type 名前空間型
     * @param src source 解析対象
     * @param ns user name space 名前空間
     * @return 一致した範囲
     */
    <N> Packet is(FrontPacket src, N ns);
    Packet is(FrontPacket src);

    /**
     * 先頭一致でパースする。
     * 
     * @param <N> user name space type
     * @param val 比較文字列
     * @param ns user name space
     * @return 一致ありなし
     */
    <N> boolean is(String val, N ns);
    boolean is(String val);

    /**
     * サブ要素含んで抽出するパーサな機能.
     * @param <X> 返し型
     * @param <N> user name space type
     * @param pac 元データ
     * @param ns user name space
     * @param parsers おまけで検索するサブ要素
     * @return 検索結果
     */
    <X,N> C<X> find(FrontPacket pac, N ns, BNFParser<? extends X>... parsers);

    /**
     * 検索結果用構造
     * あと1回書き直したい
     * @param <X> データ型
     */
    public static class C<X> {
        /**
         * メインの戻り値
         */
        public final Packet ret;
        public final Map<String, List<X>> subs = new HashMap<>();

        public C() {
            ret = new PacketA();
        }

        public C(Packet pac) {
            ret = pac;
        }

        public void add(String name, X r) {
            List l;
            l = subs.get(name);
            if (l == null) {
                l = new ArrayList();
                subs.put(name, l);
            }
            l.add(r);
        }

        public Set<String> keySet() {
            return subs.keySet();
        }

        public List<X> get(String name) {
            return subs.get(name);
        }

        public List<X> get(BNF bnf) {
            return get(bnf.getName());
        }
    }
}
