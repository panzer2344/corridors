package ru.corridors.rmi.client;

import ru.corridors.dto.ClientInfo;
import ru.corridors.dto.StepInfo;
import ru.corridors.dto.gamefield.GameField;
import ru.corridors.rmi.server.Server;

import java.rmi.RemoteException;
import java.util.Scanner;

public interface Client {
    ClientInfo registerClient(Server server) throws RemoteException;
    void takeControl(Server server) throws RemoteException;
    StepInfo takeStepArgs(Scanner scanner) throws RemoteException;
    boolean checkArgs(StepInfo stepInfo, Server server) throws RemoteException;
    void registerArgs(StepInfo stepInfo, Server server) throws RemoteException;
    void giveUpControl(Server server) throws RemoteException;
    GameField takeGamefield(Server server) throws RemoteException;
}
