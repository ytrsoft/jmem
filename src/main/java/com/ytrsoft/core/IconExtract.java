package com.ytrsoft.core;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.COM.COMUtils;
import com.sun.jna.platform.win32.*;
import com.sun.jna.ptr.PointerByReference;

import java.awt.image.BufferedImage;
import java.nio.file.Path;

public final class IconExtract {

    private static final String IID = "BCC18B79-BA16-442F-80C4-8A59C30C463B";

    private IconExtract() {
        throw new UnsupportedOperationException();
    }

    public static BufferedImage getIconForFile(int width, int height, Path file) {
        return getIconForFile(width, height, file.toString());
    }

    public static BufferedImage getIconForFile(int width,int height,String fileName) {
        WinDef.HBITMAP hbitmap = getHBITMAPForFile(width, height, fileName);
        if(hbitmap == null) return null;
        WinGDI.BITMAP bitmap = new WinGDI.BITMAP();
        try {
            if(extractBitmapInfo(hbitmap, bitmap) > 0) {
                return getBufferedImageFromBitmap(bitmap, hbitmap);
            }
        }finally {
            GDI32.INSTANCE.DeleteObject(hbitmap);
        }
        return null;
    }

    private static int extractBitmapInfo(WinDef.HBITMAP hbitmap, WinGDI.BITMAP bitmap) {
        return GDI32.INSTANCE.GetObject(hbitmap,bitmap.size(), bitmap.getPointer());
    }

    private static BufferedImage getBufferedImageFromBitmap(WinGDI.BITMAP bitmap, WinDef.HBITMAP hbitmap){
        bitmap.read();
        int w = bitmap.bmWidth.intValue();
        int h = bitmap.bmHeight.intValue();
        final WinDef.HDC hdc = User32.INSTANCE.GetDC(null);
        WinGDI.BITMAPINFO bitmapinfo = prepareBitmapInfo(h, w, hdc, hbitmap);
        Memory lpPixels = new Memory((long) w * h * 4);
        bitmapinfo.bmiHeader.biCompression = WinGDI.BI_RGB;
        bitmapinfo.bmiHeader.biHeight = -h;
        bitmapinfo.bmiHeader.biSizeImage = w * h * 4;
        handleGDIResult(GDI32.INSTANCE.GetDIBits(hdc, hbitmap, 0, h, lpPixels, bitmapinfo, WinGDI.DIB_RGB_COLORS));
        int[] colorArray = lpPixels.getIntArray(0, w * h);
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        bi.setRGB(0, 0, w, h, colorArray, 0, w);
        return bi;
    }

    private static WinGDI.BITMAPINFO prepareBitmapInfo(int h, int w, WinDef.HDC hdc, WinDef.HBITMAP hbitmap){
        WinGDI.BITMAPINFO bitmapinfo = new WinGDI.BITMAPINFO();
        bitmapinfo.bmiHeader.biSize = bitmapinfo.bmiHeader.size();
        bitmapinfo.bmiHeader.biWidth = w;
        bitmapinfo.bmiHeader.biHeight = -h;
        bitmapinfo.bmiHeader.biPlanes = 1;
        bitmapinfo.bmiHeader.biBitCount = 32;
        bitmapinfo.bmiHeader.biCompression = WinGDI.BI_RGB;
        handleGDIResult(GDI32.INSTANCE.GetDIBits(hdc, hbitmap, 0, 0, Pointer.NULL, bitmapinfo, WinGDI.DIB_RGB_COLORS));
        bitmapinfo.read();
        return bitmapinfo;
    }

    private static void handleGDIResult(int result){
        if(result == 0) {
            throw new IllegalArgumentException("GetDIBits should not return 0");
        }
    }

    public static WinDef.HBITMAP getHBITMAPForFile(int width, int height, String fileName) {
        WinNT.HRESULT h1 = Ole32.INSTANCE.CoInitialize(null);
        if(COMUtils.SUCCEEDED(h1)) {
            PointerByReference factory = new PointerByReference();
            WinNT.HRESULT h2 = Shell32Ex.INSTANCE.SHCreateItemFromParsingName(
                    new WString(fileName),
                    null,
                    new Guid.REFIID(new Guid.IID(IID)),
                    factory
            );
            if(COMUtils.SUCCEEDED(h2)) {
                IShellItemImageFactory imageFactory = new IShellItemImageFactory(factory.getValue());
                PointerByReference bitmapPointer = new PointerByReference();
                WinNT.HRESULT h3 = imageFactory.getImage(new IShellItemImageFactory.SIZEByValue(width, height), 0, bitmapPointer);
                if(COMUtils.SUCCEEDED(h3)) {
                    return new WinDef.HBITMAP(bitmapPointer.getValue());
                }
                imageFactory.Release();
            }
        }
        return null;
    }
}