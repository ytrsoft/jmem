package com.ytrsoft.core;

import com.sun.jna.Structure;

import java.util.Arrays;

@Structure.FieldOrder({"id;", "address", "size", "bytes", "mnemonic", "op_str"})
public class LMInst extends Structure {
    public int id;
    public long address;
    public short size;
    public byte[] bytes = new byte[LibmemNT.INST_SIZE];
    public byte[] mnemonic = new byte[32];
    public byte[] op_str = new byte[160];

    @Override
    public String toString() {
        return "LMInst{" +
                "id=" + id +
                ", address=" + address +
                ", size=" + size +
                ", bytes=" + Arrays.toString(bytes) +
                ", mnemonic=" + Arrays.toString(mnemonic) +
                ", op_str=" + Arrays.toString(op_str) +
                '}';
    }
}
