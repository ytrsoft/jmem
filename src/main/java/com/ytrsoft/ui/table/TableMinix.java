package com.ytrsoft.ui.table;

import com.ytrsoft.core.LmProcess;
import com.ytrsoft.model.ProcessNode;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TableMinix<T> {

    private TableMinix() {
        throw new UnsupportedOperationException();
    }

    public static <T> List<Field> extractAnnotatedFields(Class<T> type) {
        List<Field> annotatedFields = new ArrayList<>();
        for (Field field : type.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                field.setAccessible(true);
                annotatedFields.add(field);
            }
        }
        return annotatedFields;
    }

    public static <T> void configureTableColumns(List<Field> annotatedFields, JTable table) {
        int columnIndex = 0;
        for (Field field : annotatedFields) {
            Column columnAnnotation = field.getAnnotation(Column.class);
            if (columnAnnotation != null) {
                TableColumn column = table.getColumnModel().getColumn(columnIndex);
                int width = columnAnnotation.width();
                if (width > 0) {
                    column.setPreferredWidth(width);
                }
                if (columnAnnotation.center()) {
                    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                    centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
                    column.setCellRenderer(centerRenderer);
                }
            }
            columnIndex++;
        }
    }

    public static void setActions(JTable table, ActionEditor.SelectionListener listener, String[] labels, int width) {
        TableColumn actionColumn = new TableColumn();
        actionColumn.setHeaderValue("操作");
        actionColumn.setCellRenderer(new ActionRender(labels));
        ActionEditor editor = new ActionEditor(labels);
        editor.setOnSelectListener(listener);
        actionColumn.setCellEditor(editor);
        actionColumn.setPreferredWidth(width);
        table.addColumn(actionColumn);
    }
}

