package com.ytrsoft.core;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

public interface Libmem extends StdCallLibrary {
    Libmem INSTANCE = Native.load("libmem", Libmem.class, W32APIOptions.DEFAULT_OPTIONS);
}
