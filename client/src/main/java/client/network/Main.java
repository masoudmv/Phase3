package client.network;


import client.network.toolBox.Menu;

import javax.swing.*;
import java.io.IOException;

import static client.network.toolBox.utils.tryConnection;


public class Main {
    public static void main(String[] args) {
        Status.getINSTANCE();
        tryConnection();




//        MyResponseHandler serverHandler = new MyResponseHandler(socketRequestSender);
//        socketRequestSender.sendRequest(new HiRequest()).run(serverHandler);


//        InetAddress localHost = InetAddress.getLocalHost();
//        String address = returnMACAddress(localHost);
    }
}