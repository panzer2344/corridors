package ru.corridors.gui.model;

import javax.swing.*;
import java.awt.*;

public class ConnectionLine extends JComponent {

    private PointBtn startBtn;
    private PointBtn endBtn;

    public ConnectionLine() {}

    @Override
    protected void printComponent(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawLine(startBtn.getX(), startBtn.getY(), endBtn.getX(), endBtn.getY());
    }
}
