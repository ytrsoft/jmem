package com.ytrsoft.ui.table;

import java.util.ArrayList;
import java.util.List;

public class TreeTableNode<T> {

    private T data;

    private List<TreeTableNode<T>> children = new ArrayList<>();

    public TreeTableNode() {}

    public TreeTableNode(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<TreeTableNode<T>> getChildren() {
        return children;
    }

    public void setChildren(List<TreeTableNode<T>> children) {
        this.children = children;
    }
}
