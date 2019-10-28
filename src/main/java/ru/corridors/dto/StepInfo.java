package ru.corridors.dto;

import java.io.Serializable;
import java.util.Objects;

public class StepInfo implements Serializable {
    private Line line;

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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        StepInfo stepInfo = (StepInfo) object;
        return Objects.equals(line, stepInfo.line);
    }

    @Override
    public int hashCode() {
        return Objects.hash(line);
    }

    @Override
    public String toString() {
        return "StepInfo{" +
                "line=" + line +
                '}';
    }
}
