package com.ytrsoft.ui;

import com.ytrsoft.core.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SegmentView extends JFrame {

    private LmProcess process;

    public SegmentView(LmProcess process) {
        this.process = process;
        setTitle(process.getName());
        setSize(UITools.getDefaultSizeSM());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public void setup() {
        List<LmSegment> segments = LibmemUtils.getMemorySegments(process);
        String[] columnNames = {"基址", "地址", "大小", "权限"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        for (LmSegment segment : segments) {
            Object[] rowData = {
                Long.toHexString(segment.getBase()).toUpperCase(),
                Long.toHexString(segment.getEnd()).toUpperCase(),
                FileUtils.formatter(segment.getSize()),
                Protection.valueOf(segment.getProt())
            };
            tableModel.addRow(rowData);
        }

        BaseTable segmentTable = new BaseTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(segmentTable);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }
}
