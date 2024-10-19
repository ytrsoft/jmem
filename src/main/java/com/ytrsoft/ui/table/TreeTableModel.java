package com.ytrsoft.ui.table;

import com.ytrsoft.utils.Transform;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TreeTableModel<T> extends AbstractTreeTableModel {

    private TreeTableNode<T> root;
    private final List<Field> annotatedFields;

    public TreeTableModel() {
        this.annotatedFields = new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    public void setRoot(TreeTableNode<T> root, Class<T> type) {
        this.root = root;
        extractAnnotatedFields(type);
        modelSupport.fireNewRoot();
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
    @SuppressWarnings("unchecked")
    public Object getValueAt(Object node, int columnIndex) {
        TreeTableNode<T> treeNode = (TreeTableNode<T>) node;
        T data = treeNode.getData();
        if (data == null) {
            return null;
        }
        if (columnIndex >= 0 && columnIndex < annotatedFields.size()) {
            try {
                Field field = annotatedFields.get(columnIndex);
                if (field == null) {
                    return null;
                }
                return FormatterUtils.get(field, data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    @Override
    @SuppressWarnings("unchecked")
    public Object getChild(Object parent, int index) {
        TreeTableNode<T> treeNode = (TreeTableNode<T>) parent;
        return treeNode.getChildren().get(index);
    }

    @Override
    @SuppressWarnings("unchecked")
    public int getChildCount(Object parent) {
        TreeTableNode<T> treeNode = (TreeTableNode<T>) parent;
        return treeNode.getChildren().size();
    }

    @Override
    @SuppressWarnings("unchecked")
    public int getIndexOfChild(Object parent, Object child) {
        TreeTableNode<T> cn = (TreeTableNode<T>) child;
        TreeTableNode<T> pn = (TreeTableNode<T>) parent;
        return pn.getChildren().indexOf(cn);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean isLeaf(Object node) {
        TreeTableNode<T> treeNode = (TreeTableNode<T>) node;
        return treeNode.getChildren().isEmpty();
    }

    @Override
    public Object getRoot() {
        return root;
    }

}
