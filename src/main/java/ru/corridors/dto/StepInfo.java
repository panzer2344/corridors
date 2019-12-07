package ru.corridors.dto;

import java.io.Serializable;
import java.util.Objects;

public class StepInfo implements Serializable {
    private Line line;
    private int countOfSquares = 0;
    private ClientInfo filledSqureBy;

    public StepInfo(Line line) {
        this.line = line;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public int getMaxLen() {
        return Math.max(line.getHorizontalLength(), line.getVerticalLength());
    }

    public ClientInfo getFilledSqureBy() {
        return filledSqureBy;
    }

    public void incCountOfSquares(ClientInfo filledSqureBy) {
        this.filledSqureBy = filledSqureBy;
        countOfSquares++;
    }

    public int getCountOfSquares() {
        return countOfSquares;
    }

    @Override
    public String toString() {
        return "StepInfo{" +
                "line=" + line +
                ", filledSqureBy=" + filledSqureBy +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        StepInfo stepInfo = (StepInfo) object;
        return Objects.equals(line, stepInfo.line) &&
                Objects.equals(filledSqureBy, stepInfo.filledSqureBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(line, filledSqureBy);
    }
}
