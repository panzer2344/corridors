package ru.corridors.rmi.client;

import ru.corridors.dto.ClientInfo;
import ru.corridors.dto.GameResults;
import ru.corridors.dto.StepInfo;
import ru.corridors.dto.gamefield.GameField;
import ru.corridors.rmi.Action;
import ru.corridors.rmi.server.Server;

import java.rmi.RemoteException;
import java.util.Scanner;

public interface Client {
    void init(Server server) throws RemoteException;
    ClientInfo registerClient() throws RemoteException;
    void takeControl() throws RemoteException;
    StepInfo takeStepArgs(Scanner scanner) throws RemoteException;
    boolean checkArgs(StepInfo stepInfo) throws RemoteException;
    void registerArgs(StepInfo stepInfo) throws RemoteException;
    void giveUpControl() throws RemoteException;
    GameField takeGamefield() throws RemoteException;
    boolean checkOnFinished() throws RemoteException;
    GameResults getResults() throws RemoteException;
    //<C, P> P tryDo(Action<C, P> action, C... args) throws RemoteException;
}
