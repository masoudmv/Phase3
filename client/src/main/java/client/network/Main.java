package client.network;


import client.network.game.controller.onlineGame.OnlineGame;
import client.network.game.view.MainFrame;
import client.network.game.view.charactersView.EpsilonView;
import client.network.game.view.charactersView.SquarantineView;
import client.network.socket.SocketRequestSender;
//import controller..UserInputHandler;

import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {
        Status.getINSTANCE();
//        tryConnection();
//        PanelManager.displayMainMenu();


        SocketRequestSender socketRequestSender = new SocketRequestSender();
        Status.getINSTANCE().setConnectedToServer(true);
        Status.getINSTANCE().setSocket(socketRequestSender);


        EpsilonView.loadImage();


        MainFrame.getINSTANCE();
        new OnlineGame();
    }
}