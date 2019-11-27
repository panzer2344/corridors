package ru.corridors.gui.model;

import ru.corridors.event.EventBus;
import ru.corridors.event.StartChoosePointEvent;
import ru.corridors.gui.action.DrawAction;
import ru.corridors.gui.action.DrawLineAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.List;

public class PointBtn extends JComponent implements MouseListener {

    public static final int DEFAULT_POINT_BTN_RADIUS = 16;

    private int x = 0;
    private int y = 0;
    private int radius;

    private Color playerColor = Color.GRAY;
    private Color pointColor = Color.GRAY;

    private boolean isActive = false;
    private boolean isChoosen = false;
    private boolean isBlocked = false;

    private EventBus eventBus;

    private List<DrawAction> drawActionList = new LinkedList<>();

    public PointBtn() {
        init(DEFAULT_POINT_BTN_RADIUS);
    }

    public PointBtn(Color playerColor) {
        init(DEFAULT_POINT_BTN_RADIUS);
        this.playerColor = playerColor;
    }

    public PointBtn(Color playerColor, EventBus eventBus) {
        init(DEFAULT_POINT_BTN_RADIUS);
        this.playerColor = playerColor;
        this.eventBus = eventBus;
    }

    public PointBtn(int x, int y, Color playerColor, EventBus eventBus) {
        init(DEFAULT_POINT_BTN_RADIUS);
        this.playerColor = playerColor;
        this.eventBus = eventBus;

        this.x = x;
        this.y = y;
    }

    public PointBtn(int radius) {
        init(radius);
    }

    private void init(int radius) {
        this.radius = radius;
        setBounds(x, y, 2 * radius, 2 * radius);
        addMouseListener(this);
        addDrawPointBtnAction();
    }

    @Override
    protected void paintComponent(Graphics g) {
        for(DrawAction drawAction: drawActionList) {
            drawAction.draw(g);
        }

//        g.setColor(Color.BLACK);
//        g.fillRoundRect(x, y, 2 * radius + 4, 2 * radius + 4, 2 * radius + 2, 2 * radius + 2);
//
//        g.setColor(pointColor);
//        g.fillRoundRect(x + 2, y + 2, 2 * radius, 2 * radius, 2 * radius, 2 * radius);
//
//        g.setColor(isActive ? playerColor : Color.GRAY);
//        g.fillRoundRect(x + 4, y + 4, 2 * radius - 4, 2 * radius - 4, 2 * radius - 2, 2 * radius - 2);

//        g.setColor(Color.BLACK);
//        g.fillRoundRect(0, 0, 2 * radius + 4, 2 * radius + 4, 2 * radius + 2, 2 * radius + 2);
//
//        g.setColor(pointColor);
//        g.fillRoundRect(2, 2, 2 * radius, 2 * radius, 2 * radius, 2 * radius);
//
//        g.setColor(isActive ? playerColor : Color.GRAY);
//        g.fillRoundRect(4, 4, 2 * radius - 4, 2 * radius - 4, 2 * radius - 2, 2 * radius - 2);
    }


    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        if( ! isActive ) {

            isActive = true;
            isChoosen = true;

            eventBus.setEvent(new StartChoosePointEvent(this));

            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(isChoosen) {
            isChoosen = false;
            addDrawLineAction();
            getParent().repaint();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if( isMouseDown(e) ) {
            isActive = true;
            repaint();

            StartChoosePointEvent event = (StartChoosePointEvent) eventBus.getEvent();
            event.setEndBtn(this);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if( isMouseDown(e) && ! isChoosen) {
            isActive = false;
            repaint();

            StartChoosePointEvent event = (StartChoosePointEvent) eventBus.getEvent();
            event.setEndBtn(null);
        }
    }

    boolean isMouseDown(MouseEvent e) {
        return ( e.getModifiersEx() & InputEvent.BUTTON1_DOWN_MASK ) == InputEvent.BUTTON1_DOWN_MASK;
    }

    private void addDrawLineAction() {
        StartChoosePointEvent event = (StartChoosePointEvent) eventBus.getEvent();
        PointBtn startBtn = event.getStartBtn();
        PointBtn endBtn = event.getEndBtn();

        drawActionList.add(0, new DrawLineAction(
                DEFAULT_POINT_BTN_RADIUS,
                DEFAULT_POINT_BTN_RADIUS,
                endBtn.getX() - startBtn.getX() + DEFAULT_POINT_BTN_RADIUS,
                endBtn.getY() - startBtn.getY() + DEFAULT_POINT_BTN_RADIUS));

        startBtn.isBlocked = true;
        endBtn.isBlocked = true;

//        drawActionList.add(g -> {
//            g.setColor(Color.BLACK);
//            g.drawLine(0, 0, endBtn.getX() - startBtn.getY(), endBtn.getY() - startBtn.getY());
//            System.out.println("Draw line from: ( " + startBtn.getX() + ", " + startBtn.getY() + " )");
//            System.out.println("to: ( " + endBtn.getX() + ", " + endBtn.getY() + " )");
//        });
    }

    private void addDrawPointBtnAction() {
       drawActionList.add(g -> {
           g.setColor(Color.BLACK);
           g.fillRoundRect(0, 0, 2 * radius + 4, 2 * radius + 4, 2 * radius + 2, 2 * radius + 2);

           g.setColor(pointColor);
           g.fillRoundRect(2, 2, 2 * radius, 2 * radius, 2 * radius, 2 * radius);

           g.setColor(isActive ? playerColor : Color.GRAY);
           g.fillRoundRect(4, 4, 2 * radius - 4, 2 * radius - 4, 2 * radius - 2, 2 * radius - 2);
       });
    }
}
