package ru.corridors.gui;

import ru.corridors.gui.handler.OpponentHandler;
import ru.corridors.gui.model.UIScore;

import javax.swing.*;
import java.awt.*;

public class ClientGUI extends JFrame {

    private GameFieldUI gameFieldUI;
    private UIScore uiScore;

    private OpponentHandler opponentHandler;

    public ClientGUI() throws HeadlessException {
        super("Game");

        setBounds(100, 100, 500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        gameFieldUI = new GameFieldUI();
        getContentPane().add(gameFieldUI);

        uiScore = new UIScore();
        getContentPane().add(uiScore, BorderLayout.CENTER);

        opponentHandler = new OpponentHandler(gameFieldUI);
    }

    public OpponentHandler getOpponentHandler() {
        return opponentHandler;
    }

    public UIScore getUiScore() {
        return uiScore;
    }

    // for tests
    public static void main(String[] args) {
        ClientGUI clientGUI = new ClientGUI();
        clientGUI.getOpponentHandler().handle(0, 0, 1, 0);
    }
}
