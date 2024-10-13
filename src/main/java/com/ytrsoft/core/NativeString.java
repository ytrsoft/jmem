package com.ytrsoft.core;

import com.sun.jna.Native;

public final class NativeString {

    private NativeString() {
        throw new UnsupportedOperationException();
    }

    public static String load(byte[] bytes) {
        return Native.toString(bytes).trim();
    }

    public static byte[] toByteArray(String str, int size) {
        byte[] result = new byte[size];
        if (str != null) {
            byte[] strBytes = Native.toByteArray(str);
            System.arraycopy(strBytes, 0, result, 0, Math.min(strBytes.length, size));
        }
        return result;
    }

}
