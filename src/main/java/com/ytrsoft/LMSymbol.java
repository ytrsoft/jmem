package com.ytrsoft;

import com.sun.jna.Structure;

@Structure.FieldOrder({"name", "address"})
public class LMSymbol extends Structure {
    public String name;
    public int address;
}
