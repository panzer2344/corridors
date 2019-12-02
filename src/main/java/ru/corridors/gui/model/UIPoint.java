package ru.corridors.gui.model;

import ru.corridors.gui.handler.ClickHandler;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UIPoint extends JComponent {

    private int _x;
    private int _y;
    private int radius;

    private int indexHor;
    private int indexVert;

    private State state = State.NOT_ACTIVE;

    private List<UILine> connections;

    public UIPoint(int x, int y, int radius) {
        super();
        init(x, y, radius);
    }

    private void init(int x, int y, int radius) {
        this._x = x;
        this._y = y;
        this.radius = radius;
        this.connections = new ArrayList<>(4);

        setLocation(x - radius, y - radius);
        setSize(2 * radius, 2 * radius);
        addMouseListener(ClickHandler.instance);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(state.getColor());
        g.fillRoundRect(0, 0, 2 * radius, 2 * radius, 2 * radius, 2 * radius);
    }

    public void setState(State state) {
        this.state = state;
        repaint();
    }

    public int get_X() {
        return _x;
    }

    public int get_Y() {
        return _y;
    }

    public int getRadius() {
        return radius;
    }

    public State getState() {
        return state;
    }

    public List<UILine> getConnections() {
        return connections;
    }

    public int getIndexHor() {
        return indexHor;
    }

    public void setIndexHor(int indexHor) {
        this.indexHor = indexHor;
    }

    public int getIndexVert() {
        return indexVert;
    }

    public void setIndexVert(int indexVert) {
        this.indexVert = indexVert;
    }
}
