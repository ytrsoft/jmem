package com.ytrsoft.ui.table;

import org.jdesktop.swingx.JXTreeTable;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.List;

public class TreeTable<T> extends JXTreeTable implements ActionEditor.SelectionListener {

    private boolean isEdit;
    private static final int ROW_HEIGHT = 32;
    private ActionEditor.SelectionListener listener;

    public TreeTable() {
        setRowHeight(ROW_HEIGHT);
        setTableHeader();
        setRowSelectionAllowed(false);
        setColumnSelectionAllowed(false);
        setFocusable(false);
        setShowGrid(true);
        setIntercellSpacing(new Dimension(1, 1));
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setOnSelectionListener(ActionEditor.SelectionListener listener) {
        this.listener = listener;
    }

    @SuppressWarnings("unchecked")
    public void setRoot(TreeTableNode<T> root, Class<T> type) {
        TreeTableModel<T> model = new TreeTableModel<>();
        model.setRoot(root, type);
        setTreeTableModel(model);
        List<Field> annotatedFields = TableMinix.extractAnnotatedFields(type);
        TableMinix.configureTableColumns(annotatedFields, this);
    }

    private void setTableHeader() {
        JTableHeader header = getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), ROW_HEIGHT));
    }

    public void setActions(String[] labels, int width) {
        TableMinix.setActions(this, this, labels, width);
    }

    public JScrollPane getScrollView() {
        JScrollPane pane = new JScrollPane(this);
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        return pane;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return getColumnCount() - 1 == column && isEdit;
    }

    @Override
    public void select(Selection selection) {
        if (listener != null) {
            listener.select(selection);
        }
    }
}
