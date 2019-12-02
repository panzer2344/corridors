package ru.corridors.rmi.server;

import ru.corridors.dto.ClientInfo;
import ru.corridors.dto.StepInfo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Server extends Remote {

    String UNIQUE_BINDING_SERVER_NAME = "binding.server";
    String UNIQUE_BINDING_CLIENT_PREFIX = "binding.client.";

    ClientInfo registerClient() throws RemoteException;

    boolean isStepCorrect(StepInfo stepInfo) throws RemoteException;

    boolean registerStep(StepInfo stepInfo, ClientInfo clientInfo) throws RemoteException;

}
