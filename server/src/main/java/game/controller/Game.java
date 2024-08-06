package game.controller;

import game.model.DoubleDimension2D;
import game.model.FinalPanelModel;
import game.model.charactersModel.*;
import game.model.charactersModel.blackOrb.BlackOrb;
import game.model.entities.Ability;
import game.model.entities.Profile;
import server.DataBase;
import server.GameData;
import shared.constants.Constants;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
;
import java.util.concurrent.CopyOnWriteArrayList;

import static shared.constants.Constants.FRAME_DIMENSION;


public class Game {
    private String gameID;

    public List<EpsilonModel> epsilons = new CopyOnWriteArrayList<>();
    public List<GeoShapeModel> entities = new CopyOnWriteArrayList<>();
    public List<SquarantineModel> squarantineModels = new ArrayList<>();
    public List<TrigorathModel> trigorathModels = new ArrayList<>();


    public double ELAPSED_TIME = 0;
    public int wave;
    private boolean isPaused = false;

    private GameType gameType;
    private GameLoop gameLoop;
    private long pausedTime = 0;

    private Profile profile;

    public Game(GameType gameType) {
        this.gameType = gameType;
        this.gameID = "1";
        profile = new Profile();
        GameData gameData = new GameData(gameID);
        DataBase.getDataBase().getGameData().add(gameData);
        DataBase.getDataBase().addGame(this);

        ELAPSED_TIME = 0;


        Constants.RADIUS = 15;

        gameLoop = new GameLoop(gameID);



    }

    public void addEpsilons(String macAddress1, String macAddress2, GameType gameType) {
        Dimension dimension = FRAME_DIMENSION;
        double width = dimension.getWidth();
        double height = dimension.getHeight();
        double epsilonDis = 50;

        Point2D loc = new Point2D.Double(width / 2 - 250, height / 2 - 250);
        DoubleDimension2D size = new DoubleDimension2D(500, 500);

        FinalPanelModel localPanel = new FinalPanelModel(loc, size, gameID);
        localPanel.setIsometric(false);

        switch (gameType) {
            case colosseum -> {
                Point2D pos1 = new Point2D.Double(width / 2 + epsilonDis, height / 2);
                EpsilonModel epsilon1 = new EpsilonModel(pos1, true, gameID, Color.green);
                epsilon1.setMacAddress(macAddress1);

                Point2D pos2 = new Point2D.Double(width / 2 - epsilonDis, height / 2);
                EpsilonModel epsilon2 = new EpsilonModel(pos2, true, gameID, Color.yellow);
                epsilon2.setMacAddress(macAddress2);
            }
            case monomachia -> {
                Point2D pos1 = new Point2D.Double(width / 2 + epsilonDis, height / 2);
                EpsilonModel epsilon1 = new EpsilonModel(pos1, true, gameID, Color.green);
                epsilon1.setMacAddress(macAddress1);

                Point2D pos2 = new Point2D.Double(width / 2 - epsilonDis, height / 2);
                EpsilonModel epsilon2 = new EpsilonModel(pos2, false, gameID, Color.yellow);
                epsilon2.setMacAddress(macAddress2);
            }
        }

        profile.activeAbilities.put("1", Ability.EMPOWER);
        BlackOrb blackOrb = new BlackOrb();
        blackOrb.create(gameID);
       TrigorathModel t = new TrigorathModel();
       t.create(gameID);
       t.create(gameID);
       t.create(gameID);
       t.create(gameID);

        BarricadosModel b = new BarricadosModel();
        b.create(gameID);
//        b.create(gameID);
//        b.create(gameID);
//        b.create(gameID);
//        b.create(gameID);
//        b.create(gameID);
//        b.create(gameID);



    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
        if (paused) {
            pausedTime = System.currentTimeMillis();
            gameLoop.pauseGame();
        } else {
            gameLoop.resumeGame();
        }
    }


    public GameType getGameType() {
        return gameType;
    }


    public String getGameID() {
        return gameID;
    }


    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public long getPausedTime() {
        return pausedTime;
    }

    public void setPausedTime(long pausedTime) {
        this.pausedTime = pausedTime;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}

