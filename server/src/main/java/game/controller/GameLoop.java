package game.controller;

import game.model.FinalPanelModel;
import game.model.PanelManager;
import game.model.charactersModel.*;
import game.model.reflection.WaveManager;
import game.model.charactersModel.blackOrb.BlackOrb;
import server.DataBase;
import server.GameData;
import shared.model.TimedLocation;
import shared.model.dummies.DummyModel;
import shared.model.dummies.DummyPanel;

import java.awt.*;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static game.model.charactersModel.CollectibleModel.collectibleModels;
import static game.model.collision.Collidable.collidables;
import static server.DataBase.handleEndGame;
import static shared.model.TimedLocation.myPolToPolygon;

public class GameLoop implements Runnable {
    private String gameID;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicBoolean exit = new AtomicBoolean(false);
    private final AtomicBoolean paused = new AtomicBoolean(false);


    public static boolean movementInProgress = false;
    private WaveManager waveManager;
    private Thread waveManagerThread;
    private Thread pauseThread;


    public GameLoop(String gameID, int numberOfWaves) {
        this.gameID = gameID;

        movementInProgress = false;
        this.waveManager = new WaveManager(gameID, numberOfWaves);
        this.waveManagerThread = new Thread(waveManager); // Initialize the WaveManager thread
        this.start();
    }

    public void start() {
        if (running.get()) return;
        running.set(true);
        new Thread(this).start();
        waveManagerThread.start(); // Start the WaveManager thread
    }

    public void stop() {
        if (!running.get()) return;
        running.set(false);
        exit.set(true);
    }

    public void pauseGame(String macAddress) {
        pauseThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true){
                        Thread.sleep(1000);
                        int x = DataBase.getDataBase().findPlayer("1").incrementInMenuTime();
                        if (x > 45) resumeGame();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        pauseThread.start();
        waveManager.pause();
        paused.set(true);
    }

    public void resumeGame() {
        waveManager.resume();
        pauseThread.interrupt();
        paused.set(false);
    }

    public boolean isPaused() {
        return paused.get();
    }

