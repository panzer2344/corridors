package ru.corridors.rmi.server;

import ru.corridors.dto.ClientInfo;
import ru.corridors.dto.Line;
import ru.corridors.dto.Point;
import ru.corridors.dto.StepInfo;
import ru.corridors.dto.gamefield.GameField;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerImpl implements Server {
    public static final String UNIQUE_BINDING_NAME = "binding.server";
    private static int DEFAULT_SQUARE_COUNT = 5;

    private static int clientCount = 0;
    private static int currentClient = 0;

    private static final GameField gameField = new GameField(DEFAULT_SQUARE_COUNT);

    @Override
    public synchronized ClientInfo registerClient() throws RemoteException {
        ClientInfo clientInfo = new ClientInfo(clientCount);
        clientCount++;

        System.out.println("Client with id#" + clientInfo.getOrderNumber() + " was registered");
        System.out.println("Current client count: " + clientCount);

        return clientInfo;
    }

    @Override
    public void giveControl(ClientInfo clientInfo) throws RemoteException {
        while (currentClient != clientInfo.getOrderNumber() || clientCount < 2) {
            System.out.println("Try to get control by " + clientInfo.getOrderNumber());
            System.out.println("Current client count: " + clientCount);
            try {
                Thread.currentThread().sleep(100);
            } catch (InterruptedException e) {
                throw new RemoteException("InterruptedException: " + e);
            }
        }
    }

    @Override
    public boolean checkArgs(StepInfo stepInfo, ClientInfo clientInfo) throws RemoteException {
        boolean alreadyOnGameField = gameField.getLines().contains(stepInfo.getLine());
        boolean isReachable = gameField.isReachable(stepInfo.getLine().getFrom()) &&
                gameField.isReachable(stepInfo.getLine().getTo());
        boolean isLengthCorrect = stepInfo.getMaxLen() == 1;
        boolean isOnGrid = stepInfo.getLine().isOnGrid();

        return !alreadyOnGameField && isReachable && isLengthCorrect && isOnGrid;
    }

    @Override
    public boolean registerArgs(StepInfo stepInfo, ClientInfo clientInfo) throws RemoteException {
        synchronized (gameField) {
            Line currentLine = stepInfo.getLine();

            gameField.getLines().add(currentLine);

            if (currentLine.isHorizontalLine()) {
                if (gameField.isLockBot(currentLine)) {
                    gameField.getSquares().put(
                            new Point(currentLine.getFrom().getX(), currentLine.getFrom().getY() - 1),
                            clientInfo.getOrderNumber());
                } else if (gameField.isLockTop(currentLine)) {
                    gameField.getSquares().put(
                            new Point(currentLine.getFrom().getX(), currentLine.getFrom().getY()),
                            clientInfo.getOrderNumber());
                }
            } else if (currentLine.isVerticalLine()) {
                if (gameField.isLockLeft(currentLine)) {
                    gameField.getSquares().put(
                            new Point(currentLine.getFrom().getX() - 1, currentLine.getFrom().getY()),
                            clientInfo.getOrderNumber());
                } else if (gameField.isLockRight(currentLine)) {
                    gameField.getSquares().put(
                            new Point(currentLine.getFrom().getX(), currentLine.getFrom().getY()),
                            clientInfo.getOrderNumber());
                }
            }

            return true;
        }
    }

    @Override
    public void removeControl(ClientInfo clientInfo) throws RemoteException {
        if (currentClient + 1 >= clientCount) {
            currentClient = 0;
        } else {
            currentClient++;
        }
        try {
            Thread.currentThread().sleep(200);
        } catch (InterruptedException e) {
            throw new RemoteException("InterruptedException: " + e);
        }
    }

    @Override
    public GameField getGamefield() throws RemoteException {
        return gameField;
    }

    public static void main(String[] args) throws RemoteException, AlreadyBoundException, InterruptedException {
        final Server server = new ServerImpl();

        final Registry registry = LocateRegistry.createRegistry(2732);

        Remote stub = UnicastRemoteObject.exportObject(server, 0);
        registry.bind(UNIQUE_BINDING_NAME, stub);

        Thread.sleep(Integer.MAX_VALUE);
    }
}
