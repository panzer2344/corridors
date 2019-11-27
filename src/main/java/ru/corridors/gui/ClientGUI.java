package ru.corridors.gui;

import ru.corridors.event.EventBus;
import ru.corridors.gui.model.PointBtn;

import javax.swing.*;
import java.awt.*;

public class ClientGUI extends JFrame {

    private Color playerColor;
    private int pointsVerticalLength;
    private EventBus eventBus = new EventBus();

    public ClientGUI(int pointsVerticalLength, Color playerColor) {
        super("gui");

        this.pointsVerticalLength = pointsVerticalLength;
        this.playerColor = playerColor;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 600);
        setPreferredSize(new Dimension(300, 300));
        setLayout(new BorderLayout());
        //setLayout(null);

        Container container = new Container();
        //container.setLayout(null);
        //container.setSize(300, 300);
        //container.setLayout(null);
        //container.setBounds(0, 0, 300, 300);
        //container.setPreferredSize(new Dimension(300, 300));
        //container.setMinimumSize(new Dimension(300, 300));
        //container.setBounds(0, 0, 2000, 2000);
        //container.setBounds(getInsets().left + 100, getInsets().top + 100, 600, 600);
        //container.setPreferredSize(new Dimension(400, 400));
        //container.setSize(new Dimension(400, 400));
        container.setLayout(new GridLayout(pointsVerticalLength + 1, pointsVerticalLength + 1));

        initPointBtns(pointsVerticalLength * pointsVerticalLength, container);

        JButton connectBtn = new JButton("Connect");

        JPanel menuPanel = new JPanel(new FlowLayout());
        menuPanel.add(connectBtn);

        add(container, BorderLayout.CENTER);
        add(menuPanel, BorderLayout.EAST);

        setVisible(true);
    }

    /*private void initPointBtns(int pointsVerticalLength, Container container) {
        int xOffset = (int) (container.getWidth() / pointsVerticalLength - 2 * PointBtn.DEFAULT_POINT_BTN_RADIUS);
        int yOffset = (int) (container.getHeight() / pointsVerticalLength - 2 * PointBtn.DEFAULT_POINT_BTN_RADIUS);

        int y = 0;

        for(int i = 0; i < pointsVerticalLength; i++) {
            int x = 0;

            for(int j = 0; j < pointsVerticalLength; j++) {
                container.add(new PointBtn(x, y, playerColor, eventBus));
                x += 2 * PointBtn.DEFAULT_POINT_BTN_RADIUS + xOffset;
            }

            y += 2 * PointBtn.DEFAULT_POINT_BTN_RADIUS + yOffset;
        }
    }*/

    private void initPointBtns(int pointsCount, Container container) {
        for(int i = 0; i < pointsCount; i++) {
            container.add(new PointBtn(playerColor, eventBus));
        }
    }

    public static void main(String[] args) {
        ClientGUI clientGUI = new ClientGUI(3, Color.BLUE);
    }

}
