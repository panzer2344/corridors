package ru.corridors.rmi.client;

import ru.corridors.dto.ClientInfo;
import ru.corridors.dto.Line;
import ru.corridors.dto.Point;
import ru.corridors.dto.StepInfo;
import ru.corridors.dto.gamefield.GameField;
import ru.corridors.rmi.server.Server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class ClientImpl implements Client {
    public static final String UNIQUE_BINDING_NAME = "binding.server";

    private ClientInfo clientInfo;

    @Override
    public ClientInfo registerClient(Server server) throws RemoteException {
        clientInfo = server.registerClient();
        return clientInfo;
    }

    @Override
    public void takeControl(Server server) throws RemoteException {
        server.giveControl(clientInfo);
    }

    @Override
    public StepInfo takeStepArgs(Scanner scanner) {
        System.out.println("Print args like from.x from.y to.x to.y");
        System.out.print("Print: ");

        Line line = null;

        boolean isCorrect = false;
        do {
            String scanned = scanner.nextLine();
            String[] splitted = scanned.split(" ");

            Point from = new Point(Integer.parseInt(splitted[0]), Integer.parseInt(splitted[1]));
            Point to = new Point(Integer.parseInt(splitted[2]), Integer.parseInt(splitted[3]));

            isCorrect = (from.getX() == to.getX() && from.getY() < to.getY()) ||
                    (from.getY() == from.getY() && from.getX() < to.getX());

            line = new Line(from, to);
            isCorrect = isCorrect && line.isOnGrid();

        } while (!isCorrect);

        System.out.println("Input is correct: " + line);

        return new StepInfo(line);
    }

    @Override
    public boolean checkArgs(StepInfo stepInfo, Server server) throws RemoteException {
        return server.checkArgs(stepInfo, clientInfo);
    }

    @Override
    public void registerArgs(StepInfo stepInfo, Server server) throws RemoteException {
        server.registerArgs(stepInfo, clientInfo);
    }

    @Override
    public void giveUpControl(Server server) throws RemoteException {
        server.removeControl(clientInfo);
    }

    @Override
    public GameField takeGamefield(Server server) throws RemoteException {
        return server.getGamefield();
    }

    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    private static Scanner scanner = new Scanner(System.in);


    /** todo: make a finish constraints, and also normal UI  */
    public static void main(String[] args) throws RemoteException, NotBoundException, InterruptedException {

        Client client = new ClientImpl();

        final Registry registry = LocateRegistry.getRegistry(2732);
        Server server = (Server) registry.lookup(UNIQUE_BINDING_NAME);

        client.registerClient(server);
        while (true) {
            client.takeControl(server);

            StepInfo stepInfo = null;
            boolean isCorrect = true;
            do {
                stepInfo = client.takeStepArgs(scanner);
                isCorrect = client.checkArgs(stepInfo, server);
                System.out.println("isCorrect: " + isCorrect);
            }
            while ( ! isCorrect );

            if (isCorrect) {
                client.registerArgs(stepInfo, server);
            }

            GameField gameField = client.takeGamefield(server);
            System.out.println(gameField);

            client.giveUpControl(server);
        }
    }
}
