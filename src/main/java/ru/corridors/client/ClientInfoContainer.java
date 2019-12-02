package ru.corridors.client;

import ru.corridors.dto.ClientInfo;

public class ClientInfoContainer {

    public static final ClientInfoContainer instance = new ClientInfoContainer();

    private ClientInfo clientInfo;

    private ClientInfoContainer() {}

    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    public ClientInfo getClientInfo() {
        return clientInfo;
    }
}
