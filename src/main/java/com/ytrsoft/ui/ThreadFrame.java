package com.ytrsoft.ui;

import com.ytrsoft.core.*;
import com.ytrsoft.ui.table.TreeTable;
import com.ytrsoft.ui.table.TreeTableNode;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThreadFrame extends JFrame implements Refresh.ChangeListener {

    private final LmProcess process;
    private final TreeTable<LmThread> table;
    private TreeTableNode<LmThread> root;

    public ThreadFrame(LmProcess process) {
        this.process = process;
        setTitle(process.getName());
        BufferedImage image = Icons.get(process.getPath());
        if (image != null) {
            setIconImage(image);
        }
        setSize(UITools.getDefaultSizeSM());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        table = new TreeTable<>();
        getContentPane().add(table.getScrollView());
        Refresh refresh = new Refresh();
        refresh.setOnChangeListener(this);
        refresh.start();
    }

    private static Map<Integer, TreeTableNode<LmThread>> makeMap(List<LmThread> threads) {
        Map<Integer, TreeTableNode<LmThread>> nodeMap = new HashMap<>();
        threads.forEach((p) -> nodeMap.put(p.getId(), new TreeTableNode<>(p)));
        return nodeMap;
    }

    private static TreeTableNode<LmThread> makeRoot(List<LmThread> threads) {
        Map<Integer, TreeTableNode<LmThread>> nodeMap = makeMap(threads);
        TreeTableNode<LmThread> root = new TreeTableNode<>();
        for (LmThread thread : threads) {
            TreeTableNode<LmThread> node = nodeMap.get(thread.getId());
            TreeTableNode<LmThread> parent = nodeMap.get(thread.getPid());
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
        root = makeRoot(LibmemUtils.getThreads(process));
    }

    @Override
    public void onUpdateUI() {
        table.setRoot(root, LmThread.class);
    }

    public void setup() {
        setVisible(true);
    }

}
