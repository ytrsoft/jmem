package com.ytrsoft.core;

import com.sun.jna.Native;
import com.sun.jna.Structure;

@Structure.FieldOrder({"base", "end", "size", "prot"})
public class LMPage extends Structure {
    public int base;
    public int end;
    public int size;
    public int prot;

    @Override
    public String toString() {
        return "LMPage{" +
                "base=" + base +
                ", end=" + end +
                ", size=" + size +
                ", prot=" + prot +
                '}';
    }
}
