package ru.corridors.rmi.server;

import ru.corridors.dto.*;
import ru.corridors.dto.gamefield.GameField;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerImpl implements Server {
    public static final String UNIQUE_BINDING_NAME = "binding.server";
    public static final int DEFAULT_SQUARE_COUNT = 1;

    private static int clientCount = 0;
    private static int currentClient = 0;

    private static final GameField gameField = new GameField(DEFAULT_SQUARE_COUNT);

    private static final Map<Integer, ClientInfo> clientsMap = new ConcurrentHashMap<>();

    @Override
    public synchronized ClientInfo registerClient()  {
        ClientInfo clientInfo = new ClientInfo(clientCount);
        clientsMap.put(clientCount, clientInfo);
        clientCount++;

        System.out.println("Client with id#" + clientInfo.getOrderNumber() + " was registered");
        System.out.println("Current client count: " + clientCount);

        return clientInfo;
    }

    @Override
    public void giveControl(ClientInfo clientInfo) throws RemoteException {
        while ( (currentClient != clientInfo.getOrderNumber() || clientCount < 2 ) && !isFinished()) {
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
    public boolean checkArgs(StepInfo stepInfo, ClientInfo clientInfo) {
        boolean alreadyOnGameField = gameField.getLines().contains(stepInfo.getLine());
        boolean isReachable = gameField.isReachable(stepInfo.getLine().getFrom()) &&
                gameField.isReachable(stepInfo.getLine().getTo());
        boolean isLengthCorrect = stepInfo.getMaxLen() == 1;
        boolean isOnGrid = stepInfo.getLine().isOnGrid();

        return !alreadyOnGameField && isReachable && isLengthCorrect && isOnGrid;
    }

    @Override
    public boolean registerArgs(StepInfo stepInfo, ClientInfo clientInfo) {
        synchronized (gameField) {
            Line currentLine = stepInfo.getLine();

            gameField.getLines().add(currentLine);

            if (currentLine.isHorizontalLine()) {
                if (gameField.isLockBot(currentLine)) {
                    gameField.setLockOnBottom(currentLine, clientInfo.getOrderNumber());
                } else if (gameField.isLockTop(currentLine)) {
                    gameField.setLockOnTop(currentLine, clientInfo.getOrderNumber());
                }
            } else if (currentLine.isVerticalLine()) {
                if (gameField.isLockLeft(currentLine)) {
                    gameField.setLockOnLeft(currentLine, clientInfo.getOrderNumber());
                } else if (gameField.isLockRight(currentLine)) {
                    gameField.setLockOnRight(currentLine, clientInfo.getOrderNumber());
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
    public GameField getGamefield()  {
        return gameField;
    }

    @Override
    public boolean isFinished() {
        return gameField.isAllSqaresFilled();
    }

    @Override
    public GameResults getResults() {
        Map<ClientInfo, ClientScore> results = new HashMap<>(clientCount);
        ClientInfo winner = null;
        int maxScore = 0;

        for(Integer clientOrder : gameField.getSquares().values()) {
            ClientInfo clientInfo = clientsMap.get(clientOrder);
            ClientScore clientScore = null;
            if(results.containsKey(clientInfo)) {
                clientScore = results.get(clientInfo);
            } else {
                clientScore = new ClientScore(1);
                results.put(clientInfo, clientScore);

            }

            if(clientScore.getSquareCount() > maxScore) {
                maxScore = clientScore.getSquareCount();
                winner = clientInfo;
            }
        }

        return new GameResults(winner, results);
    }

    public static void main(String[] args) throws RemoteException, AlreadyBoundException, InterruptedException {
        final Server server = new ServerImpl();

        final Registry registry = LocateRegistry.createRegistry(2732);

        Remote stub = UnicastRemoteObject.exportObject(server, 0);
        registry.bind(UNIQUE_BINDING_NAME, stub);

        Thread.sleep(Integer.MAX_VALUE);
    }
}
