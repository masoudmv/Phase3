package client.network.game.controller.onlineGame;

import client.network.game.controller.input.UserInputHandler;

import client.network.game.view.FinalPanelView;
import client.network.game.view.MainFrame;


import javax.swing.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static client.network.RequestFactory.createStateRequest;
import static client.network.game.controller.UserInterfaceController.*;
import static client.network.game.view.FinalPanelView.finalPanelViews;


public class OnlineGameLoop implements Runnable{

    public static OnlineGameLoop INSTANCE;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicBoolean exit = new AtomicBoolean(false);

    private long lastUpdateTime;

    private volatile String FPS_UPS = "";
    private long lastTickTime = System.currentTimeMillis();
    private int frameCount = 0;


    private int updateCount = 0;

    public static boolean movementInProgress = false;
    private final int MOVEMENT_DELAY = 10; // Delay in milliseconds
    private Timer gameLoop;

    private int extraBullet=0;

    double lastHpRegainTime=-1;
    private double hpRegainRate = Double.MAX_VALUE;

    public static boolean decreaseVelocities;



    private boolean acesoInProgress=false;

    public OnlineGameLoop() {
        decreaseVelocities=false;

        movementInProgress = false;






        MainFrame frame = MainFrame.getINSTANCE();
//        frame.addKeyListener(UserInputHandler.getINSTANCE());
//        frame.addMouseListener(new MouseController());
//        frame.addMouseMotionListener(new MouseController());



        INSTANCE = this;

        this.start();
    }



    public void start() {
        if (running.get()) return;
        running.set(true);
        new Thread(this).start();
    }

    public void stop() {
        if (!running.get()) return;
        running.set(false);
        exit.set(true);
    }





    public void updateView() {




//        label.setText("<html>Wave: "+ Game.wave + "<br>Elapsed Time: "+ (int) Game.ELAPSED_TIME
//                + "<br> XP: "+Game.inGameXP +"<br>HP: "+ EpsilonModel.getINSTANCE().getHp());

        long currentTickTime = System.currentTimeMillis();
        long interval = currentTickTime - lastTickTime;

        lastTickTime = currentTickTime;



//        if (!EpsilonModel.getINSTANCE().isImpactInProgress()) UserInputHandler.updateMovement();

        UserInputHandler.updateMovement();


        // Increment frame count every time updateView is called
        frameCount++;
        // Current time in milliseconds
        long currentTime = System.currentTimeMillis();

        // Check if one second has passed
        if (currentTime - lastUpdateTime >= 1000) {
            // Reset frame counter and last update time for the next second
            frameCount = 0;
            lastUpdateTime = currentTime;
        }

//
        for (FinalPanelView f : finalPanelViews){
            f.setLocation(calculateLocationOfFinalPanelView(f.getId()));
            f.setSize(calculateDimensionOfFinalPanelView(f.getId()));
        }
//
//        updateGeoShapeViewProperties();
//        SwingUtilities.invokeLater(MainFrame.getINSTANCE()::repaint);
        createStateRequest();
    }



    public Timer getGameLoop() {
        return gameLoop;
    }

    public void setGameLoop(Timer gameLoop) {
        this.gameLoop = gameLoop;
    }









    @Override
    public void run() {
        long initialTime = System.nanoTime();
        final double timeF = (double) 1000000000 / 60;
        double deltaF = 0;
        int frames = 0;
        long timer = System.currentTimeMillis();

        while (!exit.get()) {
            long currentTime = System.nanoTime();
            deltaF += (currentTime - initialTime) / timeF;
            initialTime = currentTime;

            if (deltaF >= 1) {
                updateView();
                frames++;
                deltaF--;
            }

            if (System.currentTimeMillis() - timer > 1000) {
                System.out.println(String.format("FPS: %s", frames));
                frames = 0;
                timer += 1000;
            }
        }
    }


    public static OnlineGameLoop getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new OnlineGameLoop();
        return INSTANCE;
    }

    public boolean isRunning() {
        return running.get();
    }

    public boolean isOn() {
        return !exit.get();
    }

}










