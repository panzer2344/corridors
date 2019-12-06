package ru.corridors.gui.model;

import ru.corridors.dto.ClientInfo;

import java.awt.*;

public enum  State {
    NOT_ACTIVE_POINT(Color.GRAY, true),
    NOT_ACTIVE_LINE(Color.BLACK, true),
    ACTIVE_FIRST_PLAYER(Color.BLUE, false),
    ACTIVE_SECOND_PLAYER(Color.ORANGE, false),
    CHOOSING_FIRST_PLAYER(Color.CYAN, true),
    CHOOSING_SECOND_PLAYER(Color.YELLOW, true),
    MISSED_POINT(Color.RED, true);

    private Color color;
    private boolean isChangableState;

    State(Color color, boolean isChangableState) {
        this.color = color;
        this.isChangableState = isChangableState;
    }

    public Color getColor() {
        return color;
    }

    public boolean isChangableState() {
        return isChangableState;
    }
}
