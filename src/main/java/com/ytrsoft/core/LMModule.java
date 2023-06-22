package com.ytrsoft.core;

import com.sun.jna.Native;
import com.sun.jna.Structure;

@Structure.FieldOrder({"base", "end", "size", "path", "name"})
public class LMModule extends Structure {
    public int base;
    public int end;
    public int size;
    public byte[] path = new byte[LibmemNT.PATH_MAX];
    public byte[] name = new byte[LibmemNT.PATH_MAX];

    @Override
    public String toString() {
        return "LMModule{" +
                "base=" + base +
                ", end=" + end +
                ", size=" + size +
                ", path=" + Native.toString(path) +
                ", name=" + Native.toString(name) +
                '}';
    }
}
