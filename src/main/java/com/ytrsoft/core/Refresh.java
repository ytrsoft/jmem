package com.ytrsoft.core;

import javax.swing.*;

public class Refresh extends Thread {

    public interface ChangeListener {
        void onTask();
        void onUpdateUI();
    }

    private ChangeListener listener;

    public void setOnChangeListener(ChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        try {
            if (listener != null) {
                listener.onTask();
            }
            SwingUtilities.invokeLater(() -> {
                if (listener != null) {
                    listener.onUpdateUI();
                }
            });
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
