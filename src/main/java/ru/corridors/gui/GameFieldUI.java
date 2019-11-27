package ru.corridors.gui;

import ru.corridors.gui.model.Line;
import ru.corridors.gui.model.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GameFieldUI extends JComponent implements MouseListener {

    private Point point;
    private Point point1;

    private Line line;

    public GameFieldUI() {
        super();
        //setBounds(100, 100, 500, 500);

        setLocation(0, 0);
        setSize(200, 200);
        setBackground(Color.DARK_GRAY);

        point = new Point(50, 50, 20);
        point1 = new Point(100, 150, 20);

        add(point);
        add(point1);

        line = new Line(50, 50, 100, 150);
        add(line);
    }

//    @Override
//    protected void paintComponent(Graphics g) {
//        //System.out.println("paint");
//        point.paintComponents(g);
//    }

    @Override
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

    }

    public static void main(String[] args) {
        JFrame jFrame = new JFrame();
        jFrame.setBounds(100, 100, 500, 500);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLayout(new BorderLayout());
        jFrame.setLocationRelativeTo(null);
        jFrame.getContentPane().add(new GameFieldUI());
        //jFrame.getContentPane().add(new Point(150, 150, 20));
        jFrame.setVisible(true);
    }
}
