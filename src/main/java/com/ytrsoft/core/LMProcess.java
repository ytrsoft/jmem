package com.ytrsoft.core;

import com.sun.jna.Native;
import com.sun.jna.Structure;

@Structure.FieldOrder({"pid", "ppid", "bits", "start_time", "path", "name"})
public class LMProcess extends Structure {
    public int pid;
    public int ppid;
    public int bits;
    public long start_time;
    public byte[] path = new byte[LibmemNT.PATH_MAX];
    public byte[] name = new byte[LibmemNT.PATH_MAX];

    @Override
    public String toString() {
        return "LMProcess{" +
                "pid=" + pid +
                ", ppid=" + ppid +
                ", bits=" + bits +
                ", start_time=" + start_time +
                ", path=" + Native.toString(path) +
                ", name=" + Native.toString(name) +
                '}';
    }
}
