package client.network.toolBox;

import client.network.RequestFactory;
import client.network.Status;
import client.network.socket.SocketRequestSender;
import shared.request.IdentificationRequest;

import javax.swing.*;
import static client.network.toolBox.UIMessageConstants.CONNECTED_TO_SERVER;

public class utils {
    public static void tryConnection() {
        try {
            SocketRequestSender socketRequestSender = new SocketRequestSender();
            JOptionPane.showMessageDialog(MainFrame.getINSTANCE(), CONNECTED_TO_SERVER.getValue(), "Message", JOptionPane.INFORMATION_MESSAGE);

            // Implement menu loading in Online mode and other logic...
            Status.getINSTANCE().setConnectedToServer(true);
            Status.getINSTANCE().setSocket(socketRequestSender);

            // send identification Request ...
//            String macAddress = Status.getINSTANCE().getPlayer().getMacAddress();

            RequestFactory.createIdentificateReq();
            System.out.println("no i am here");

            // start a thread to check connection with server ...
            new Thread(new Menu.ConnectionChecker()).start();


//            MainFrame.getINSTANCE().switchToPanel(Menu.getINSTANCE());
        } catch (Exception e) {
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
            }
        }
    }
}
