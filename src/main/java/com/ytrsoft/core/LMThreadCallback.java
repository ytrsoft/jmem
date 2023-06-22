package com.ytrsoft.core;

import com.sun.jna.win32.StdCallLibrary;

public interface LMThreadCallback extends StdCallLibrary.StdCallCallback {
    boolean apply(LMThread thread);
}
