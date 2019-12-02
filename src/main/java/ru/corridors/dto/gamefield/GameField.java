package ru.corridors.dto.gamefield;

import ru.corridors.dto.Line;
import ru.corridors.dto.Point;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GameField implements Serializable {
    private final int pointsCount;
    private final int squaresLength;

    private final Map<Point, Integer> squares;
    private final List<Line> lines;

    private final int squaresCount;
    private int filledSquaresCount = 0;

    public GameField(int linesCount) {
        this.squaresLength = linesCount;
        pointsCount = linesCount + 1;
        squaresCount = linesCount * linesCount;

        squares = new ConcurrentHashMap<>(linesCount);
        lines = new ArrayList<>(3 * (pointsCount - 1) * (pointsCount - 1));
    }

    public int getPointsCount() {
        return pointsCount;
    }

    public int getSquaresLength() {
        return squaresLength;
    }

    public Map<Point, Integer> getSquares() {
        return squares;
    }

    public List<Line> getLines() {
        return lines;
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

    public void setLockOnBottom(Line line, Integer clientOrder) {
        squares.put(getOnLockSquarePoint(line, 0, -1), clientOrder);
        filledSquaresCount++;
    }

    public void setLockOnTop(Line line, Integer clientOrder) {
        squares.put(getOnLockSquarePoint(line, 0, 0), clientOrder);
        filledSquaresCount++;
    }

    public void setLockOnLeft(Line line, Integer clientOrder) {
        squares.put(getOnLockSquarePoint(line, -1, 0), clientOrder);
        filledSquaresCount++;
    }

    public void setLockOnRight(Line line, Integer clientOrder) {
        squares.put(getOnLockSquarePoint(line, 0, 0), clientOrder);
        filledSquaresCount++;
    }

    private Point getOnLockSquarePoint(Line line, Integer xDelta, Integer yDelta) {
        return new Point(
                line.getFrom().getX() + xDelta,
                line.getFrom().getY() + yDelta);
    }

    public boolean isAllSqaresFilled() {
        return filledSquaresCount == squaresCount;
    }

    private int getFilledSquaresCount() {
        return filledSquaresCount;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        GameField gameField = (GameField) object;
        return pointsCount == gameField.pointsCount &&
                squaresLength == gameField.squaresLength &&
                Objects.equals(squares, gameField.squares) &&
                Objects.equals(lines, gameField.lines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pointsCount, squaresLength, squares, lines);
    }

    @Override
    public String toString() {
        return "GameField{" +
                "pointsCount=" + pointsCount +
                ", squaresLength=" + squaresLength +
                ", squares=" + squares +
                ", lines=" + lines +
                '}';
    }
}
