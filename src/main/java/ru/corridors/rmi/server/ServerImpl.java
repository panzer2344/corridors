package ru.corridors.rmi.server;

import ru.corridors.dto.*;
import ru.corridors.dto.gamefield.GameField;
import ru.corridors.rmi.client.Client;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerImpl implements Server {
    public static final int DEFAULT_SQUARE_COUNT = 5;

    private static int clientCount = 0;
    private static int currentClient = 0;

    private static final GameField gameField = new GameField(DEFAULT_SQUARE_COUNT);

    private static final Map<Integer, ClientInfo> clientsMap = new ConcurrentHashMap<>();

    private static final Map<Integer, Client> clientsStubMap = new ConcurrentHashMap<>();

    final Registry registry;

    public ServerImpl() throws RemoteException {
        registry = LocateRegistry.getRegistry(2732);
    }

    @Override
    public ClientInfo registerClient() throws RemoteException {
        ClientInfo clientInfo = new ClientInfo(clientCount);
        clientsMap.put(clientCount, clientInfo);
        clientCount++;

        System.out.println("Client with id#" + clientInfo.getOrderNumber() + " was registered");
        System.out.println("Current client count: " + clientCount);

        return clientInfo;
    }

    @Override
    public void sayReadyToStart(ClientInfo clientInfo) throws RemoteException {
        try {
            Client clientStub = (Client) registry.lookup(UNIQUE_BINDING_CLIENT_PREFIX + clientInfo.getOrderNumber());
            clientsStubMap.put(clientInfo.getOrderNumber(), clientStub);

            if(clientsStubMap.size() >= 2) {
                startGame();
                sendOpponentsInfo(clientInfo);
            }
        } catch (NotBoundException e) {
            throw new RemoteException(e.getMessage(), e);
        }
    }

    @Override
    public boolean isStepCorrect(StepInfo stepInfo) throws RemoteException {
        boolean alreadyOnGameField = gameField.getLines().contains(stepInfo.getLine());
        boolean isReachable = gameField.isReachable(stepInfo.getLine().getFrom()) &&
                gameField.isReachable(stepInfo.getLine().getTo());
        boolean isLengthCorrect = stepInfo.getMaxLen() == 1;
        boolean isOnGrid = stepInfo.getLine().isOnGrid();

        System.out.println(gameField.toString());
        System.out.println(stepInfo.toString());

        return !alreadyOnGameField && isReachable && isLengthCorrect && isOnGrid;
    }

    @Override
    public boolean registerStep(StepInfo stepInfo, ClientInfo clientInfo) throws RemoteException {
        synchronized (gameField) {
            if(currentClient != clientInfo.getOrderNumber()) return false;

            Line currentLine = stepInfo.getLine();

            gameField.getLines().add(currentLine);

            if (currentLine.isHorizontalLine()) {
                if (gameField.isLockBot(currentLine)) {
                    gameField.setLockOnBottom(currentLine, clientInfo.getOrderNumber());
                    stepInfo.incCountOfSquares(clientInfo);
                }
                if (gameField.isLockTop(currentLine)) {
                    gameField.setLockOnTop(currentLine, clientInfo.getOrderNumber());
                    stepInfo.incCountOfSquares(clientInfo);
                }
            } else if (currentLine.isVerticalLine()) {
                if (gameField.isLockLeft(currentLine)) {
                    gameField.setLockOnLeft(currentLine, clientInfo.getOrderNumber());
                    stepInfo.incCountOfSquares(clientInfo);
                }
                if (gameField.isLockRight(currentLine)) {
                    gameField.setLockOnRight(currentLine, clientInfo.getOrderNumber());
                    stepInfo.incCountOfSquares(clientInfo);
                }
            }

            sendStepToAnotherPlayers(stepInfo, clientInfo);
            changeActivePlayer();

            System.out.println(gameField);

            if(isFinished()) {
                sendResults(getResults());
                return false;
            }

            return true;
        }
    }

    public void sendOpponentsInfo(ClientInfo clientInfo) throws RemoteException {
        for(ClientInfo opponentInfo : clientsMap.values()) {
            if( ! opponentInfo.equals(clientInfo)) {
                clientsStubMap.get(clientInfo.getOrderNumber()).setOpponentInfo(opponentInfo);
                clientsStubMap.get(opponentInfo.getOrderNumber()).setOpponentInfo(clientInfo);
                break;
            }
        }
    }

    private void startGame() throws RemoteException {
        try {
            Client clientStub = (Client) registry.lookup(UNIQUE_BINDING_CLIENT_PREFIX + 0);
            clientStub.allowStep(true);
        } catch (NotBoundException e) {
            throw new RemoteException(e.getMessage(), e);
        }
    }

    private void initClientsStubMap () throws RemoteException {
        try {
            for(ClientInfo clientInfo : clientsMap.values()) {
                Client clientStub = (Client) registry.lookup(UNIQUE_BINDING_CLIENT_PREFIX + clientInfo.getOrderNumber());
                clientsStubMap.put(clientInfo.getOrderNumber(), clientStub);
            }
        } catch (NotBoundException e) {
            throw new RemoteException(e.getMessage(), e);
        }
    }

    private void sendStepToAnotherPlayers(StepInfo stepInfo, ClientInfo clientInfo) throws RemoteException {
        if(clientsStubMap.isEmpty()) initClientsStubMap();

        for(Map.Entry<Integer, Client> entry : clientsStubMap.entrySet()) {
            entry.getValue().handleStepResult(stepInfo, clientInfo);
        }
    }

    private boolean isFinished() {
        return gameField.isAllSqaresFilled();
    }

    private void sendResults(GameResults gameResults) throws RemoteException {
        for(Client client : clientsStubMap.values()) {
            client.handleResults(gameResults);
        }
    }

    public GameResults getResults() {
        Map<ClientInfo, ClientScore> results = new HashMap<>(clientCount);
        ClientInfo winner = null;
        int maxScore = 0;

        for(ClientInfo clientInfo : clientsMap.values()) {
            results.put(clientInfo, new ClientScore(0));
        }

        for(Integer clientOrder : gameField.getSquares().values()) {
            ClientInfo clientInfo = clientsMap.get(clientOrder);
            ClientScore clientScore = results.get(clientInfo);

            clientScore.increase(1);

            if(clientScore.getSquareCount() > maxScore) {
                maxScore = clientScore.getSquareCount();
                winner = clientInfo;
            }
        }

        return new GameResults(winner, results);
    }

    private void changeActivePlayer() throws RemoteException {
        clientsStubMap.get(currentClient).allowStep(false);
        nextClient();
        clientsStubMap.get(currentClient).allowStep(true);
    }

    private int nextClient() {
        currentClient++;
        if(currentClient >= clientCount) {
            currentClient %= clientCount;
        }
        return currentClient;
    }

    public static void main(String[] args) throws RemoteException, AlreadyBoundException, InterruptedException {
        Server server = new ServerImpl();
        final Registry registry = LocateRegistry.createRegistry(2732);

        Remote stub = UnicastRemoteObject.exportObject(server, 0);
        registry.bind(UNIQUE_BINDING_SERVER_NAME, stub);

        Thread.sleep(Integer.MAX_VALUE);
    }
}
