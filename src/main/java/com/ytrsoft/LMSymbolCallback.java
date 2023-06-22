package com.ytrsoft;

import com.sun.jna.win32.StdCallLibrary;

public interface LMSymbolCallback extends StdCallLibrary.StdCallCallback {
    boolean apply(LMSymbol symbol);
}
