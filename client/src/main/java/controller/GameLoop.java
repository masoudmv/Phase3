package controller;

import model.charactersModel.BulletModel;
import model.charactersModel.CollectibleModel;
import model.charactersModel.*;
import model.charactersModel.blackOrb.BlackOrb;
import model.charactersModel.smiley.Fist;
import model.charactersModel.smiley.Hand;
import model.charactersModel.SmileyBullet;
import model.charactersModel.smiley.Smiley;
import model.entities.Profile;
import model.movement.Movable;
import view.*;
import view.junks.GameOverPanel;

import view.junks.VictoryPanel;

import javax.swing.*;
import javax.swing.Timer;

import java.util.concurrent.atomic.AtomicBoolean;

import static controller.constants.Constants.*;
import static controller.UserInterfaceController.*;
import static controller.Game.*;
import static controller.MouseController.*;
import static controller.Sound.*;
import static model.FinalPanelModel.finalPanelModels;
import static model.PanelManager.handlePanelPanelCollision;
import static model.charactersModel.CollectibleModel.collectibleModels;
//import static model.NonRigid.nonRigids;
import static model.charactersModel.GeoShapeModel.entities;
import static model.charactersModel.NecropickModel.necropickModels;
import static model.charactersModel.SquarantineModel.squarantineModels;
import static model.charactersModel.TrigorathModel.trigorathModels;
import static model.charactersModel.smiley.Fist.fists;
import static model.charactersModel.smiley.Hand.hands;
import static model.charactersModel.SmileyBullet.smileyBullets;
//import static model.collision.Coll.colls;
import static model.collision.Collidable.collidables;
import static model.movement.Movable.movables;
import static view.FinalPanelView.finalPanelViews;
import static view.MainFrame.label;
//import static view.Panel.panels;


public class GameLoop implements Runnable {



    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicBoolean exit = new AtomicBoolean(false);
    private final AtomicBoolean paused = new AtomicBoolean(false);

    public static GameLoop INSTANCE;


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

    public GameLoop() {
        decreaseVelocities=false;
        movementInProgress = false;

        playThemeSound();


        ELAPSED_TIME = 0;
        inGameXP = 1000;

        MainFrame frame = MainFrame.getINSTANCE();
        frame.addMouseListener(new MouseController());
        frame.addMouseMotionListener(new MouseController());



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
        if (paused.get()) return;


        label.setText("<html>Wave: "+ Game.wave + "<br>Elapsed Time: "+ (int) Game.ELAPSED_TIME
                + "<br> XP: "+Game.inGameXP +"<br>HP: "+ EpsilonModel.getINSTANCE().getHp());

        long currentTickTime = System.currentTimeMillis();
        long interval = currentTickTime - lastTickTime;

        lastTickTime = currentTickTime;

        ELAPSED_TIME += (double) interval / 1000;

//        if (!EpsilonModel.getINSTANCE().isImpactInProgress()) updateMovement(); // Single REsponsibility



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




        for (FinalPanelView f : finalPanelViews){
            f.setLocation(calculateLocationOfFinalPanelView(f.getId()));
            f.setSize(calculateDimensionOfFinalPanelView(f.getId()));
        }



        updateGeoShapeViewProperties();
        SwingUtilities.invokeLater(MainFrame.getINSTANCE()::repaint);
    }

    public void updateModel() {
        if (paused.get()) return;


        for (int i = 0; i < finalPanelModels.size(); i++) {
            finalPanelModels.get(i).panelMotion();  // todo
        }


        for (int i = 0; i < finalPanelModels.size(); i++) {
            for (int j = i + 1; j < finalPanelModels.size(); j++) {
                handlePanelPanelCollision(finalPanelModels.get(i), finalPanelModels.get(j));
            }
        }

        for (int i = 0; i < collidables.size(); i++) {
            for (int j = i + 1; j < collidables.size(); j++) {
                collidables.get(i).checkCollision(collidables.get(j));
            }
        }



        for (int i = 0; i < BlackOrb.blackOrbs.size(); i++) {
            BlackOrb.blackOrbs.get(i).update();
        }






        updateCount++;


        for (int i = 0; i < squarantineModels.size(); i++) {
            if (squarantineModels.get(i).isImpactInProgress()) {
                squarantineModels.get(i).getDirection().accelerateDirection(squarantineModels.get(i).impactMaxVelocity);
                if (squarantineModels.get(i).getDirection().getMagnitude() > squarantineModels.get(i).impactMaxVelocity) {
                    squarantineModels.get(i).setImpactInProgress(false);
                }
            }



        }
        for (int i = 0; i < trigorathModels.size(); i++) {
            if (trigorathModels.get(i).isImpactInProgress()) {
                trigorathModels.get(i).getDirection().accelerateDirection(trigorathModels.get(i).impactMaxVelocity);
                if (trigorathModels.get(i).getDirection().getMagnitude() > trigorathModels.get(i).impactMaxVelocity) {
                    trigorathModels.get(i).setImpactInProgress(false);
                }
            }
        }

        EpsilonModel epsilonModel = EpsilonModel.getINSTANCE();
        if (epsilonModel.isImpactInProgress()) {
            epsilonModel.getDirection().accelerateDirection(4);

            if (epsilonModel.getDirection().getMagnitude() > 3.8) {
                epsilonModel.setImpactInProgress(false);
            }
        }
        for (CollectibleModel collectibleModel : collectibleModels) {
            if (collectibleModel.impactInProgress) {
                collectibleModel.getDirection().accelerateDirection(collectibleModel.impactMaxVel);
                if (collectibleModel.getDirection().getMagnitude() > collectibleModel.impactMaxVel) {
                    collectibleModel.impactInProgress = false;
                }
            }
            collectibleModel.friction();
            collectibleModel.update();
        }


        UserInputHandler.updateMovement();



        for (GeoShapeModel entity : entities){
            entity.update();
        }




//        if (acesoInProgress) {
//            EpsilonModel epsilon = EpsilonModel.getINSTANCE();
//            if (lastHpRegainTime == -1) {
//                epsilon.sumHpWith(1);
//                lastHpRegainTime = ELAPSED_TIME;
//            } else if (ELAPSED_TIME - lastHpRegainTime > hpRegainRate) {
//                epsilon.sumHpWith(1);
//                lastHpRegainTime = ELAPSED_TIME;
//            }
//        }




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
        final double timeF = (double) 1000000000 / Profile.getCurrent().FPS;
        final double timeU = (double) 1000000000 / Profile.getCurrent().UPS;
        double deltaU = 0, deltaF = 0;
        int frames = 0, ticks = 0;
        long timer = System.currentTimeMillis();

        while (!exit.get()) {
            long currentTime = System.nanoTime();
            deltaU += (currentTime - initialTime) / timeU;
            deltaF += (currentTime - initialTime) / timeF;
            initialTime = currentTime;

            if (deltaU >= 1) {
                updateModel();
                ticks++;
                deltaU--;
            }

            if (deltaF >= 1) {
                updateView();
                frames++;
                deltaF--;
            }

            if (System.currentTimeMillis() - timer > 1000) {
                System.out.println(String.format("UPS: %s, FPS: %s", ticks, frames));
                frames = 0;
                ticks = 0;
                timer += 1000;
            }
        }
//        stop();
    }

    public static GameLoop getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new GameLoop();
        return INSTANCE;
    }

    public boolean isRunning() {
        return running.get();
    }

    public boolean isOn() {
        return !exit.get();
    }


    public void pauseGame() {
        paused.set(true);
    }

    public void resumeGame() {
        paused.set(false);
    }

    public boolean isPaused() {
        return paused.get();
    }

}










