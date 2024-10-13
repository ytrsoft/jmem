package com.ytrsoft.model;

import com.ytrsoft.core.LmThread;

import java.util.ArrayList;
import java.util.List;

public class ThreadNode {
    private LmThread data;
    private List<ThreadNode> children = new ArrayList<>();

    public ThreadNode() {}

    public ThreadNode(LmThread data) {
        this.data = data;
    }

    public LmThread getData() {
        return data;
    }

    public void setData(LmThread data) {
        this.data = data;
    }

    public List<ThreadNode> getChildren() {
        return children;
    }

    public void setChildren(List<ThreadNode> children) {
        this.children = children;
    }
}
