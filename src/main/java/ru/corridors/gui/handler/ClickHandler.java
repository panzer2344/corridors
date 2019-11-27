package ru.corridors.gui.handler;

import ru.corridors.gui.model.Point;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ClickHandler implements MouseListener {

    public static final ClickHandler instance = new ClickHandler();

    private Point first;
    private Point second;

    private ClickHandler() {

    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        //System.out.println("mouseClicked at " + mouseEvent.getComponent().getName());
        //System.out.println("" + mouseEvent.getComponent().getSize(new Dimension(10, 10)));
        //mouseEvent.getComponent().setSize(10, 10);

        if(mouseEvent.getSource() instanceof Point) {
            Point point = (Point) mouseEvent.getSource();

            if(first == null) {
                first = point;
                first.setState(Point.State.CHOOSED_FIRST_PLAYER);
            } else {
                second = point;
                first.setState(Point.State.ACTIVE_FIRST_PLAYER);
                second.setState(Point.State.ACTIVE_FIRST_PLAYER);

                first = null;
                second = null;
            }

        } else {
            first = null;
        }

        System.out.println(mouseEvent.getSource().getClass());
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
}
