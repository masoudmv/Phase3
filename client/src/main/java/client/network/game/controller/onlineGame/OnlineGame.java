package client.network.game.controller.onlineGame;

import client.network.game.controller.constants.Constants;
import client.network.game.controller.input.UserInputHandler;
import client.network.game.view.MainFrame;


import javax.swing.*;

public class OnlineGame {
    private static OnlineGame INSTANCE;
    private boolean isPaused = false;
//    private static GameLoop gameLoop;




    public OnlineGame (){
        INSTANCE = this;
        Constants.RADIUS = 15;
        MainFrame frame = MainFrame.getINSTANCE();
//        frame.addMouseListener(new MouseController());
//        frame.addMouseMotionListener(new MouseController());


        SwingUtilities.invokeLater(() -> {
//            MainFrame.getINSTANCE().add(label);

            MainFrame.getINSTANCE().addKeyListener(UserInputHandler.getINSTANCE());
            new OnlineGameLoop();


        });
    }

    public static OnlineGame getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new OnlineGame();
        return INSTANCE;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }
    public void openShop(){}
    public void closeShop(){}





    public static void nullifyGameInstance() {
        INSTANCE = null;
        // MainFrame.getINSTANCE().removeKeyListener(gameLoop);
        // todo: now the gameLoop doesnt contain KeyLister
//        gameLoop =null;
    }
}
