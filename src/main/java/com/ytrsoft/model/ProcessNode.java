package com.ytrsoft.model;

import com.ytrsoft.core.LmProcess;

import java.util.ArrayList;
import java.util.List;

public class ProcessNode {
    private LmProcess data;
    private List<ProcessNode> children = new ArrayList<>();

    public ProcessNode() {}

    public ProcessNode(LmProcess data) {
        this.data = data;
    }

    public LmProcess getData() {
        return data;
    }

    public void setData(LmProcess data) {
        this.data = data;
    }

    public List<ProcessNode> getChildren() {
        return children;
    }

    public void setChildren(List<ProcessNode> children) {
        this.children = children;
    }
}
