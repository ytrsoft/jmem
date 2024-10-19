package com.ytrsoft.core;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.COM.Unknown;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.ptr.PointerByReference;

public class IShellItemImageFactory extends Unknown {

    public IShellItemImageFactory(Pointer p) {
        super(p);
    }

    static class SIZEByValue extends WinUser.SIZE implements Structure.ByValue {
        public SIZEByValue(int w, int h) {
            super(w, h);
        }
    }

    public WinNT.HRESULT getImage(SIZEByValue size, int flags, PointerByReference bitmap) {
        Object[] params = new Object[] {
                this.getPointer(),
                size,
                flags,
                bitmap
        };
        return (WinNT.HRESULT) _invokeNativeObject(3, params, WinNT.HRESULT.class);
    }
}