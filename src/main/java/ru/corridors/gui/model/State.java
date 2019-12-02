package ru.corridors.gui.model;

import ru.corridors.dto.ClientInfo;

import java.awt.*;

public enum  State {
    NOT_ACTIVE(Color.GRAY),
    ACTIVE_FIRST_PLAYER(Color.BLUE),
    ACTIVE_SECOND_PLAYER(Color.ORANGE),
    CHOOSING_FIRST_PLAYER(Color.CYAN),
    CHOOSING_SECOND_PLAYER(Color.YELLOW),
    MISSED_POINT(Color.RED);

    private Color color;

    State(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
