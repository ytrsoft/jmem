package com.ytrsoft.core;

import com.sun.jna.Structure;

@Structure.FieldOrder({"orig_func", "index", "next"})
public class LMVmtEntry extends Structure {
    public int orig_func;
    int index;
    LMVmtEntry next;
}
