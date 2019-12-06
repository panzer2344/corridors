package ru.corridors.rmi.client;

import ru.corridors.dto.ClientInfo;
import ru.corridors.dto.GameResults;
import ru.corridors.dto.StepInfo;
import ru.corridors.gui.ClientGUI;
import ru.corridors.gui.validator.AllowStepValidator;

import javax.swing.*;
import java.rmi.RemoteException;


/*** todo: check whats wrong with validating next line, this works like peace of shit*/

public class ClientImpl implements Client {

    private ClientGUI clientGUI;

    public ClientImpl(ClientGUI clientGUI) {
        this.clientGUI = clientGUI;
    }

    @Override
    public void handleOpponentStep(StepInfo stepInfo, ClientInfo clientInfo) throws RemoteException {
        clientGUI.getOpponentHandler().handle(stepInfo);
    }

    @Override
    public void allowStep(boolean allow) throws RemoteException {
        AllowStepValidator.instance.setAllow(allow);
    }

    @Override
    public void handleResults(GameResults gameResults) throws RemoteException {
        JFrame messageFrame = new JFrame();
        messageFrame.setSize(400, 400);
        messageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        messageFrame.setVisible(true);

        JOptionPane.showMessageDialog(messageFrame, gameResults.toString());
        System.out.println("Finish: " + gameResults.toString());
    }
}
