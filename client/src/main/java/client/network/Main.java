package client.network;


import client.network.containers.PanelManager;
import client.network.game.controller.MouseController;
import client.network.game.controller.onlineGame.ClientGame;
import client.network.game.view.MainFrame;
import client.network.game.view.charactersView.*;
import client.network.game.view.junks.AbilityShopPanel;
import client.network.socket.SocketRequestSender;
import client.network.toolBox.utils;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static client.network.RequestFactory.createIdentificateReq;


public class Main {
    public static void main(String[] args) throws IOException {
        Status.getINSTANCE();
        utils.tryConnection();
        PanelManager.displayMainMenu();





        SocketRequestSender socketRequestSender = new SocketRequestSender();
        Status.getINSTANCE().setConnectedToServer(true);
        Status.getINSTANCE().setSocket(socketRequestSender);

//        createIdentificateReq();

        EpsilonView.loadImage();
        OrbView.loadImage();
        ArchmireView.loadImage();
        BabyArchmireView.loadImage();
        BarricadosView.loadImage();
        NecropickView.loadImage();
        NonrigidBulletView.loadImage();


//        MainFrame.getINSTANCE().addMouseListener(new MouseController());
//        new ClientGame();

    }
}