    public void updateModel() {
        if (paused.get()) return;
        if (findGame(gameID).isEnded()) {
            handleEndGame(findGame(gameID).getGameType(), findGame(gameID));
            stop();
        }

        List<FinalPanelModel> finalPanelModels = findGame(gameID).finalPanelModels;

        for (int i = 0; i < finalPanelModels.size(); i++) {
            finalPanelModels.get(i).panelMotion();
        }

        for (int i = 0; i < finalPanelModels.size(); i++) {
            for (int j = i + 1; j < finalPanelModels.size(); j++) {
                PanelManager.handlePanelPanelCollision(finalPanelModels.get(i), finalPanelModels.get(j));
            }
        }

        for (int i = 0; i < collidables.size(); i++) {
            for (int j = i + 1; j < collidables.size(); j++) {
                collidables.get(i).checkCollision(collidables.get(j));
            }
        }

        findGame(gameID).ELAPSED_TIME += 0.0167;

        for (int i = 0; i < BlackOrb.blackOrbs.size(); i++) {
            BlackOrb.blackOrbs.get(i).update();
        }



        List<SquarantineModel> squarantineModels = DataBase.getDataBase().findGame(gameID).squarantineModels;

        for (int i = 0; i < squarantineModels.size(); i++) {
            if (squarantineModels.get(i).isImpactInProgress()) {
                squarantineModels.get(i).getDirection().accelerateDirection(squarantineModels.get(i).impactMaxVelocity);
                if (squarantineModels.get(i).getDirection().getMagnitude() > squarantineModels.get(i).impactMaxVelocity) {
                    squarantineModels.get(i).setImpactInProgress(false);
                }
            }
        }

        List<TrigorathModel> trigorathModels = DataBase.getDataBase().findGame(gameID).trigorathModels;

        for (int i = 0; i < trigorathModels.size(); i++) {
            if (trigorathModels.get(i).isImpactInProgress()) {
                trigorathModels.get(i).getDirection().accelerateDirection(trigorathModels.get(i).impactMaxVelocity);
                if (trigorathModels.get(i).getDirection().getMagnitude() > trigorathModels.get(i).impactMaxVelocity) {
                    trigorathModels.get(i).setImpactInProgress(false);
                }
            }
        }

        for (EpsilonModel epsilon : DataBase.getDataBase().findGame(gameID).epsilons) {
            if (epsilon.isImpactInProgress()) {
                epsilon.getDirection().accelerateDirection(6);
                if (epsilon.getDirection().getMagnitude() > 4.5) {
                    epsilon.setImpactInProgress(false);
                }
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

        for (GeoShapeModel entity : DataBase.getDataBase().findGame(gameID).entities) {
            entity.update();
        }

        DataBase dataBase = DataBase.getDataBase();
        GameData gameData = dataBase.findGameData(gameID);
        gameData.clearModels();

        for (GeoShapeModel entity : findGame(gameID).entities) {
            String id = entity.getId();
            Point2D anchor = entity.getAnchor();
            Point point = new Point((int) anchor.getX(), (int) anchor.getY());

            int[] xPoints = new int[entity.myPolygon.npoints];
            int[] yPoints = new int[entity.myPolygon.npoints];
            for (int i = 0; i < entity.myPolygon.npoints; i++) {
                xPoints[i] = (int) entity.myPolygon.xpoints[i];
                yPoints[i] = (int) entity.myPolygon.ypoints[i];
            }

            int nPoints = entity.myPolygon.npoints;
            double angle = entity.getAngle();

            DummyModel model = new DummyModel(id, point, angle, xPoints, yPoints, nPoints);

            List<Polygon> polygons = new ArrayList<>();
            if (entity instanceof ArchmireModel) {
                LinkedList<TimedLocation> timedLocation = ((ArchmireModel) entity).getLocationHistory();
                for (TimedLocation location : timedLocation) {
                    polygons.add(myPolToPolygon(location.getMyPolygon()));
                }
            }

            if (entity instanceof NecropickModel) {
                boolean res = ((NecropickModel) entity).isHovering();
                model.setShowNextLoc(res);
            }

            model.setPolygons(polygons);
            gameData.addUpdatedModels(model);
        }

        for (FinalPanelModel panelModel : finalPanelModels) {
            DummyPanel panel = new DummyPanel();

            panel.setId(panelModel.getId());

            int x = (int) panelModel.getLocation().getX();
            int y = (int) panelModel.getLocation().getY();
            panel.setLocation(new Point(x, y));

            Dimension2D size = panelModel.getSize();
            Dimension dimension = new Dimension((int) size.getWidth(), (int) size.getHeight());

            panel.setDimension(dimension);
            gameData.addUpdatedPanels(panel);
        }
    }

    @Override
    public void run() {
        long initialTime = System.nanoTime();
        final double timeU = (double) 1000000000 / findGame(gameID).getProfile().UPS;
        double deltaU = 0;
        int ticks = 0;
        long timer = System.currentTimeMillis();

        while (!exit.get()) {
            long currentTime = System.nanoTime();
            deltaU += (currentTime - initialTime) / timeU;
            initialTime = currentTime;

            if (deltaU >= 1) {
                updateModel();
                ticks++;
                deltaU--;
            }

            if (System.currentTimeMillis() - timer > 1000) {
                System.out.println(String.format("UPS: %s", ticks));
                ticks = 0;
                timer += 1000;
            }
        }
    }

    public boolean defeatedAllWaves(){
//        Game game = findGame(gameID);
//        List<EpsilonModel> epsilons = game.epsilons;
//        for (EpsilonModel epsilon : epsilons) {
//
//        }


        int waveIndex = waveManager.getWaveIndex();
        int numberOfWaves = waveManager.getNumberOfWaves();
        if (waveIndex > numberOfWaves) return true;

        return false;
//
//        GameType gameType = game.getGameType();


//        if (gameType == GameType.monomachia){
//            double time = game.ELAPSED_TIME;
//            if (time > 600) handleMonomachiaEndGame(game);
//
//        }



    }


    private Game findGame(String gameID) {
        return DataBase.getDataBase().findGame(gameID);
    }

    public void enemyEliminated() {
        waveManager.onEnemyEliminated();
    }

    public WaveManager getWaveManager() {
        return waveManager;
    }

    public AtomicBoolean getExit() {
        return exit;
    }
}
