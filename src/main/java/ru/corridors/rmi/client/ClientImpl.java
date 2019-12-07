package ru.corridors.rmi.client;

import ru.corridors.client.ClientInfoContainer;
import ru.corridors.dto.ClientInfo;
import ru.corridors.dto.GameResults;
import ru.corridors.dto.StepInfo;
import ru.corridors.gui.ClientGUI;
import ru.corridors.gui.handler.StepResultHandler;
import ru.corridors.gui.validator.AllowStepValidator;

import javax.swing.*;
import java.rmi.RemoteException;


public class ClientImpl implements Client {

    private ClientGUI clientGUI;
    private StepResultHandler stepResultHandler;

    public ClientImpl(ClientGUI clientGUI) {
        this.clientGUI = clientGUI;
        stepResultHandler = new StepResultHandler(clientGUI);
    }

    @Override
    public void handleStepResult(StepInfo stepInfo, ClientInfo stepMaker) {
        if( ! ClientInfoContainer.instance.getClientInfo().equals(stepMaker)) {
            clientGUI.getOpponentHandler().handle(stepInfo);
        }
        stepResultHandler.handle(stepInfo);
    }

    @Override
    public void allowStep(boolean allow) throws RemoteException {
        AllowStepValidator.instance.setAllow(allow);
        clientGUI.getUiScore().setYourStep(allow);
    }

    @Override
    public void setOpponentInfo(ClientInfo opponentInfo) throws RemoteException {
        ClientInfoContainer.instance.setOpponentInfo(opponentInfo);
    }

    @Override
    public void handleResults(GameResults gameResults) throws RemoteException {
        ClientInfo you = ClientInfoContainer.instance.getClientInfo();
        ClientInfo opponent = ClientInfoContainer.instance.getOpponentInfo();

        StringBuilder messageSB = new StringBuilder("Winner: ")
                .append(you.getOrderNumber() == gameResults.getWinner().getOrderNumber() ? "You" : "Opponent")
                .append("\n Scores: ")
                .append(" You ")
                .append(gameResults.getResults().get(you).getSquareCount())
                .append(" - ")
                .append(gameResults.getResults().get(opponent).getSquareCount())
                .append(" Opponent ");

        JOptionPane.showMessageDialog(clientGUI, messageSB);
        System.out.println("Finish: " + gameResults.toString());
    }
}
