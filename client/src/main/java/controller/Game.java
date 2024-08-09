package controller;

import controller.constants.Constants;
import model.MyPolygon;
import model.charactersModel.EpsilonModel;
//import view.MainPanel;

import model.charactersModel.TrigorathModel;
import org.example.GraphicalObject;
import org.example.Main;
import view.MainFrame;
//import view.MainPanel;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import static view.MainFrame.label;


public class Game {
    private static Game INSTANCE;
    public static int inGameXP = 1000;
    public static double ELAPSED_TIME = 0;
    public static int wave;
    private boolean isPaused = false;



    private GameLoop gameLoop;




    public Game (){
        INSTANCE = this;
        ELAPSED_TIME =0;
        inGameXP=0;
        wave=1;
        Constants.RADIUS = 15;


        BufferedImage b = EpsilonModel.loadImage();
        GraphicalObject bows = new GraphicalObject(b);
        MyPolygon pol = bows.getMyBoundingPolygon();

        new EpsilonModel(new Point2D.Double(1000, 700), pol);


        TrigorathModel.create();
        TrigorathModel.create();
        TrigorathModel.create();
        TrigorathModel.create();

        SwingUtilities.invokeLater(() -> {
            MainFrame.getINSTANCE().add(label);


            MainFrame.getINSTANCE().addKeyListener(UserInputHandler.getINSTANCE());

            gameLoop = new GameLoop();


        });
    }

    public static Game getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new Game();
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
    public int getInGameXp() {
        return inGameXP;
    }

    public void sumInGameXpWith(int xp){
        this.inGameXP += xp;
    }


    public static void nullifyGameInstance() {
        INSTANCE = null;
//        MainFrame.getINSTANCE().removeKeyListener(gameLoop); // todo: now the gameLoop doesnt contain KeyLister
    }

    public GameLoop getGameLoop() {
        return gameLoop;
    }
}
