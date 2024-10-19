package com.ytrsoft.ui;

import com.ytrsoft.core.*;
import com.ytrsoft.ui.table.*;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class SegmentFrame extends JFrame implements Refresh.ChangeListener {

    private final LmProcess process;
    private final Table<LmSegment> table;
    private List<LmSegment> segments;

    public SegmentFrame(LmProcess process) {
        this.process = process;
        setTitle(process.getName());
        BufferedImage image = Icons.get(process.getPath());
        if (image != null) {
            setIconImage(image);
        }
        setSize(UITools.getDefaultSizeSM());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        table = new Table<>();
        getContentPane().add(table.getScrollView());
        Refresh refresh = new Refresh();
        refresh.setOnChangeListener(this);
        refresh.start();
    }


    @Override
    public void onTask() {
        segments = LibmemUtils.getMemorySegments(process);
    }

    @Override
    public void onUpdateUI() {
        if (!segments.isEmpty()) {
            table.setData(segments);
        }
    }

    public void setup() {
        setVisible(true);
    }
}
