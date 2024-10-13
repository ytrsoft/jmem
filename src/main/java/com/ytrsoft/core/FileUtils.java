package com.ytrsoft.core;

public final class FileUtils {

    private FileUtils() {
        throw new UnsupportedOperationException();
    }

    private static final long KILOBYTE = 1024;
    private static final long MEGABYTE = KILOBYTE * 1024;
    private static final long GIGABYTE = MEGABYTE * 1024;
    private static final long TERABYTE = GIGABYTE * 1024;

    public static String formatter(long size) {
        if (size < KILOBYTE) {
            return size + " B";
        } else if (size < MEGABYTE) {
            return String.format("%.2f KB", (double) size / KILOBYTE);
        } else if (size < GIGABYTE) {
            return String.format("%.2f MB", (double) size / MEGABYTE);
        } else if (size < TERABYTE) {
            return String.format("%.2f GB", (double) size / GIGABYTE);
        } else {
            return String.format("%.2f TB", (double) size / TERABYTE);
        }
    }
}
