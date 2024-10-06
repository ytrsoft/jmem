package com.ytrsoft;

public final class ByteUtils {

    private ByteUtils() {
        throw new UnsupportedOperationException();
    }

    public static int toInt(byte[] bytes) {
        if (bytes == null || bytes.length != 4) {
            return -1;
        }
        return ((bytes[0] & 0xFF) << 24) |
                ((bytes[1] & 0xFF) << 16) |
                ((bytes[2] & 0xFF) << 8)  |
                (bytes[3] & 0xFF);
    }

}
