package client.network.toolBox;

import client.network.RequestFactory;
import client.network.Status;
import client.network.containers.MainFrame;
import client.network.containers.MainMenu;
import client.network.containers.PanelManager;
import client.network.socket.SocketRequestSender;

import javax.swing.*;
import static shared.constants.UIMessageConstants.CONNECTED_TO_SERVER;

public class utils {
    public static void tryConnection() {
        try {
            SocketRequestSender socketRequestSender = new SocketRequestSender();
            JOptionPane.showMessageDialog(MainFrame.getINSTANCE(), CONNECTED_TO_SERVER.getValue(), "Message", JOptionPane.INFORMATION_MESSAGE);

            // Implement menu loading in Online mode and other logic...
            Status.getINSTANCE().setConnectedToServer(true);
            Status.getINSTANCE().setSocket(socketRequestSender);

            // send identification Request ...
            RequestFactory.createIdentificateReq();

            // start a thread to check connection with server ...
            new Thread(new ConnectionChecker()).start();


//            MainFrame.getINSTANCE().switchToPanel(Menu.getINSTANCE());
        } catch (Exception e) {
            System.out.println("could not connect. loading in offline mode ...");
            Object[] options = {"Yes", "Retry", "Cancel"};
            int choice = JOptionPane.showOptionDialog(
                    MainFrame.getINSTANCE(), // Parent component
                    "Could not connect to server. Do you want to proceed in Offline Mode?", // Message to display
                    "Custom Options", // Dialog title
                    JOptionPane.YES_NO_CANCEL_OPTION, // Option type (Yes, No, Cancel)
                    JOptionPane.QUESTION_MESSAGE, // Message type (question icon)
                    null, // Custom icon (null means no custom icon)
                    options, // Custom options array
                    options[2] // Initial selection (default is "Cancel")
            );

            if (choice == JOptionPane.YES_OPTION) {
                Status.getINSTANCE().setConnectedToServer(false);
                Status.getINSTANCE().setSocket(null);
            } else if (choice == JOptionPane.NO_OPTION) {
                tryConnection();
            } else {
                System.out.println("Cancel chosen");
            }
        }
    }


    static class ConnectionChecker implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);

                    synchronized (MainMenu.getINSTANCE()) {
                        if (!Status.getINSTANCE().isConnectedToServer()) break;
                    }
                    RequestFactory.createIdentificateReq();
                    System.out.println("Connection check successful.");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(MainFrame.getINSTANCE(), "Connection Lost!", "Error", JOptionPane.ERROR_MESSAGE);
                    Status.getINSTANCE().setConnectedToServer(false);
                    PanelManager.displayMainMenu();
                    break;
                }
            }
        }
    }
}
