package com.ytrsoft.core;

import javax.swing.*;
import java.awt.*;

public final class UITools {

    private static final float SCALE = 0.7F;

    private UITools() {
        throw new UnsupportedOperationException();
    }

    public static Dimension getDefaultSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width * SCALE);
        int height = (int) (screenSize.height * SCALE);
        return new Dimension(width, height);
    }

    public static Dimension getDefaultSizeSM() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width * SCALE * 0.8);
        int height = (int) (screenSize.height * SCALE * 0.8);
        return new Dimension(width, height);
    }
}
