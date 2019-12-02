package ru.corridors.dto;

import java.io.Serializable;
import java.util.Objects;

public class ClientScore implements Serializable {
    private Integer squareCount;

    public ClientScore(Integer squareCount) {
        this.squareCount = squareCount;
    }

    public Integer getSquareCount() {
        return squareCount;
    }

    public ClientScore increase(Integer onDelta) {
        squareCount += onDelta;
        return this;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ClientScore that = (ClientScore) object;
        return Objects.equals(squareCount, that.squareCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(squareCount);
    }

    @Override
    public String toString() {
        return "ClientScore{" +
                "squareCount=" + squareCount +
                '}';
    }
}
