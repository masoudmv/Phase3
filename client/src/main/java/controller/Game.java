package controller;

import controller.constants.Constants;
import model.charactersModel.EpsilonModel;
//import view.MainPanel;
import org.example.Main;
import view.MainFrame;
//import view.MainPanel;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static view.MainFrame.label;


public class Game {
    private static Game INSTANCE;
    public static int inGameXP;
    public static double ELAPSED_TIME =0;
    public static int wave;
    private boolean isPaused = false;
    private EpsilonModel epsilon;
    public static SkillTreeAbility skillTreeAbility=null;
    public static boolean empowerIsOn = false;
    public static double empowerEndTime = Double.MAX_VALUE;
    public static double empowerStartTime = Double.MAX_VALUE;
    public static Clip clip;
    public static BufferedImage bufferedImage;
    public static Image bufferedImageResult;
    public static SkillTreeAbility activeAbility;
    private static GameLoop gameLoop;




    public Game (){
        INSTANCE = this;
        ELAPSED_TIME =0;
        inGameXP=0;
        wave=1;
        Constants.RADIUS = 15;
        MainFrame frame = MainFrame.getINSTANCE();
        frame.addMouseListener(new MouseController());
        frame.addMouseMotionListener(new MouseController());


        SwingUtilities.invokeLater(() -> {
            MainFrame.getINSTANCE().add(label);

//            MainPanel.getINSTANCE();
//            gameLoop = new GameLoop();
            MainFrame.getINSTANCE().addKeyListener(UserInputHandler.getINSTANCE());
            new GameLoop();
//            GameLoop.getINSTANCE().initializeGame();
//            GameLoop.getINSTANCE().start();
//            GameLoop.getINSTANCE();
//            gameLoop.run(); // todo change the logic of game loop

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

    public enum SkillTreeAbility{
        ares,
        aceso,
        proteus
    }

    public static void nullifyGameInstance() {
        INSTANCE = null;
//        MainFrame.getINSTANCE().removeKeyListener(gameLoop); // todo: now the gameLoop doesnt contain KeyLister
        gameLoop =null;
    }
}
