package ru.corridors.rmi.client;

import ru.corridors.dto.*;
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
    private Server server;

    @Override
    public void init(Server server) throws RemoteException {
        this.server = server;
    }

    @Override
    public ClientInfo registerClient() throws RemoteException {
        clientInfo = server.registerClient();
        return clientInfo;
    }

    @Override
    public void takeControl() throws RemoteException {
        server.giveControl(clientInfo);
    }

    @Override
    public StepInfo takeStepArgs(Scanner scanner) {
        System.out.println("Print args like from.x from.y to.x to.y");

        Line line = null;

        boolean isCorrect = false;
        do {
            System.out.print("Print: ");
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
    public boolean checkArgs(StepInfo stepInfo) throws RemoteException {
        return server.checkArgs(stepInfo, clientInfo);
    }

    @Override
    public void registerArgs(StepInfo stepInfo) throws RemoteException {
        server.registerArgs(stepInfo, clientInfo);
    }

    @Override
    public void giveUpControl() throws RemoteException {
        server.removeControl(clientInfo);
    }

    @Override
    public GameField takeGamefield() throws RemoteException {
        return server.getGamefield();
    }

    @Override
    public boolean checkOnFinished() throws RemoteException {
        return server.isFinished();
    }

    @Override
    public GameResults getResults() throws RemoteException {
        return server.getResults();
    }

    /*@Override
    public <C, P> P tryDo(Function<C[], P> action, C... args) throws RemoteException {
        if( ! checkOnFinished()) {
            return action.apply(args);
        }
        return null;
    }*/

    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    private static Scanner scanner = new Scanner(System.in);


    /** todo: add normal UI, add Action, that will check ifGameIsFinished and if not, then do Action  */
    public static void main(String[] args) throws RemoteException, NotBoundException, InterruptedException {

        Client client = new ClientImpl();

        final Registry registry = LocateRegistry.getRegistry(2732);
        Server server = (Server) registry.lookup(UNIQUE_BINDING_NAME);

        boolean isFinised = false;

        GameField gameField = null;

        client.init(server);
        client.registerClient();
        while (true) {
            client.takeControl();
            //client.tryDo(() -> client.takeControl());
            //client.tryDo(() -> client.takeControl(), null);
            //client.tryDo(() -> client.takeControl(), null);

            isFinised = client.checkOnFinished();
            if(isFinised)
                break;

            gameField = client.takeGamefield();
            System.out.println(gameField);

            StepInfo stepInfo = null;
            boolean isCorrect = true;
            do {
                stepInfo = client.takeStepArgs(scanner);
                isCorrect = client.checkArgs(stepInfo);
                System.out.println("isCorrect: " + isCorrect);
            }
            while ( ! isCorrect );

            if (isCorrect) {
                client.registerArgs(stepInfo);
            }

            gameField = client.takeGamefield();
            System.out.println(gameField);

            client.giveUpControl();
        }

        GameResults results = client.getResults();
        System.out.println(results);
    }
}
