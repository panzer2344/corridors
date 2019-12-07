package ru.corridors.gui.model;

import javax.swing.*;
import java.awt.*;

public class UIScore extends JComponent {

    private boolean yourStep = false;
    private int yourScore = 0;
    private int opponentScore = 0;

    public UIScore () {
        super();
        //setLocation(0,50);
        setSize(500, 200);
        setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setFont(new Font("Monaco", Font.BOLD, 15));

        g.drawString(yourStep ? "Your step!" : "Wait for other player", 20, 35);
        g.drawString("Score(You): " + yourScore, 170, 35);
        g.drawString("Score(Opponent): " + opponentScore, 300, 35);
    }

    public void setYourStep(boolean yourStep) {
        this.yourStep = yourStep;
        repaint();
    }

    public void addYourPoints(int delta) {
        yourScore += delta;
        repaint();
    }

    public void addOpponentPoints(int delta) {
        opponentScore += delta;
        repaint();
    }
}
