package com.ytrsoft.ui;

import com.ytrsoft.core.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.List;

public class ModuleView extends JFrame {

    private final LmProcess process;

    public ModuleView(LmProcess process) {
        this.process = process;
        setTitle(process.getName());
        setSize(UITools.getDefaultSizeSM());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public void setup() {
        List<LmModule> modules = LibmemUtils.getModules(process);

        String[] columnNames = {"基址", "地址", "大小", "路径", "名称", "操作"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        for (LmModule module : modules) {
            Object[] rowData = {
                Long.toHexString(module.getBase()).toUpperCase(),
                Long.toHexString(module.getEnd()).toUpperCase(),
                FileUtils.formatter(module.getSize()),
                module.getPath(),
                module.getName(),
            };
            tableModel.addRow(rowData);
        }

        BaseTable moduleTable = new BaseTable(tableModel, true);

        ActionCell actionCell = new ActionCell("符号");
        actionCell.setOnActionListener(e -> {
            int row = actionCell.getCurrentRow();
            LmModule module = modules.get(row);
            new SymbolView(module).setup();
        });

        TableColumn column = moduleTable.getColumn("操作");
        column.setCellRenderer(actionCell);
        column.setCellEditor(actionCell);

        JScrollPane scrollPane = new JScrollPane(moduleTable);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }
}
