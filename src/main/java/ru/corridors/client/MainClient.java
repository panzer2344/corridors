package ru.corridors.client;

import ru.corridors.dto.ClientInfo;
import ru.corridors.gui.ClientGUI;
import ru.corridors.gui.handler.RegisterStepAction;
import ru.corridors.gui.validator.FillLineValidator;
import ru.corridors.rmi.client.Client;
import ru.corridors.rmi.client.ClientImpl;
import ru.corridors.rmi.server.Server;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class MainClient {

    public static void main(String[] args) throws RemoteException, NotBoundException, AlreadyBoundException {
        ClientGUI clientGUI = new ClientGUI();
        Client client = new ClientImpl(clientGUI);

        Registry registry = LocateRegistry.getRegistry("localhost", 2732);
        Server server = (Server) registry.lookup(ClientImpl.UNIQUE_BINDING_SERVER_NAME);

        ClientInfo clientInfo = server.registerClient();
        Remote stub = UnicastRemoteObject.exportObject(client, 0);
        registry.bind(ClientImpl.UNIQUE_BINDING_CLIENT_PREFIX + clientInfo.getOrderNumber(), stub);

        ClientInfoContainer.instance.setClientInfo(clientInfo);

        RegisterStepAction.instance.setServerStub(server);
        FillLineValidator.instance.setServerStub(server);

    }
}
