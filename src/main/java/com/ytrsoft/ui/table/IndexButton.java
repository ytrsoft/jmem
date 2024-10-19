package com.ytrsoft.ui.table;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class IndexButton extends JLabel implements MouseListener {

    interface ClickListener {
        void click(int index);
    }

    private final int index;
    private ClickListener listener;

    public IndexButton(String label, int index) {
        super(label);
        this.index = index;
        addMouseListener(this);
        setOpaque(false);
        setHorizontalAlignment(SwingConstants.CENTER);
        adjustSizeForText(label);
    }

    public void setOnListener(ClickListener listener) {
        this.listener = listener;
    }

    private void adjustSizeForText(String text) {
        FontMetrics fm = getFontMetrics(getFont());
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        int padding = 5;
        setPreferredSize(new Dimension(textWidth + padding * 2, textHeight + padding));
        setText(text);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {}

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if (this.listener != null) {
            this.listener.click(index);
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {}

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {}

    @Override
    public void mouseExited(MouseEvent mouseEvent) {}

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.decode("#46494B"));
        g2.fillRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 4, 4); // 确保背景绘制不覆盖边框
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.decode("#5A5E60"));
        g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 4, 4); // 调整位置，防止边框被覆盖
    }
}
