package ru.corridors.gui;

import ru.corridors.gui.handler.OpponentHandler;

import javax.swing.*;
import java.awt.*;

public class ClientGUI extends JFrame {

    private GameFieldUI gameFieldUI;

    private OpponentHandler opponentHandler;

    public ClientGUI() throws HeadlessException {
        setBounds(100, 100, 500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        gameFieldUI = new GameFieldUI();
        getContentPane().add(gameFieldUI);

        opponentHandler = new OpponentHandler(gameFieldUI);

        setVisible(true);
    }

    public OpponentHandler getOpponentHandler() {
        return opponentHandler;
    }

    // for tests
    public static void main(String[] args) {
        ClientGUI clientGUI = new ClientGUI();
        clientGUI.getOpponentHandler().handle(0, 0, 1, 0);
    }
}
