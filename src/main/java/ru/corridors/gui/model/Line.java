package ru.corridors.gui.model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Line extends JComponent /*implements MouseListener*/ {

    private int fromX;
    private int fromY;
    private int toX;
    private int toY;

    private final Stroke stroke = new BasicStroke(5);

    public Line(Point from, Point to) {
        super();
        init(from.get_X(), from.get_Y(), to.get_X(), to.get_Y());
//        int x0 = Math.min(from.getX(), to.getX());
//        int y0 = Math.min(from.getY(), to.getY());
//
//        setLocation(x0, y0);
//        setSize(Math.abs(to.getX() - from.getX()), Math.abs(to.getY() - from.getY()));
//
//        this.fromX = fromX - x0;
//        this.fromY = fromY - y0;
//
//        this.toX = toX - x0;
//        this.toY = toY - y0;
    }

    public Line(int fromX, int fromY, int toX, int toY) {
        super();
        init(fromX, fromY, toX, toY);

        //        this.fromX = fromX;
//        this.fromY = fromY;
//        this.toX = toX;
//        this.toY = toY;

//        int x0 = Math.min(fromX, toX);
//        int y0 = Math.min(fromY, toY);
//
//        setLocation(x0, y0);
//        setSize(Math.abs(toX - fromX), Math.abs(toY - fromY));
//
//        this.fromX = fromX - x0;
//        this.fromY = fromY - y0;
//
//        this.toX = toX - x0;
//        this.toY = toY - y0;
    }

    private void init(int fromX, int fromY, int toX, int toY) {
        int x0 = Math.min(fromX, toX);
        int y0 = Math.min(fromY, toY);

        setLocation(x0, y0);
        setSize(Math.max(Math.abs(toX - fromX), 2), Math.max(Math.abs(toY - fromY), 2));

        this.fromX = fromX - x0;
        this.fromY = fromY - y0;

        this.toX = toX - x0;
        this.toY = toY - y0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);
        g2d.setStroke(stroke);
        g2d.drawLine(fromX, fromY, toX, toY);

        //System.out.println("draw");
    }

   /* @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }*/
}