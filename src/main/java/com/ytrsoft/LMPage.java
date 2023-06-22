package com.ytrsoft;

import com.sun.jna.Structure;

@Structure.FieldOrder({"base", "end", "size", "prot"})
public class LMPage extends Structure {
    public int base;
    public int end;
    public int size;
    public int prot;
}
