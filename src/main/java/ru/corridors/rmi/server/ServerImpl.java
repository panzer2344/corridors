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
    public static final int DEFAULT_SQUARE_COUNT = 2;

    private static int clientCount = 0;
    private static int currentClient = 0;

    private static final GameField gameField = new GameField(DEFAULT_SQUARE_COUNT);

    private static final Map<Integer, ClientInfo> clientsMap = new ConcurrentHashMap<>();

//    private Client client1;
//    private Client client2;

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

        if(clientCount >= 2)
            startGame();

        return clientInfo;
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
//            if(isFinished()) {
//                sendResults(getResults());
//                return false;
//            }

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

    private Client getClientStub(Integer clientId) throws RemoteException {
        Client clientStub = clientsStubMap.get(clientId);

        if(clientStub == null) {
            try {
                clientStub = (Client) registry.lookup(UNIQUE_BINDING_CLIENT_PREFIX + clientId);
            } catch (NotBoundException e) {
                throw new RemoteException(e.getMessage(), e);
            }
            clientsStubMap.put(clientId, clientStub);
        }

        return clientStub;
    }

    private void startGame() throws RemoteException {
        //Client clientStub = clientsStubMap.get(clientCount);
        Client clientStub = getClientStub(currentClient);
        clientStub.allowStep(true);
    }

    private void sendStepToAnotherPlayers(StepInfo stepInfo, ClientInfo clientInfo) throws RemoteException {
        for(Map.Entry<Integer, Client> entry : clientsStubMap.entrySet()) {
            if( !entry.getKey().equals(clientInfo.getOrderNumber())) {
                entry.getValue().handleOpponentStep(stepInfo, clientInfo);
            }
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

    private void changeActivePlayer() throws RemoteException {
        getClientStub(currentClient).allowStep(false);
        nextClient();
        getClientStub(currentClient).allowStep(true);
    }

    private int nextClient() {
        currentClient++;
        if(currentClient >= clientCount) {
            currentClient %= clientCount;
        }
        return currentClient;
    }

//    private Client getClientStub(ClientInfo clientInfo) throws RemoteException, NotBoundException {
//        Client clientStub = clientsStubMap.get(clientInfo.getOrderNumber());
//
//        if(clientStub == null) {
//            clientStub = (Client) registry.lookup(UNIQUE_BINDING_CLIENT_PREFIX + clientInfo.getOrderNumber());
//            clientsStubMap.put(clientInfo.getOrderNumber(), clientStub);
//        }
//
//        return clientStub;
//    }

    public static void main(String[] args) throws RemoteException, AlreadyBoundException, InterruptedException {
        Server server = new ServerImpl();
        final Registry registry = LocateRegistry.createRegistry(2732);

        Remote stub = UnicastRemoteObject.exportObject(server, 0);
        registry.bind(UNIQUE_BINDING_SERVER_NAME, stub);

        Thread.sleep(Integer.MAX_VALUE);
    }
}
