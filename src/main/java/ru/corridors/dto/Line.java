package ru.corridors.dto;

import java.io.Serializable;
import java.util.Objects;

public class Line implements Serializable {
    private Point from;
    private Point to;

    public Line(Point from, Point to) {
        this.from = from;
        this.to = to;
    }

    public Point getFrom() {
        return from;
    }

    public void setFrom(Point from) {
        this.from = from;
    }

    public Point getTo() {
        return to;
    }

    public void setTo(Point to) {
        this.to = to;
    }

    public int getHorizontalLength() {
        return Math.abs(to.x - from.x);
    }

    public int getVerticalLength() {
        return Math.abs(to.y - from.y);
    }

    public boolean isVerticalLine() {
        return getHorizontalLength() == 0;
    }

    public boolean isHorizontalLine() {
        return getVerticalLength() == 0;
    }

    public boolean isOnGrid() {
        return isVerticalLine() || isHorizontalLine();
    }

    public Line getParallelLineWithXDelta(int xDelta) {
        return new Line(
                new Point(
                        from.getX() + xDelta,
                        from.getY()
                ),
                new Point(
                        to.getX() + xDelta,
                        to.getY()
                )
        );
    }

    public Line getParallelLineWithYDelta(int yDelta) {
        return new Line(
                new Point(
                        from.getX(),
                        from.getY() + yDelta
                ),
                new Point(
                        to.getX(),
                        to.getY() + yDelta
                )
        );
    }

    public enum Angle {
        CLOCKWISE_90,
        CLOCKWISE_270
    }
    public Line rotate(Angle angle) {
        Line rotated = null;
        if(isVerticalLine()) {
            switch (angle) {
                case CLOCKWISE_90: {
                    rotated = new Line(
                            getFrom(),
                            new Point(
                                    getFrom().getX() + 1,
                                    getFrom().getY()
                            )
                    );
                    break;
                }
                case CLOCKWISE_270: {
                    rotated = new Line(
                            new Point(
                                    getFrom().getX() - 1,
                                    getFrom().getY()
                            ),
                            getFrom()
                    );
                    break;
                }
                default:
                    break;
            }
        } else {
            switch (angle) {
                case CLOCKWISE_90: {
                    rotated = new Line(
                            new Point(
                                    getFrom().getX(),
                                    getFrom().getY() - 1
                            ),
                            getFrom()
                    );
                    break;
                }
                case CLOCKWISE_270: {
                    rotated = new Line(
                            getFrom(),
                            new Point(
                                    getFrom().getX(),
                                    getFrom().getY() + 1
                            )
                    );
                    break;
                }
                default:
                    break;
            }
        }

        return rotated;
    }

    public boolean isLeftThan(Line line) {
        Integer anotherMinCoord = Math.min(line.getFrom().x, line.getTo().x);
        Integer thisMinCoord = Math.min(from.x, to.x);

        return thisMinCoord < anotherMinCoord;
    }

    public boolean isRightThan(Line line) {
        Integer anotherMaxCoord = Math.max(line.getFrom().x, line.getTo().x);
        Integer thisMaxCoord = Math.max(from.x, to.x);

        return thisMaxCoord > anotherMaxCoord;
    }

    public boolean isLowerThan(Line line) {
        Integer anotherMinCoord = Math.min(line.getFrom().y, line.getTo().y);
        Integer thisMinCoord = Math.min(from.y, to.y);

        return thisMinCoord < anotherMinCoord;
    }

    public boolean isHigherThan(Line line) {
        Integer anotherMaxCoord = Math.max(line.getFrom().y, line.getTo().y);
        Integer thisMaxCoord = Math.max(from.y, to.y);

        return thisMaxCoord > anotherMaxCoord;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Line line = (Line) object;
        return Objects.equals(from, line.from) && Objects.equals(to, line.to) ||
                Objects.equals(from, line.to) && Objects.equals(to, line.from);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    @Override
    public String toString() {
        return "Line{" +
                "from=" + from +
                ", to=" + to +
                '}';
    }
}
