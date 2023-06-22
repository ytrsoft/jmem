package com.ytrsoft.core;

import com.sun.jna.win32.StdCallLibrary;

public interface LMSymbolCallback extends StdCallLibrary.StdCallCallback {
    boolean apply(LMSymbol symbol);
}
