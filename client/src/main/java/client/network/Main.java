package client.network;


import client.network.containers.PanelManager;

import java.io.IOException;

import static client.network.toolBox.utils.tryConnection;


public class Main {
    public static void main(String[] args) throws IOException {
        Status.getINSTANCE();
        tryConnection();
        PanelManager.displayMainMenu();





//        var name = JOptionPane.showInputDialog("Please Enter Your username", JOptionPane.YES_OPTION);
//        JOptionPane.showMessageDialog(frame, "Username set to: " + username);

//        SocketRequestSender socketRequestSender = new SocketRequestSender();
//        MyResponseHandler serverHandler = new MyResponseHandler();
//        String macAddress = Status.getINSTANCE().getPlayer().getMacAddress();
//        socketRequestSender.sendRequest(new IdentificationRequest(macAddress)).run(serverHandler);


//        InetAddress localHost = InetAddress.getLocalHost();
//        String address = returnMACAddress(localHost);





//        List<Squad> squads = new ArrayList<>();
//        squads.add(new Squad(new Player("massooood")));
//        squads.add(new Squad(new Player("massooood")));
//        squads.add(new Squad(new Player("massooood")));
//        squads.add(new Squad(new Player("massooood")));
//        squads.add(new Squad(new Player("massooood")));
//
//        SwingUtilities.invokeLater(() -> {
//            MainFrame frame = MainFrame.getINSTANCE();
//            SquadListPanel squadListPanel = new SquadListPanel(squads, frame);
//            frame.switchToPanel(squadListPanel);
//        });
    }
}