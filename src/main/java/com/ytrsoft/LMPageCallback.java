package com.ytrsoft;

import com.sun.jna.win32.StdCallLibrary;

public interface LMPageCallback extends StdCallLibrary.StdCallCallback {
    boolean apply(LMPage page);
}
