package com.ytrsoft;

import com.sun.jna.*;

@Structure.FieldOrder({"pid", "ppid", "bits", "start_time", "path", "name"})
public class LMProcess extends Structure {
    public int pid;
    public int ppid;
    public int bits;
    public long start_time;
    public byte[] path = new byte[LibmemNT.PATH_MAX];
    public Pointer name = new Memory(LibmemNT.PATH_MAX);
}
