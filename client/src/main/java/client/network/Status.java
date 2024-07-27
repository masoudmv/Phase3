package client.network;

import client.network.socket.SocketRequestSender;
import shared.Model.Player;

public class Status {
    private static Status INSTANCE;
    private SocketRequestSender socket;
    private boolean connectedToServer;
    private Player player;

    private Status() {
        connectedToServer = false;
    }

    public static Status getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new Status();
        }
        return INSTANCE;
    }

    public SocketRequestSender getSocket() {
        return socket;
    }

    public void setSocket(SocketRequestSender socket) {
        this.socket = socket;
    }

    public boolean isConnectedToServer() {
        return connectedToServer;
    }

    public void setConnectedToServer(boolean connectedToServer) {
        this.connectedToServer = connectedToServer;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
