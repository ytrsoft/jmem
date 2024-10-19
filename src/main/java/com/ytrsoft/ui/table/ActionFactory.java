package com.ytrsoft.ui.table;

import javax.swing.*;
import java.awt.*;

public final class ActionFactory {

    private ActionFactory() {
        throw new UnsupportedOperationException();
    }

    public static JPanel groupsInPanel(String[] labels) {
        return groupsInPanel(labels, null);
    }

    public static JPanel groupsInPanel(String[] labels, IndexButton.ClickListener listener) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        for (int i = 0; i < labels.length ; i++) {
            IndexButton btn = new IndexButton(labels[i], i);
            if (listener != null) {
                btn.setOnListener(listener);
            }
            panel.add(btn);
        }
        panel.setBackground(Color.decode("#46494B"));
        return panel;
    }

}
