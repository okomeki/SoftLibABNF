package net.siisise.abnf;

import net.siisise.bnf.parser.BNFParser;
import net.siisise.io.FrontPacket;
import net.siisise.io.Packet;
import net.siisise.io.PacketA;

/**
 * 完全一致検索。
 * 重いかもしれない
 * example = *( a / b ) a に対応したもの
 *
 */
public class ABNFplm extends ABNFpl {

    public ABNFplm(ABNF[] list) {
        super(list);
    }

    @Override
    public ABNFpl copy(ABNFReg reg) {
        ABNF[] l = new ABNF[list.length];

        for (int i = 0; i < list.length; i++) {
            l[i] = this.list[i].copy(reg);
        }
        return new ABNFplm(l);
    }

    @Override
    public <X,N> C<X> buildFind(FrontPacket pac, N ns, BNFParser<? extends X>... subps) {
        return longfind(pac, ns, list, subps);
    }

    /**
     * 詳細検索
     *
     * @param <X>
     * @param pac
     * @param list
     * @param subparsers
     * @return
     */
    protected <X,N> C<X> longfind(FrontPacket pac, N ns, ABNF[] list, BNFParser<? extends X>[] subparsers) {
        if (list.length == 0) {
            return new C();
        }
        int flen = pac.size();
        
        do {
            // 1つめ 指定サイズまでに制限する
            Packet frontPac = new PacketA();
            byte[] data = new byte[flen];
            pac.read(data, 0, flen);
            frontPac.write(data, 0, flen);
            C firstret = list[0].find(frontPac, ns, subparsers);
            pac.dbackWrite(frontPac.toByteArray());

            if (firstret == null || list.length == 1) { // 一致しないか最後ならここで戻り
                return firstret;
            }
            flen = firstret.ret.size();
            // 2つめ以降
            ABNF[] slist = new ABNF[list.length - 1];
            System.arraycopy(list, 1, slist, 0, slist.length);
            C nextret = longfind(pac, ns, slist, subparsers);
            if (nextret != null) {
                // firstret と nextret 両方成立
                mix(firstret, nextret);
                return firstret;
            }
            // scのみ成立 破棄
            byte[] sdata = firstret.ret.toByteArray();
            pac.dbackWrite(sdata);
            flen--;

        } while (flen >= 0);
        return null;
    }
}
