package com.ytrsoft;

import com.formdev.flatlaf.FlatDarkLaf;
import com.ytrsoft.core.LibmemUtils;
import com.ytrsoft.core.LmProcess;
import com.ytrsoft.core.Refresh;
import com.ytrsoft.core.UITools;
import com.ytrsoft.ui.ModuleFrame;
import com.ytrsoft.ui.SegmentFrame;
import com.ytrsoft.ui.ThreadFrame;
import com.ytrsoft.ui.table.*;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LaunchFrame extends JFrame implements Refresh.ChangeListener, ActionEditor.SelectionListener {

    private List<LmProcess> processes;
    private final TreeTable<LmProcess> table;
    private TreeTableNode<LmProcess> root;
    private static final String[] ACTIONS = {
      "线程", "模块", "分段"
    };

    public LaunchFrame() {
        setTitle("Libmem");
        setSize(UITools.getDefaultSize());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        table = new TreeTable<>();
        table.setEdit(true);
        table.setOnSelectionListener(this);
        table.setTreeCellRenderer(new TreeNodeRender());
        getContentPane().add(table.getScrollView());
        Refresh refresh = new Refresh();
        refresh.setOnChangeListener(this);
        refresh.start();
    }

    private static Map<Integer, TreeTableNode<LmProcess>> makeMap(List<LmProcess> processes) {
        Map<Integer, TreeTableNode<LmProcess>> nodeMap = new HashMap<>();
        processes.forEach((p) -> nodeMap.put(p.getId(), new TreeTableNode<>(p)));
        return nodeMap;
    }

    private static TreeTableNode<LmProcess> makeRoot(List<LmProcess> processes) {
        Map<Integer, TreeTableNode<LmProcess>> nodeMap = makeMap(processes);
        TreeTableNode<LmProcess> root = new TreeTableNode<>();
        for (LmProcess process : processes) {
            TreeTableNode<LmProcess> node = nodeMap.get(process.getId());
            TreeTableNode<LmProcess> parent = nodeMap.get(process.getPid());
            if (parent != null) {
                parent.getChildren().add(node);
            } else {
                root.getChildren().add(node);
            }
        }
        return root;
    }

    @Override
    public void onTask() {
        processes = LibmemUtils.getProcesses();
        root = makeRoot(LibmemUtils.getProcesses());
    }

    @Override
    public void onUpdateUI() {
        table.setRoot(root, LmProcess.class);
        table.setActions(ACTIONS, 120);
    }

    public void setup() {
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FlatDarkLaf.setup();
            new LaunchFrame().setup();
        });
    }

    private void toThreadFrame(int row) {
        LmProcess process = processes.get(row);
        new ThreadFrame(process).setup();
    }

    private void toModuleFrame(int row) {
        LmProcess process = processes.get(row);
        new ModuleFrame(process).setup();
    }

    private void toSegmentFrame(int row) {
        LmProcess process = processes.get(row);
        new SegmentFrame(process).setup();
    }

    @Override
    public void select(Selection selection) {
        int row = selection.getRow();
        switch (selection.getIndex()) {
            case 0:
                toThreadFrame(row);
                break;
            case 1:
                toModuleFrame(row);
                break;
            case 2:
                toSegmentFrame(row);
                break;
        }
    }
}
