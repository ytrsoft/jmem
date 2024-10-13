package com.ytrsoft.ui;

import com.ytrsoft.core.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SymbolView extends JFrame {

    private final LmModule module;

    public SymbolView(LmModule module) {
        this.module = module;
        setTitle(module.getName());
        setSize(UITools.getDefaultSizeSM());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public void setup() {
        List<LmSymbol> symbols = LibmemUtils.getSymbolsDemangled(module);

        String[] columnNames = {"名称", "地址"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        for (LmSymbol symbol : symbols) {
            Object[] rowData = {
                symbol.getName(),
                Long.toHexString(symbol.getAddress()).toUpperCase()
            };
            tableModel.addRow(rowData);
        }

        BaseTable symbolTable = new BaseTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(symbolTable);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }
}
