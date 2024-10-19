package com.ytrsoft.ui.table;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ActionRender extends JPanel implements TableCellRenderer {

    private final JPanel root;

    public ActionRender(String... labels) {
        this.root = ActionFactory.groupsInPanel(labels);
    }

    @Override
    public Component getTableCellRendererComponent(JTable jTable, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return root;
    }

}
