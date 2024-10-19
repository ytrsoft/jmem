package com.ytrsoft.core;
;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Icons {

    private static final int SIZE = 16;

    private Icons() {
        throw new UnsupportedOperationException();
    }

    public static BufferedImage get(String url) {
        Path path = Paths.get(url);
        return IconExtract.getIconForFile(SIZE, SIZE, path);
    }
}