package client.network;


import client.network.game.controller.MouseController;
import client.network.game.controller.onlineGame.ClientGame;
import client.network.game.view.MainFrame;
import client.network.game.view.charactersView.*;
import client.network.game.view.junks.AbilityShopPanel;
import client.network.socket.SocketRequestSender;


import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {
        Status.getINSTANCE();

        SocketRequestSender socketRequestSender = new SocketRequestSender();
        Status.getINSTANCE().setConnectedToServer(true);
        Status.getINSTANCE().setSocket(socketRequestSender);


        EpsilonView.loadImage();
        OrbView.loadImage();
        ArchmireView.loadImage();
        BabyArchmireView.loadImage();
        BarricadosView.loadImage();
        NecropickView.loadImage();
        NonrigidBulletView.loadImage();

//        MainFrame.getINSTANCE().handleAbilityShopPanelToggle();


        MainFrame.getINSTANCE().addMouseListener(new MouseController());
        new ClientGame();
    }
}