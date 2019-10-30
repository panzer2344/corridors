package ru.corridors.rmi.server;

import ru.corridors.dto.ClientInfo;
import ru.corridors.dto.GameResults;
import ru.corridors.dto.StepInfo;
import ru.corridors.dto.gamefield.GameField;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Server extends Remote {
    ClientInfo registerClient() throws RemoteException;
    void giveControl(ClientInfo clientInfo) throws RemoteException;
    boolean checkArgs(StepInfo stepInfo, ClientInfo clientInfo) throws RemoteException;
    boolean registerArgs(StepInfo stepInfo, ClientInfo clientInfo) throws RemoteException;
    void removeControl(ClientInfo clientInfo) throws RemoteException;
    GameField getGamefield() throws RemoteException;
    boolean isFinished() throws RemoteException;
    GameResults getResults() throws RemoteException;
}
