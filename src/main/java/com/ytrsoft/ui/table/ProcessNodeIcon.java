package com.ytrsoft.ui.table;

import com.ytrsoft.core.Icons;
import com.ytrsoft.core.LmProcess;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ProcessNodeIcon extends JPanel {
    public ProcessNodeIcon(TreeTableNode<LmProcess> node) {
        LmProcess process = node.getData();
        if (process != null) {
            setLayout(new BorderLayout());
            JLabel icon = new JLabel();
            icon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 4));
            BufferedImage image = Icons.get(process.getPath());
            if (image != null) {
                icon.setIcon(new ImageIcon(image));
            }
            JLabel text = new JLabel(String.valueOf(process.getId()));
            add(icon, BorderLayout.WEST);
            add(text, BorderLayout.CENTER);
            setBackground(Color.decode("#46494B"));
        }
    }
}
