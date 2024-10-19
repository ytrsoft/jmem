package com.ytrsoft.ui.table;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

public class ActionEditor extends AbstractCellEditor implements TableCellEditor, IndexButton.ClickListener {

    public interface SelectionListener {
        void select(Selection selection);
    }

    private final JPanel panel;
    private final Selection selection;
    private SelectionListener listener;

    public ActionEditor(String... labels) {
        selection = new Selection();
        panel = ActionFactory.groupsInPanel(labels, this);
    }

    public void setOnSelectListener(SelectionListener listener) {
        this.listener = listener;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        selection.setPushed(true);
        selection.setTable(table);
        selection.setSelected(isSelected);
        selection.setRow(row);
        selection.setColumn(column);
        selection.setValue(value);
        panel.setOpaque(false);
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        selection.setPushed(false);
        return selection.getValue();
    }

    @Override
    public void click(int index) {
        fireEditingCanceled();
        if (listener != null) {
            selection.setIndex(index);
            listener.select(selection);
        }
    }

}
