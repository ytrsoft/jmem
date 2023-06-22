package com.ytrsoft.core;

import com.sun.jna.win32.StdCallLibrary;

public interface LMProcessCallback extends StdCallLibrary.StdCallCallback {
    boolean apply(LMProcess process);
}
