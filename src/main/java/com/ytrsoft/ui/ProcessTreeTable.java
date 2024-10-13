package com.ytrsoft.ui;

import com.ytrsoft.core.LibmemUtils;
import com.ytrsoft.core.LmProcess;
import com.ytrsoft.core.TimeUtils;
import com.ytrsoft.core.UITools;
import com.ytrsoft.model.ProcessNode;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessTreeTable extends JPanel {

    private final JXTreeTable treeTable;

    public ProcessTreeTable() {
        setLayout(new BorderLayout());
        treeTable = new JXTreeTable(new ProcessTreeTableModel());
        treeTable.setRowSelectionAllowed(false);
        treeTable.setFocusable(false);
        columnCenter(1);
        columnCenter(2);
        double width = UITools.getDefaultSize().getWidth();
        columnWidth(0, (int) (width * 0.2));
        columnWidth(3, (int) (width * 0.1));
        columnWidth(4, (int) (width * 0.3));
        columnWidth(5, (int) (width * 0.2));
        TableColumn action = treeTable.getColumn(5);
        ActionEditor editor = new ActionEditor();
        editor.setOnNodeActionListener(this::onClick);
        action.setCellEditor(editor);
        TableCellRenderer renderer = new TableCellRenderer();
        action.setCellRenderer(renderer);
        treeTable.setRowHeight(28);
        JScrollPane scrollPane = new JScrollPane(treeTable);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void columnCenter(int index) {
        TableColumn column = treeTable.getColumn(index);
        DefaultTableCellRenderer render = new DefaultTableCellRenderer();
        render.setHorizontalAlignment(SwingConstants.CENTER);
        column.setCellRenderer(render);
    }

    private void columnWidth(int index, int width) {
        TableColumn column = treeTable.getColumn(index);
        column.setPreferredWidth(width);
    }

    public void onClick(int index, LmProcess process) {
        if (index == 0) {
            new ThreadView(process).setup();
        }
        if (index == 1) {
            new ModuleView(process).setup();
        }
        if (index == 2) {
            new SegmentView(process).setup();
        }
    }

    private static class TableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (column == 5) {
                return new ActionEditor().getTableCellEditorComponent(table, value, isSelected, row, column);
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }

    public static class ActionEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
        private final JPanel panel;
        private final JButton threadButton;
        private final JButton moduleButton;
        private final JButton segmentButton;
        private final JButton scanButton;
        private int currentRow;
        private JXTreeTable table;
        private NodeActionListener listener;

        interface NodeActionListener {
            void onClick(int index, LmProcess process);
        }

        public ActionEditor() {
            panel = new JPanel(new FlowLayout());
            panel.setBackground(Color.decode("#46494B"));
            threadButton = createButton("线程");
            moduleButton = createButton("模块");
            segmentButton = createButton("分段");
            scanButton = createButton("扫描");

            panel.add(threadButton);
            panel.add(moduleButton);
            panel.add(segmentButton);
            panel.add(scanButton);
        }

        public void setOnNodeActionListener(NodeActionListener listener) {
            this.listener = listener;
        }

        private JButton createButton(String text) {
            JButton button = new JButton(text);
            button.setFocusable(false);
            button.setPreferredSize(new Dimension(60, 20));
            button.addActionListener(this);
            return button;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.table = (JXTreeTable) table;
            this.currentRow = row;
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            ProcessTreeTableModel model = (ProcessTreeTableModel) table.getTreeTableModel();
            ProcessNode node = (ProcessNode) model.getChild(model.getRoot(), currentRow);
            LmProcess process = node.getData();
            Object source = actionEvent.getSource();
            if (listener != null) {
                if (source == threadButton) {
                    listener.onClick(0, process);
                } else if (source == moduleButton) {
                    listener.onClick(1, process);
                } else if (source == segmentButton) {
                    listener.onClick(2, process);
                } else if (source == scanButton) {
                    listener.onClick(3, process);
                }
            }
            stopCellEditing();
        }
    }

    private static class ProcessTreeTableModel extends AbstractTreeTableModel {
        private static final String[] COLUMN_HEADERS = {
                "名称", "ID", "架构", "启动时间", "路径", "操作"
        };

        private final ProcessNode rootNode;

        public ProcessTreeTableModel() {
            List<LmProcess> processes = LibmemUtils.getProcesses();
            this.rootNode = makeRoot(processes);
        }

        private Map<Integer, ProcessNode> makeMap(List<LmProcess> processes) {
            Map<Integer, ProcessNode> nodeMap = new HashMap<>();
            processes.forEach((p) -> nodeMap.put(p.getId(), new ProcessNode(p)));
            return nodeMap;
        }

        private ProcessNode makeRoot(List<LmProcess> processes) {
            Map<Integer, ProcessNode> nodeMap = makeMap(processes);
            ProcessNode root = new ProcessNode();

            for (LmProcess process : processes) {
                ProcessNode node = nodeMap.get(process.getId());
                ProcessNode parent = nodeMap.get(process.getPid());

                if (parent != null) {
                    parent.getChildren().add(node);
                } else {
                    root.getChildren().add(node);
                }
            }
            return root;
        }

        @Override
        public int getColumnCount() {
            return COLUMN_HEADERS.length;
        }

        @Override
        public String getColumnName(int column) {
            return COLUMN_HEADERS[column];
        }

        @Override
        public Object getValueAt(Object o, int column) {
            ProcessNode node = (ProcessNode) o;
            LmProcess process = node.getData();
            switch (column) {
                case 0: return process.getName();
                case 1: return process.getId();
                case 2: return process.getArch().name();
                case 3: return TimeUtils.formatter(process.getStartTime());
                case 4: return process.getPath();
            }
            return null;
        }

        @Override
        public Object getChild(Object o, int index) {
            ProcessNode node = (ProcessNode) o;
            return node.getChildren().get(index);
        }

        @Override
        public int getChildCount(Object o) {
            ProcessNode node = (ProcessNode) o;
            return node.getChildren().size();
        }

        @Override
        public int getIndexOfChild(Object p, Object c) {
            ProcessNode parent = (ProcessNode) p;
            ProcessNode child = (ProcessNode) c;
            return parent.getChildren().indexOf(child);
        }

        @Override
        public boolean isLeaf(Object o) {
            ProcessNode node = (ProcessNode) o;
            return node.getChildren().isEmpty();
        }

        @Override
        public boolean isCellEditable(Object node, int column) {
            return column == 5;
        }

        @Override
        public Object getRoot() {
            return rootNode;
        }
    }
}
