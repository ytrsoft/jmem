package com.ytrsoft.utils;

public class HexTransform implements Transform {
    @Override
    public String transform(Object value) {
        return Long.toHexString((Long) value).toUpperCase();
    }
}
