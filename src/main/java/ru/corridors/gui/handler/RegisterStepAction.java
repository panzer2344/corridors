package ru.corridors.gui.handler;

import ru.corridors.client.ClientInfoContainer;
import ru.corridors.dto.StepInfo;
import ru.corridors.rmi.server.Server;

import java.rmi.RemoteException;

public class RegisterStepAction {

    public static final RegisterStepAction instance = new RegisterStepAction();

    private Server serverStub;

    private RegisterStepAction() {}

    public void doAction(StepInfo stepInfo) {
        try {
            serverStub.registerStep(stepInfo, ClientInfoContainer.instance.getClientInfo());
        } catch (RemoteException e) {
            System.out.println("Error! " + e.getMessage());
        }
    }

    public void setServerStub(Server serverStub) {
        this.serverStub = serverStub;
    }

}
