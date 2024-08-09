package controller;

import model.WaveManager;
import model.charactersModel.CollectibleModel;
import model.charactersModel.*;
import model.charactersModel.blackOrb.BlackOrb;
import model.entities.Profile;
import view.*;
import javax.swing.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static controller.UserInterfaceController.*;
import static controller.Game.*;
import static model.FinalPanelModel.finalPanelModels;
import static model.PanelManager.handlePanelPanelCollision;
import static model.charactersModel.CollectibleModel.collectibleModels;
import static model.charactersModel.GeoShapeModel.entities;
import static model.charactersModel.SquarantineModel.squarantineModels;
import static model.charactersModel.TrigorathModel.trigorathModels;
import static model.collision.Collidable.collidables;

import static view.FinalPanelView.finalPanelViews;
import static view.MainFrame.label;


public class GameLoop implements Runnable {
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicBoolean exit = new AtomicBoolean(false);
    private final AtomicBoolean paused = new AtomicBoolean(false);
    public static GameLoop INSTANCE;
    public static boolean movementInProgress = false;
    private final WaveManager waveManager;
    private final Thread waveManagerThread;



    public GameLoop() {
        movementInProgress = false;
        MainFrame frame = MainFrame.getINSTANCE();
        frame.addMouseListener(new MouseController());
        frame.addMouseMotionListener(new MouseController());
        INSTANCE = this;
        this.waveManager = new WaveManager();
        this.waveManagerThread = new Thread(waveManager);
        waveManagerThread.start();
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

        label.setText("<html>Wave: "+ Game.wave + "<br>Elapsed Time: "+ (int) Game.elapsedTime
                + "<br> XP: "+ Profile.getCurrent().inGameXP +"<br>HP: "+ EpsilonModel.getINSTANCE().getHp());

        elapsedTime += 0.017;


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

    public WaveManager getWaveManager() {
        return waveManager;
    }

}










