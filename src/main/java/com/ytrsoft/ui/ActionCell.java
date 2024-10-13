package com.ytrsoft.ui;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionCell extends AbstractCellEditor implements TableCellEditor, TableCellRenderer, ActionListener {
    private final JPanel panel;
    private int currentRow;
    private JTable table;
    private ActionListener listener;

    public ActionCell(String... buttonLabels) {
        panel = new JPanel(new FlowLayout());
        JButton[] buttons = new JButton[buttonLabels.length];

        for (int i = 0; i < buttonLabels.length; i++) {
            buttons[i] = createButton(buttonLabels[i]);
            panel.add(buttons[i]);
        }
    }

    public void setOnActionListener(ActionListener listener) {
        this.listener = listener;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFocusable(false);
        button.setPreferredSize(new Dimension(60, 20));
        button.addActionListener(this);
        return button;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            panel.setBackground(table.getSelectionBackground());
        } else {
            panel.setBackground(table.getBackground());
        }
        return panel;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.table = table;
        this.currentRow = row;
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (listener != null) {
            ActionEvent newEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ((JButton) actionEvent.getSource()).getText());
            listener.actionPerformed(newEvent);
        }
        stopCellEditing();
    }

    public int getCurrentRow() {
        return currentRow;
    }

    public JTable getTable() {
        return table;
    }
}
