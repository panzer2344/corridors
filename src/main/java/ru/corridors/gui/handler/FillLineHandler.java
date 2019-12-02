package ru.corridors.gui.handler;

import ru.corridors.client.ClientInfoContainer;
import ru.corridors.dto.StepInfo;
import ru.corridors.rmi.server.Server;

import java.rmi.RemoteException;

public class FillLineHandler {

    public static final FillLineHandler instance = new FillLineHandler();

    private Server serverStub;

    private FillLineHandler() {}

    public void handle(StepInfo stepInfo) {
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
