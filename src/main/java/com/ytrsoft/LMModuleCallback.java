package com.ytrsoft;

import com.sun.jna.win32.StdCallLibrary;

public interface LMModuleCallback extends StdCallLibrary.StdCallCallback {
    boolean apply(LMModule module);
}
