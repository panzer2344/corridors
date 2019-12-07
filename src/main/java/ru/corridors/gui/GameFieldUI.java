package ru.corridors.gui;

import ru.corridors.gui.model.UILine;
import ru.corridors.gui.model.UIPoint;
import ru.corridors.rmi.server.ServerImpl;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class GameFieldUI extends JComponent {

    private static final int pointRadius = 6;
    private static final int linesCountY = ServerImpl.DEFAULT_SQUARE_COUNT;

    private List<List<UIPoint>> points;
    private List<List<UILine>> lines;

    private final int yOffset = 50;

    public GameFieldUI() {
        super();
        setSize(400, 400);

        initPoints();
        initLines();
    }

    private void initPoints() {
        int x = 20;
        int y = 20;
        int delta = 25;

        points = new ArrayList<>(linesCountY + 1);
        for(int i = 0; i < linesCountY + 1; i++) {
            List<UIPoint> row = new ArrayList<>(linesCountY + 1);

            for(int j = 0; j < linesCountY + 1; j++) {
                UIPoint uiPoint = new UIPoint(x, y + yOffset, pointRadius);
                uiPoint.setIndexVert(i);
                uiPoint.setIndexHor(j);

                row.add(uiPoint);
                add(uiPoint);

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
            List<UILine> row = new ArrayList<>(linesCountY + 1);

            for(int j = 0; j < linesCountY + 1; j++) {
                // to exclude horizontal line for last column of points ( or will NPE )
                if(j < linesCountY) {
                    UILine lineHorizontal = new UILine(points.get(i).get(j), points.get(i).get(j + 1));
                    add(lineHorizontal);
                    row.add(lineHorizontal);

                    points.get(i).get(j).getConnections().add(lineHorizontal);
                    points.get(i).get(j + 1).getConnections().add(lineHorizontal);
                }

                // to exclude vertical line for last row of points ( or will NPE )
                if(i < linesCountY) {
                    UILine lineVertical = new UILine(points.get(i).get(j), points.get(i + 1).get(j));
                    row.add(lineVertical);
                    add(lineVertical);

                    points.get(i).get(j).getConnections().add(lineVertical);
                    points.get(i + 1).get(j).getConnections().add(lineVertical);
                }
            }
        }

    }

    public List<List<UIPoint>> getPoints() {
        return points;
    }

    public List<List<UILine>> getLines() {
        return lines;
    }
}
