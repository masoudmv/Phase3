package game.controller;

import game.model.charactersModel.*;
import server.DataBase;
import server.GameData;
import shared.constants.Constants;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;


public class Game {
    private String gameID;


    public List<EpsilonModel> epsilons = new CopyOnWriteArrayList<>();
    public List<GeoShapeModel> entities = new CopyOnWriteArrayList<>();
    public List<SquarantineModel> squarantineModels = new ArrayList<>();
    public List<TrigorathModel> trigorathModels =new ArrayList<>();


    private static Game INSTANCE;
    public static int inGameXP = 1000;
    public static double ELAPSED_TIME = 0;
    public static int wave;
    private boolean isPaused = false;
//    private EpsilonModel epsilon;


    private static GameLoop gameLoop;





    public Game (){
        this.gameID = "1";
        GameData gameData = new GameData(gameID);
        DataBase.getDataBase().getGameData().add(gameData);
        DataBase.getDataBase().addGame(this);





        new EpsilonModel(new Point2D.Double(1000, 700), gameID);
        new TrigorathModel(new Point2D.Double(1000, 500), gameID);

        INSTANCE = this;
        ELAPSED_TIME =0;
        inGameXP=0;
        wave=1;
        Constants.RADIUS = 15;


        new GameLoop(gameID);


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
//        MainFrame.getINSTANCE().removeKeyListener(gameLoop);
// todo: now the gameLoop doesnt contain KeyLister
        gameLoop =null;
    }


    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }
}
