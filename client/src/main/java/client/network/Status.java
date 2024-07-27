package client.network;

import client.network.socket.SocketRequestSender;

import java.net.InetAddress;
import java.net.NetworkInterface;

public class Status {
    static Status INSTANCE = null;
    private boolean isConnectedToServer = false;
    private String macAddress;
    private SocketRequestSender socket;



    private Status() {
        this.setMACAddress();
    }


    public void setMACAddress(){
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            NetworkInterface ni = NetworkInterface.getByInetAddress(localHost);

            byte[] hardwareAddress = ni.getHardwareAddress();

            String[] hexadecimal = new String[hardwareAddress.length];
            for (int i = 0; i < hardwareAddress.length; i++) {
                hexadecimal[i] = String.format("%02X", hardwareAddress[i]);
            }
            this.macAddress = String.join("-", hexadecimal);
        } catch (Exception e){
            e.printStackTrace();
            // implement better exception handling ...



        }
    }

    public SocketRequestSender getSocket() {
        return socket;
    }

    public void setSocket(SocketRequestSender socket) {
        this.socket = socket;
    }




    public String getMacAddress() {
        return macAddress;
    }

    public static Status getINSTANCE(){
        if (INSTANCE == null) INSTANCE = new Status();
        return INSTANCE;
    }

    public boolean isConnectedToServer() {
        return isConnectedToServer;
    }

    public void setConnectedToServer(boolean connectedToServer) {
        this.isConnectedToServer = connectedToServer;
    }
}
