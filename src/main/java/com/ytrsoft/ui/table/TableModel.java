package com.ytrsoft.ui.table;

import com.ytrsoft.utils.Transform;

import javax.swing.table.AbstractTableModel;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TableModel<T> extends AbstractTableModel {

    private final List<T> data;
    private final List<Field> annotatedFields;

    public TableModel(List<T> data, Class<T> type) {
        this.data = data;
        this.annotatedFields = new ArrayList<>();
        extractAnnotatedFields(type);
    }

    private void extractAnnotatedFields(Class<T> type) {
        for (Field field : type.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                field.setAccessible(true);
                annotatedFields.add(field);
            }
        }
    }

    @Override
    public int getColumnCount() {
        return annotatedFields.size();
    }

    @Override
    public String getColumnName(int column) {
        Field field = annotatedFields.get(column);
        Column columnAnnotation = field.getAnnotation(Column.class);
        return columnAnnotation.value();
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        T rowData = data.get(rowIndex);
        if (columnIndex >= 0 && columnIndex < annotatedFields.size()) {
            try {
                Field field = annotatedFields.get(columnIndex);
                return FormatterUtils.get(field, rowData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
