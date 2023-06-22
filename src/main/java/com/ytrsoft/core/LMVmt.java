package com.ytrsoft.core;

import com.sun.jna.Structure;

@Structure.FieldOrder({"vtable", "hkentries"})
public class LMVmt extends Structure {
    public int vtable;
    int hkentries;
}
