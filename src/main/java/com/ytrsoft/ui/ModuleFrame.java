package com.ytrsoft.ui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.ytrsoft.LaunchFrame;
import com.ytrsoft.core.*;
import com.ytrsoft.ui.table.*;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class ModuleFrame extends JFrame implements Refresh.ChangeListener, ActionEditor.SelectionListener {

    private final LmProcess process;
    private final Table<LmModule> table;
    private List<LmModule> modules;
    private static final String[] ACTIONS = {
      "符号"
    };

    public ModuleFrame(LmProcess process) {
        this.process = process;
        setTitle(process.getName());
        BufferedImage image = Icons.get(process.getPath());
        if (image != null) {
            setIconImage(image);
        }
        setSize(UITools.getDefaultSize());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        table = new Table<>();
        table.setEdit(true);
        table.setOnSelectionListener(this);
        getContentPane().add(table.getScrollView());
        Refresh refresh = new Refresh();
        refresh.setOnChangeListener(this);
        refresh.start();
    }


    @Override
    public void onTask() {
        modules = LibmemUtils.getModules(process);
    }

    @Override
    public void onUpdateUI() {
        if (!modules.isEmpty()) {
            table.setData(modules);
            table.setActions(ACTIONS, 40);
        }
    }

    public void setup() {
        setVisible(true);
    }

    @Override
    public void select(Selection selection) {
        LmModule module = modules.get(selection.getRow());
        new SymbolFrame(module).setup();
    }
}
