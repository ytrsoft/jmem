package com.ytrsoft;

import com.sun.jna.Structure;

@Structure.FieldOrder({"tid"})
public class LMThread extends Structure {
    public int tid;
}
