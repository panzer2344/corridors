package ru.corridors.gui.action;

import java.awt.*;
import java.awt.geom.Line2D;

//@Deprecated
public class DrawLineAction implements DrawAction {

    private int x1;
    private int y1;
    private int x2;
    private int y2;

    public DrawLineAction(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    @Override
    public void draw(Graphics g) {
        Line2D line2D = new Line2D.Float(x1, y1, x2, y2);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);
        g2d.draw(line2D);
    }
}
