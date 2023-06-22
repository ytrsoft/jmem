package com.ytrsoft.core;

import com.sun.jna.Native;
import com.sun.jna.Structure;

@Structure.FieldOrder({"tid"})
public class LMThread extends Structure {
    public int tid;

    @Override
    public String toString() {
        return "LMThread{" +
                "tid=" + tid +
                '}';
    }
}
