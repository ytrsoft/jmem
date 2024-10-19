package com.ytrsoft.ui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.ytrsoft.LaunchFrame;
import com.ytrsoft.core.*;
import com.ytrsoft.ui.table.*;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class SymbolFrame extends JFrame implements Refresh.ChangeListener {

    private final LmModule module;
    private final Table<LmSymbol> table;
    private List<LmSymbol> symbols;

    public SymbolFrame(LmModule module) {
        this.module = module;
        setTitle(module.getName());
        BufferedImage image = Icons.get(module.getPath());
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
        symbols = LibmemUtils.getSymbolsDemangled(module);
    }

    @Override
    public void onUpdateUI() {
        if (!symbols.isEmpty()) {
            table.setData(symbols);
        }
    }

    public void setup() {
        setVisible(true);
    }
}
