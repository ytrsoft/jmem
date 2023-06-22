package com.ytrsoft.core;

import com.sun.jna.Native;
import com.sun.jna.Structure;

@Structure.FieldOrder({"name", "address"})
public class LMSymbol extends Structure {
    public char name;
    public int address;

    @Override
    public String toString() {
        return "LMSymbol{" +
                "name=" + name +
                ", address=" + address +
                '}';
    }
}
