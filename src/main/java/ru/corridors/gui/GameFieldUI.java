package ru.corridors.gui;

import ru.corridors.gui.model.Line;
import ru.corridors.gui.model.Point;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameFieldUI extends JComponent {

    private static final int pointRadius = 5;
    private static final int linesCountY = 10;

    private List<List<Point>> points;
    private List<List<Line>> lines;

    public GameFieldUI() {
        super();

        setLocation(0, 0);
        setSize(200, 200);
        setBackground(Color.DARK_GRAY);

        initPoints();
        initLines();
    }

    private void initPoints() {
        int x = 20;
        int y = 20;
        int delta = 25;

        points = new ArrayList<>(linesCountY + 1);
        for(int i = 0; i < linesCountY + 1; i++) {
            List<Point> row = new ArrayList<>(linesCountY + 1);

            for(int j = 0; j < linesCountY + 1; j++) {
                Point point = new Point(x, y, pointRadius);

                row.add(point);
                add(point);

                x += 2 * pointRadius + delta;
            }

            points.add(row);

            x = 20;
            y += 2 * pointRadius + delta;
        }
    }

    private void initLines() {
        lines = new ArrayList<>(linesCountY + 1);

        for(int i = 0; i < linesCountY + 1; i++) {
            List<Line> row = new ArrayList<>(linesCountY + 1);

            for(int j = 0; j < linesCountY + 1; j++) {
                if(j < linesCountY) {
                    Line lineHorizontal = new Line(points.get(i).get(j), points.get(i).get(j + 1));
                    add(lineHorizontal);
                    row.add(lineHorizontal);
                }

                if(i < linesCountY) {
                    Line lineVertical = new Line(points.get(i).get(j), points.get(i + 1).get(j));
                    row.add(lineVertical);
                    add(lineVertical);
                }
            }
        }

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
        //jFrame.repaint();
    }
}
