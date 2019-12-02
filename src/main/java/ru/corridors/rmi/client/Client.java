package ru.corridors.rmi.client;

import ru.corridors.dto.ClientInfo;
import ru.corridors.dto.GameResults;
import ru.corridors.dto.StepInfo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client extends Remote {

    String UNIQUE_BINDING_SERVER_NAME = "binding.server";
    String UNIQUE_BINDING_CLIENT_PREFIX = "binding.client.";

    void handleOpponentStep(StepInfo stepInfo, ClientInfo clientInfo) throws RemoteException;

    void allowStep(boolean allow) throws RemoteException;

    void handleResults(GameResults gameResults) throws RemoteException;

}
