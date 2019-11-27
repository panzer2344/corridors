package ru.corridors.gui.model;

import ru.corridors.gui.handler.ClickHandler;

import javax.swing.*;
import java.awt.*;

public class Point extends JComponent {

    private int radius;
    private State state = State.NOT_ACTIVE;

    public Point(int x, int y, int radius) {
        super();

        this.radius = radius;

        setLocation(x - radius, y - radius);
        setSize(2 * radius, 2 * radius);
        addMouseListener(ClickHandler.instance);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        System.out.println("paint");
        g.setColor(state.color);
        g.fillRoundRect(0, 0, 2 * radius, 2 * radius, 2 * radius, 2 * radius);
    }

    public void setState(State state) {
        this.state = state;
        repaint();
    }

    public enum  State {
        NOT_ACTIVE(Color.GRAY),
        ACTIVE_FIRST_PLAYER(Color.BLUE),
        ACTIVE_SECOND_PLAYER(Color.RED),
        CHOOSED_FIRST_PLAYER(Color.CYAN),
        CHOOSED_SECOND_PLAYER(Color.MAGENTA);

        private Color color;

        State(Color color) {
            this.color = color;
        }
    }
}
