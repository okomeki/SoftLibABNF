package net.siisise.abnf;

import static net.siisise.abnf.AbstractABNF.mix;
import net.siisise.bnf.parser.BNFParser;
import net.siisise.io.FrontPacket;
import net.siisise.io.Packet;
import net.siisise.io.PacketA;

/**
 *
 */
public class ABNFplu extends ABNFplm {
    
    public ABNFplu(ABNF[] list) {
        super(list);
    }

    @Override
    public ABNFpl copy(ABNFReg reg) {
        ABNF[] l = new ABNF[list.length];

        for (int i = 0; i < list.length; i++) {
            l[i] = this.list[i].copy(reg);
        }
        return new ABNFplu(l);
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
    @Override
    protected <X> C<X> longfind(FrontPacket pac, ABNF[] list, BNFParser[] subparsers) {
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
            // ToDo: utf-8で1文字戻る版にしてみた
            flen--;
            while ( flen >= 0 && (sdata[flen] & 0xc0) == 0x80 ) {
                flen--;
            }

        } while (flen >= 0);
        return null;
    }
}
