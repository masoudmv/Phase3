package game.controller;

import game.model.DoubleDimension2D;
import game.model.FinalPanelModel;
import game.model.charactersModel.*;
import game.model.entities.Ability;
import game.model.entities.Profile;
import game.model.entities.Skill;
import server.DataBase;
import server.GameData;
import shared.constants.Constants;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static shared.constants.Constants.FRAME_DIMENSION;

public class Game {
    private String gameID;

    public List<EpsilonModel> epsilons = new CopyOnWriteArrayList<>();
    public List<GeoShapeModel> entities = new CopyOnWriteArrayList<>();
    public List<SquarantineModel> squarantineModels = new ArrayList<>();
    public List<TrigorathModel> trigorathModels = new ArrayList<>();
    public List<FinalPanelModel> finalPanelModels = new ArrayList<>();
    public List<EpsilonModel> deadEpsilons = new ArrayList<>();

    public double ELAPSED_TIME = 0;
    private boolean isPaused = false;

    private GameType gameType;
    private GameLoop gameLoop;



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

        addEpsilons("1", "2", "3");

        profile.activatedSkills.put("1", Skill.CERBERUS);

        gameLoop = new GameLoop(gameID, 1); // Initialize GameLoop with number of waves
    }


    public void addEpsilons(String macAddress1, String macAddress2, String macAddress3) {
        addEpsilonModels(new String[]{macAddress1, macAddress2, macAddress3},
                new boolean[]{true, false, true},
                new Point2D[]{
                        new Point2D.Double(FRAME_DIMENSION.getWidth() / 2 + 50, FRAME_DIMENSION.getHeight() / 2 - 50),
                        new Point2D.Double(FRAME_DIMENSION.getWidth() / 2 - 50, FRAME_DIMENSION.getHeight() / 2),
                        new Point2D.Double(FRAME_DIMENSION.getWidth() / 2 - 50, FRAME_DIMENSION.getHeight() / 2 + 50)
                });
    }

    public void addEpsilons(String macAddress1, String macAddress2, GameType gameType) {
        boolean isMonomachia = (gameType == GameType.monomachia);
        addEpsilonModels(new String[]{macAddress1, macAddress2},
                new boolean[]{true, isMonomachia},
                new Point2D[]{
                        new Point2D.Double(FRAME_DIMENSION.getWidth() / 2 + 50, FRAME_DIMENSION.getHeight() / 2),
                        new Point2D.Double(FRAME_DIMENSION.getWidth() / 2 - 50, FRAME_DIMENSION.getHeight() / 2)
                });
    }

    private void addEpsilonModels(String[] macAddresses, boolean[] teamAssignments, Point2D[] positions) {
        Dimension dimension = FRAME_DIMENSION;
        double width = dimension.getWidth();
        double height = dimension.getHeight();

        Point2D loc = new Point2D.Double(width / 2 - 250, height / 2 - 250);
        DoubleDimension2D size = new DoubleDimension2D(500, 500);

        FinalPanelModel localPanel = new FinalPanelModel(loc, size, gameID);
        localPanel.setIsometric(false);

        for (int i = 0; i < macAddresses.length; i++) {
            EpsilonModel epsilon = new EpsilonModel(positions[i], teamAssignments[i], gameID);
            epsilon.setMacAddress(macAddresses[i]);
        }
    }



    public boolean isPaused() {
        return gameLoop.isPaused();
    }

    public void setPaused(boolean paused, String macAddress) {
        if (paused) {
            gameLoop.pauseGame(macAddress);
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


    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    // Notify GameLoop when an enemy is eliminated
    public void enemyEliminated() {
        gameLoop.enemyEliminated();
    }

    public int getWave(){
        return gameLoop.getWaveManager().getWaveIndex();
    }

    public AtomicBoolean getExit(){
        return gameLoop.getExit();
    }

    public boolean isEnded(){
        if (GameType.monomachia == gameType) {
            if (ELAPSED_TIME > 60 * 5) return true;
            int b = 0;
            int g = 0;
            for (EpsilonModel e : epsilons){
                if (e.isBlackTeam()) b++;
                else g++;
            }

            if (b == 0 || g == 0) return true;
        }

        else {
            if (deadEpsilons.size() == 1) return true;
            if (gameLoop.defeatedAllWaves()) return true;
        }

        return false;
    }


}
