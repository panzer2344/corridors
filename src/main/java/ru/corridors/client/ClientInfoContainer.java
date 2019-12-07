package ru.corridors.client;

import ru.corridors.dto.ClientInfo;

public class ClientInfoContainer {

    public static final ClientInfoContainer instance = new ClientInfoContainer();

    private ClientInfo clientInfo;
    private ClientInfo opponentInfo;

    private ClientInfoContainer() {}

    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    public ClientInfo getOpponentInfo() {
        return opponentInfo;
    }

    public void setOpponentInfo(ClientInfo opponentInfo) {
        this.opponentInfo = opponentInfo;
    }
}
