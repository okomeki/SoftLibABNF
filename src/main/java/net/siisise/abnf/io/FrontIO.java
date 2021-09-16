package net.siisise.abnf.io;

import java.io.InputStream;
import java.io.OutputStream;
import net.siisise.io.FrontPacket;

/**
 * FrontPacket  (Streamなし) のみを配列固定長で実装した軽量版。
 * 速そう
 */
public class FrontIO implements FrontPacket {
    
    byte[] data;
    int offset;
    
//    InputStream in;
    
    public FrontIO(byte[] data) {
        this.data = data;
    }

    @Override
    public InputStream getInputStream() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//        if ( in == null ) {
//            in = new ByteArrayInputStream(data);
//        }
//        return in;
    }

    @Override
    public OutputStream getBackOutputStream() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int read() {
        if ( offset < data.length ) {
            return data[offset++] & 0xff;
        }
        return -1;
    }

    @Override
    public int read(byte[] data, int offset, int length) {
        if ( length <= (this.data.length - this.offset) ) {
            System.arraycopy(this.data, this.offset, data, offset, length);
            this.offset += length;
            return length;
        } else {
            System.arraycopy(this.data, this.offset, data, offset, (this.data.length - this.offset));
            int l = this.data.length - this.offset;
            this.offset = this.data.length;
            return l;
        }
    }

    @Override
    public int read(byte[] data) {
        return read(data, 0, data.length);
    }

    @Override
    public byte[] toByteArray() {
        byte[] data = new byte[this.data.length - offset];
        System.arraycopy(this.data, offset, data, 0, data.length);
        offset = this.data.length;
        return data;
    }

    @Override
    public void backWrite(int data) {
        offset--;
        this.data[offset] = (byte)data;
    }

    @Override
    public void backWrite(byte[] data, int offset, int length) {
        this.offset -= length;
        System.arraycopy(data, offset, this.data, this.offset, length);
    }

    @Override
    public void backWrite(byte[] data) {
        this.offset -= data.length;
        System.arraycopy(data, 0, this.data, offset, data.length);
    }

    @Override
    public long length() {
        return data.length - offset;
    }

    @Override
    public int size() {
        return data.length - offset;
    }

}
