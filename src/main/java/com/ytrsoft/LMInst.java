package com.ytrsoft;

import com.sun.jna.Structure;

@Structure.FieldOrder({"id;", "address", "size", "bytes", "mnemonic", "op_str"})
public class LMInst extends Structure {
    public int id;
    public long address;
    public short size;
    public byte[] bytes = new byte[LibmemNT.INST_SIZE];
    public byte[] mnemonic = new byte[32];
    public byte[] op_str = new byte[160];
}
