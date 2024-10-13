package com.ytrsoft;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.ytrsoft.core.UITools;
import com.ytrsoft.ui.ProcessTreeTable;

import javax.swing.*;
import java.awt.*;

public class AppView extends JFrame {

    public AppView() {
        setTitle("Libmem");
        setSize(UITools.getDefaultSize());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void setup() {
        setVisible(true);
        getContentPane().add(new ProcessTreeTable());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FlatDarkLaf.setup();
            new AppView().setup();
        });
    }

}
