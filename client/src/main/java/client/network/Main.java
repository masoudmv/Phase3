package client.network;


import client.network.toolBox.MainFrame;
import client.network.toolBox.Menu;
import client.network.toolBox.SquadMenu;

import javax.swing.*;
import javax.swing.text.html.Option;
import java.io.IOException;

import static client.network.toolBox.utils.tryConnection;


public class Main {
    public static void main(String[] args) {
        Status.getINSTANCE();
        tryConnection();



//        var name = JOptionPane.showInputDialog("Please Enter Your username", JOptionPane.YES_OPTION);
//        JOptionPane.showMessageDialog(frame, "Username set to: " + username);

//        MyResponseHandler serverHandler = new MyResponseHandler(socketRequestSender);
//        socketRequestSender.sendRequest(new HiRequest()).run(serverHandler);


//        InetAddress localHost = InetAddress.getLocalHost();
//        String address = returnMACAddress(localHost);
    }
}