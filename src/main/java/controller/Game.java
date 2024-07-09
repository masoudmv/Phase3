package controller;

import model.charactersModel.EpsilonModel;
import model.charactersModel.SquarantineModel;
import model.charactersModel.TrigorathModel;
import view.MainPanel;
import view.MainFrame;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import static view.MainFrame.label;


public class Game {
    private static Game INSTANCE;
    public static int inGameXP;
    public static double elapsedTime=0;
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
    private static Update update;




    public Game (){
        INSTANCE = this;
        elapsedTime=0;
        inGameXP=0;
        wave=1;
        Constants.RADIUS = 15;
        SwingUtilities.invokeLater(() -> {
            MainFrame.getINSTANCE().add(label);
            MainPanel.getINSTANCE();
            update = new Update();

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
        MainFrame.getINSTANCE().removeKeyListener(update);
        update=null;
    }
}
