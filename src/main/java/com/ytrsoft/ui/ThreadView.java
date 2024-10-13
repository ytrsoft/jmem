package com.ytrsoft.ui;

import com.ytrsoft.core.LibmemUtils;
import com.ytrsoft.core.LmProcess;
import com.ytrsoft.core.LmThread;
import com.ytrsoft.core.UITools;
import com.ytrsoft.model.ThreadNode;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThreadView extends JFrame {

    private final JXTreeTable treeTable;

    public ThreadView(LmProcess process) {
        setTitle(process.getName());
        setSize(UITools.getDefaultSizeSM());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        treeTable = new JXTreeTable(new ThreadTreeTableModel(process));
        configureTable();
        treeTable.setRowSelectionAllowed(false);
        treeTable.setFocusable(false);
        JScrollPane scrollPane = new JScrollPane(treeTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void configureTable() {
        treeTable.setRowHeight(28);
        int[] columnWidths = {100};
        for (int i = 0; i < columnWidths.length; i++) {
            TableColumn column = treeTable.getColumn(i);
            column.setPreferredWidth(columnWidths[i]);
            DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
            renderer.setHorizontalAlignment(SwingConstants.CENTER);
            column.setCellRenderer(renderer);
        }
    }

    public void setup() {
        setVisible(true);
    }

    private static class ThreadTreeTableModel extends AbstractTreeTableModel {
        private static final String[] COLUMN_NAMES = {"ID"};
        private final ThreadNode rootNode;

        public ThreadTreeTableModel(LmProcess process) {
            this.rootNode = buildTree(LibmemUtils.getThreads(process));
        }

        private ThreadNode buildTree(List<LmThread> threads) {
            Map<Integer, ThreadNode> nodeMap = new HashMap<>();
            threads.forEach(thread -> nodeMap.put(thread.getId(), new ThreadNode(thread)));

            ThreadNode root = new ThreadNode();
            for (LmThread thread : threads) {
                ThreadNode node = nodeMap.get(thread.getId());
                ThreadNode parentNode = nodeMap.get(thread.getPid());
                if (parentNode != null) {
                    parentNode.getChildren().add(node);
                } else {
                    root.getChildren().add(node);
                }
            }
            return root;
        }

        @Override
        public int getColumnCount() {
            return COLUMN_NAMES.length;
        }

        @Override
        public String getColumnName(int column) {
            return COLUMN_NAMES[column];
        }

        @Override
        public Object getValueAt(Object node, int column) {
            ThreadNode threadNode = (ThreadNode) node;
            LmThread thread = threadNode.getData();
            if (thread != null) {
                if (column == 0) return thread.getId();
            }
            return null;
        }

        @Override
        public Object getChild(Object parent, int index) {
            ThreadNode node = (ThreadNode) parent;
            return node.getChildren().get(index);
        }

        @Override
        public int getChildCount(Object parent) {
            ThreadNode node = (ThreadNode) parent;
            return node.getChildren().size();
        }

        @Override
        public int getIndexOfChild(Object parent, Object child) {
            ThreadNode node = (ThreadNode) parent;
            return node.getChildren().indexOf(child);
        }

        @Override
        public boolean isLeaf(Object node) {
            ThreadNode threadNode = (ThreadNode) node;
            return threadNode.getChildren().isEmpty();
        }

        @Override
        public Object getRoot() {
            return rootNode;
        }
    }
}
