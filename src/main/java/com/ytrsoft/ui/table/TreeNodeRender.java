package com.ytrsoft.ui.table;

import com.ytrsoft.core.Libmem;
import com.ytrsoft.core.LmProcess;

import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

public class TreeNodeRender implements TreeCellRenderer {

    @Override
    @SuppressWarnings("unchecked")
    public Component getTreeCellRendererComponent(JTree jTree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        TreeTableNode<LmProcess> node = (TreeTableNode<LmProcess>) value;
        return new ProcessNodeIcon(node);
    }

}
