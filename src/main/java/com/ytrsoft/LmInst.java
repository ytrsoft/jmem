package com.ytrsoft;

import java.util.Arrays;

public class LmInst {
    private long address;
    private int size;
    private byte[] bytes;
    private String mnemonic;
    private String opStr;

    public LmInst() {}

    public LmInst(Libmem.LmInst i) {
        this.address = i.address;
        this.size = i.size;
        this.mnemonic = NativeString.load(i.mnemonic);
        this.opStr = NativeString.load(i.opStr);
    }

    public Libmem.LmInst toRef() {
        Libmem.LmInst i = new Libmem.LmInst();
        i.address = this.address;
        i.size = this.size;
        i.bytes = this.bytes;
        i.mnemonic = NativeString.toByteArray(this.mnemonic, Libmem.LM_MNEMONIC_MAX);
        i.opStr = NativeString.toByteArray(this.opStr, Libmem.LM_OP_MAX);
        return i;
    }

    public long getAddress() {
        return address;
    }

    public void setAddress(long address) {
        this.address = address;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    public String getOpStr() {
        return opStr;
    }

    public void setOpStr(String opStr) {
        this.opStr = opStr;
    }

    @Override
    public String toString() {
        return "LmInst{" +
                "address=" + address +
                ", size=" + size +
                ", bytes=" + Arrays.toString(bytes) +
                ", mnemonic='" + mnemonic + '\'' +
                ", opStr='" + opStr + '\'' +
                '}';
    }
}
