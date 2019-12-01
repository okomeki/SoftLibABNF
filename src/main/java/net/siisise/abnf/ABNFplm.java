package net.siisise.abnf;

import net.siisise.abnf.parser.ABNFParser;
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
    public <X> C<X> find(Packet pac, ABNFParser<? extends X>... names) {
        C<X> ret = new ABNF.C<>();
        ABNFParser[] subparsers;
        boolean n = isName(names);
        subparsers = n ? new ABNFParser[0] : names;

        ret = longfind(pac, list, subparsers);
        return n ? sub(ret, names) : ret;
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
    private <X> C<X> longfind(Packet pac, ABNF[] list, ABNFParser[] subparsers) {
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
            C firstret = list[0].find(frontPac, subparsers);
            pac.backWrite(frontPac.toByteArray());

            if (firstret == null || list.length == 1) { // 一致しないか最後ならここで戻り
                return firstret;
            }
            flen = firstret.ret.size();
            // 2つめ以降
            ABNF[] slist = new ABNF[list.length - 1];
            System.arraycopy(list, 1, slist, 0, slist.length);
            C nextret = longfind(pac, slist, subparsers);
            if (nextret != null) {
                // firstret と nextret 両方成立
                mix(firstret, nextret);
                return firstret;
            }
            // scのみ成立 破棄
            byte[] sdata = firstret.ret.toByteArray();
            pac.backWrite(sdata);
            flen--;

        } while (flen >= 0);
        return null;
    }
}
