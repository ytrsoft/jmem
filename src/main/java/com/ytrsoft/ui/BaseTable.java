package com.ytrsoft.ui;

import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class BaseTable extends JXTable {

    private final boolean isEdit;

    public BaseTable(DefaultTableModel tableModel) {
        this(tableModel, false);
    }

    public BaseTable(DefaultTableModel tableModel, boolean isEdit) {
        super(tableModel);
        setRowHeight(28);
        centerAlignColumns();
        setRowSelectionAllowed(false);
        setFocusable(false);
        this.isEdit = isEdit;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return isEdit;
    }

    private void centerAlignColumns() {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < getColumnCount(); i++) {
            TableColumn column = getColumnModel().getColumn(i);
            column.setCellRenderer(centerRenderer);
        }
    }

}
