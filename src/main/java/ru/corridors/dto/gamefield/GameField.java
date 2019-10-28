package ru.corridors.dto.gamefield;

import ru.corridors.dto.Line;
import ru.corridors.dto.Point;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GameField implements Serializable {
    private int pointsCount;
    private int squaresCount;

    private Map<Point, Integer> squares;
    private List<Line> lines;

    public GameField(int squaresCount) {
        this.squaresCount = squaresCount;
        pointsCount = squaresCount + 1;

        squares = new ConcurrentHashMap<>(squaresCount);
        lines = new ArrayList<>(3 * (pointsCount - 1) * (pointsCount - 1));
    }

    public int getPointsCount() {
        return pointsCount;
    }

    public void setPointsCount(int pointsCount) {
        this.pointsCount = pointsCount;
    }

    public int getSquaresCount() {
        return squaresCount;
    }

    public void setSquaresCount(int squaresCount) {
        this.squaresCount = squaresCount;
    }

    public Map<Point, Integer> getSquares() {
        return squares;
    }

    public void setSquares(Map<Point, Integer> squares) {
        this.squares = squares;
    }

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    public boolean isReachable(Point point) {
        return point.getX() >= 0 &&
            point.getX() < pointsCount &&
            point.getY() >= 0 &&
            point.getY() < pointsCount;
    }

    public boolean isLockLeft(Line line) {
        return isLockSide(line, getPossibleLeftNeighbors(line));
    }

    public boolean isLockRight(Line line) {
        return isLockSide(line, getPossibleRightNeighbors(line));
    }

    public boolean isLockTop(Line line) {
        return isLockSide(line, getPossibleTopNeighbors(line));
    }

    public boolean isLockBot(Line line) {
        return isLockSide(line, getPossibleBotNeighbors(line));
    }

    private boolean isLockSide(Line line, List<Line> sideLines) {
        boolean isLockSide = true;

        for(Line neighbor : sideLines) {
            if( ! lines.contains(neighbor) ) {
                isLockSide = false;
            }
        }

        return isLockSide;
    }

    public List<Line> getPossibleNeighborLines(Line line) {
        LinkedList<Line> neighbors = new LinkedList<>();

        if(isBoundary(line)) {
            Line line1 = null, rotated = null, line3 = null;

            if(isRightBoundary(line)) {
                line1 = line.getParallelLineWithXDelta(-1);
                rotated = line1.rotate(Line.Angle.CLOCKWISE_90);
                line3 = rotated.getParallelLineWithYDelta(1);
            } else if(isLeftBoundary(line)) {
                line1 = line.getParallelLineWithXDelta(1);
                rotated = line.rotate(Line.Angle.CLOCKWISE_90);
                line3 = rotated.getParallelLineWithYDelta(1);
            } else if(isTopBoundary(line)) {
                line1 = line.getParallelLineWithYDelta(-1);
                rotated = line.rotate(Line.Angle.CLOCKWISE_90);
                line3 = rotated.getParallelLineWithXDelta(1);
            } else if(isBottomBoundary(line)) {
                line1 = line.getParallelLineWithYDelta(1);
                rotated = line1.rotate(Line.Angle.CLOCKWISE_90);
                line3 = rotated.getParallelLineWithXDelta(1);
            }

            neighbors.add(line1);
            neighbors.add(rotated);
            neighbors.add(line3);
        } else {
            Line line1, line2, rotated, line4, line5, line6;

            if(line.isVerticalLine()) {
                line1 = line.getParallelLineWithXDelta(-1);
                line2 = line.getParallelLineWithXDelta(1);
                rotated = line.rotate(Line.Angle.CLOCKWISE_90);
                line4 = rotated.getParallelLineWithXDelta(-1);
                line5 = rotated.getParallelLineWithYDelta(1);
                line6 = line4.getParallelLineWithYDelta(1);
            } else {
                line1 = line.getParallelLineWithYDelta(-1);
                line2 = line.getParallelLineWithYDelta(1);
                rotated = line.rotate(Line.Angle.CLOCKWISE_90);
                line4 = rotated.getParallelLineWithYDelta(-1);
                line5 = rotated.getParallelLineWithXDelta(1);
                line6 = line4.getParallelLineWithXDelta(1);
            }

            neighbors.add(line1);
            neighbors.add(line2);
            neighbors.add(rotated);
            neighbors.add(line4);
            neighbors.add(line5);
            neighbors.add(line6);
        }

        return neighbors;
    }

    public List<Line> getPossibleBotNeighbors(Line line) {
        List<Line> neighbors = new LinkedList<>();

        Line line1 = line.getParallelLineWithYDelta(-1);
        Line rotated = line.rotate(Line.Angle.CLOCKWISE_90);
        Line line3 = rotated.getParallelLineWithXDelta(1);

        neighbors.add(line1);
        neighbors.add(rotated);
        neighbors.add(line3);

        return neighbors;
    }

    public List<Line> getPossibleTopNeighbors(Line line) {
        List<Line> neighbors = new LinkedList<>();

        Line line1 = line.getParallelLineWithYDelta(1);
        Line rotated = line1.rotate(Line.Angle.CLOCKWISE_90);
        Line line3 = rotated.getParallelLineWithXDelta(1);

        neighbors.add(line1);
        neighbors.add(rotated);
        neighbors.add(line3);

        return neighbors;
    }

    public List<Line> getPossibleLeftNeighbors(Line line) {
        List<Line> neighbors = new LinkedList<>();

        Line line1 = line.getParallelLineWithXDelta(-1);
        Line rotated = line1.rotate(Line.Angle.CLOCKWISE_90);
        Line line3 = rotated.getParallelLineWithYDelta(1);

        neighbors.add(line1);
        neighbors.add(rotated);
        neighbors.add(line3);

        return neighbors;
    }

    public List<Line> getPossibleRightNeighbors(Line line) {
        List<Line> neighbors = new LinkedList<>();

        Line line1 = line.getParallelLineWithXDelta(1);
        Line rotated = line.rotate(Line.Angle.CLOCKWISE_90);
        Line line3 = rotated.getParallelLineWithYDelta(1);

        neighbors.add(line1);
        neighbors.add(rotated);
        neighbors.add(line3);

        return neighbors;
    }

    public boolean isBoundary(Line line) {
        return isTopBoundary(line) || isBottomBoundary(line) || isLeftBoundary(line) || isRightBoundary(line);
    }

    public boolean isTopBoundary(Line line) {
        return line.isHorizontalLine() && line.getFrom().getY() == pointsCount - 1;
    }

    public boolean isBottomBoundary(Line line) {
        return line.isHorizontalLine() && line.getFrom().getY() == 0;
    }

    public boolean isLeftBoundary(Line line) {
        return line.isVerticalLine() && line.getFrom().getX() == 0;
    }

    public boolean isRightBoundary(Line line) {
        return line.isVerticalLine() && line.getFrom().getX() == pointsCount - 1;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        GameField gameField = (GameField) object;
        return pointsCount == gameField.pointsCount &&
                squaresCount == gameField.squaresCount &&
                Objects.equals(squares, gameField.squares) &&
                Objects.equals(lines, gameField.lines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pointsCount, squaresCount, squares, lines);
    }

    @Override
    public String toString() {
        return "GameField{" +
                "pointsCount=" + pointsCount +
                ", squaresCount=" + squaresCount +
                ", squares=" + squares +
                ", lines=" + lines +
                '}';
    }
}
