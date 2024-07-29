package client.network;

import client.network.socket.SocketRequestSender;
import shared.Model.Player;
import shared.Model.Squad;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class Status {
    private static Status INSTANCE;
    private String macAddress;
    private SocketRequestSender socket;
    private ClientsideResHandler responseHandler;
    private boolean connectedToServer;
    private Player player;
    private Squad opponent;

    private Status() {
        this.connectedToServer = false;
        this.responseHandler = new ClientsideResHandler();
        String macAddress = findMacAddress();
        this.player = new Player(macAddress);
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

    public Squad getOpponent() {
        return opponent;
    }

    public void setOpponent(Squad opponent) {
        this.opponent = opponent;
    }

    public ClientsideResHandler getResponseHandler() {
        return responseHandler;
    }

    public void setResponseHandler(ClientsideResHandler responseHandler) {
        this.responseHandler = responseHandler;
    }

    private String findMacAddress() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface ni = networkInterfaces.nextElement();

                // Skip loopback and non-ethernet interfaces
                if (ni.isLoopback() || !ni.getName().startsWith("eth")) {
                    continue;
                }

                byte[] hardwareAddress = ni.getHardwareAddress();
                if (hardwareAddress != null && hardwareAddress.length > 0) {
                    String[] hexadecimal = new String[hardwareAddress.length];
                    for (int i = 0; i < hardwareAddress.length; i++) {
                        hexadecimal[i] = String.format("%02X", hardwareAddress[i]);
                    }
                    return String.join("-", hexadecimal);
                }
            }
        } catch (SocketException e) {
            System.out.println("Unable to find network interfaces");
            throw new RuntimeException(e);
        }

        throw new RuntimeException("No valid MAC address found");
    }



}
