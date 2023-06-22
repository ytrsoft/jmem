package com.ytrsoft;

import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;

@Structure.FieldOrder({"vtable", "hkentries"})
public class LMVmt extends Structure {
    public IntByReference vtable;
    LMVmtEntry hkentries;
}
