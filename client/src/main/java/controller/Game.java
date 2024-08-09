package controller;

import model.MyPolygon;
import model.charactersModel.EpsilonModel;
import model.charactersModel.NecropickModel;
import model.entities.Profile;
import org.example.GraphicalObject;
import view.MainFrame;
import javax.swing.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import static view.MainFrame.label;


public class Game {
    private static Game INSTANCE;
    public static int inGameXP = 0;
    public static double elapsedTime = 0;
    public static int wave;
    private GameLoop gameLoop;

    public Game (){
        INSTANCE = this;
        elapsedTime = 0;
        Profile.getCurrent().inGameXP = 0;
        Profile.getCurrent().totalBullets = 0;

        BufferedImage b = EpsilonModel.loadImage();
        GraphicalObject bows = new GraphicalObject(b);
        MyPolygon pol = bows.getMyBoundingPolygon();
        new EpsilonModel(new Point2D.Double(1000, 700), pol);


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

    public static void nullifyGameInstance() {
        INSTANCE = null;
    }

    public GameLoop getGameLoop() {
        return gameLoop;
    }

    public void incrementDeadEnemies(){
        gameLoop.getWaveManager().onEnemyEliminated();
    }
}
