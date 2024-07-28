package client.network;

import client.network.socket.SocketRequestSender;
import shared.Model.Player;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Status {
    private static Status INSTANCE;
    private String macAddress;
    private SocketRequestSender socket;
    private MyResponseHandler responseHandler;
    private boolean connectedToServer;
    private Player player;

    private Status() {
        this.connectedToServer = false;
        this.responseHandler = new MyResponseHandler();
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

    public MyResponseHandler getResponseHandler() {
        return responseHandler;
    }

    public void setResponseHandler(MyResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
    }

    private String findMacAddress(){
        String[] hexadecimal = null;

        try {
            InetAddress localHost = InetAddress.getLocalHost();
            NetworkInterface ni = NetworkInterface.getByInetAddress(localHost);
            byte[] hardwareAddress = ni.getHardwareAddress();

            hexadecimal = new String[hardwareAddress.length];
            for (int i = 0; i < hardwareAddress.length; i++) {
                hexadecimal[i] = String.format("%02X", hardwareAddress[i]);
            }
        } catch (UnknownHostException | SocketException e) {
            System.out.println("Unable to find local host");
            throw new RuntimeException(e);
        }

        return macAddress = String.join("-", hexadecimal);
    }



}
