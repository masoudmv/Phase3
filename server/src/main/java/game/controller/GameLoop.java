package game.controller;

import game.model.FinalPanelModel;
import game.model.PanelManager;
import game.model.charactersModel.*;
import game.model.entities.Profile;
import game.model.charactersModel.blackOrb.BlackOrb;
import javafx.scene.SpotLight;
import server.DataBase;
import server.GameData;
import shared.Model.TimedLocation;
import shared.Model.dummies.DummyModel;
import shared.Model.dummies.DummyPanel;

import javax.swing.Timer;

import java.awt.*;
import java.awt.geom.Dimension2D;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static game.controller.Game.*;

import static game.model.FinalPanelModel.finalPanelModels;
import static game.model.charactersModel.CollectibleModel.collectibleModels;
import static game.model.collision.Collidable.collidables;
import static shared.Model.TimedLocation.myPolToPolygon;

public class GameLoop implements Runnable {

    private String gameID;


    public static GameLoop INSTANCE;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicBoolean exit = new AtomicBoolean(false);

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

    public GameLoop(String gameID) {
        this.gameID = gameID;
        decreaseVelocities=false;

        movementInProgress = false;


        ELAPSED_TIME = 0;
        inGameXP = 1000;
        wave = 1;


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






    public void updateModel() {



        for (int i = 0; i < finalPanelModels.size(); i++) {
            finalPanelModels.get(i).panelMotion();  // todo
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



//        MainPanel panel = MainPanel.getINSTANCE();
//        if (ELAPSED_TIME < 2) panel.verticalShrink(2);
//        if (ELAPSED_TIME < 2) panel.horizontalShrink(2);
        if (ELAPSED_TIME > 2 && ELAPSED_TIME < 10) {
//            panel.expansion();
        }

        ELAPSED_TIME += 0.0167;


//        for (Movable movable : movables) {
//            movable.move();
////            movable.friction();
//        }
        for (int i = 0; i < BlackOrb.blackOrbs.size(); i++) {
            BlackOrb.blackOrbs.get(i).update();
        }
//        for (Fist f : fists){
//            f.update();
//        }
//        for (Smiley smiley : Smiley.smilies){
//            smiley.update();
//        }
//        for (Hand h : hands){
////            h.rot();
//            h.update();
////            h.mySlapAttack();
////            h.rotateTowardsTarget();
////            if (ELAPSED_TIME > 3) h.rot();
//        }
//        for (SmileyBullet b : smileyBullets){
//            b.update();
//        }
//        for (OmenoctModel omenoctModel : OmenoctModel.omenoctModels) {
//            omenoctModel.setOnEpsilonPanel(EpsilonModel.getINSTANCE().getLocalPanel());
//            omenoctModel.updateDirection();
//        }
//        for (NecropickModel n : necropickModels) {   // todo revert
//            n.update();
//        }
//        for (ArchmireModel archmireModel : ArchmireModel.archmireModels) {
//            archmireModel.update();
//        }
//        for (TrigorathModel t : trigorathModels) {
//            t.rotate();
//        }
//        for (SquarantineModel s : squarantineModels) {
//            s.rotate();
//        }
//        for (Movable movable: movables){
//            movable.update();
//        }





        updateCount++;

        List<SquarantineModel> squarantineModels = DataBase.getDataBase().findGame(gameID).squarantineModels;

        for (int i = 0; i < squarantineModels.size(); i++) {
            if (squarantineModels.get(i).isImpactInProgress()) {
                squarantineModels.get(i).getDirection().accelerateDirection(squarantineModels.get(i).impactMaxVelocity);
                if (squarantineModels.get(i).getDirection().getMagnitude() > squarantineModels.get(i).impactMaxVelocity) {
                    squarantineModels.get(i).setImpactInProgress(false);
                }
            }
            // todo edit the following code with health instead of hp ...
//            if (squarantineModels.get(i).getHp() <= 0) squarantineModels.get(i).remove();


        }

        List<TrigorathModel> trigorathModels = DataBase.getDataBase().findGame(gameID).trigorathModels;

        for (int i = 0; i < trigorathModels.size(); i++) {
            if (trigorathModels.get(i).isImpactInProgress()) {
                trigorathModels.get(i).getDirection().accelerateDirection(trigorathModels.get(i).impactMaxVelocity);
                if (trigorathModels.get(i).getDirection().getMagnitude() > trigorathModels.get(i).impactMaxVelocity) {
                    trigorathModels.get(i).setImpactInProgress(false);
                }
            }
//            if (trigorathModels.get(i).getHp() <= 0) trigorathModels.get(i).remove();
            // todo edit the following code with health instead of hp ...

        }
        EpsilonModel epsilonModel = EpsilonModel.getINSTANCE();
        if (epsilonModel.isImpactInProgress()) {
            epsilonModel.getDirection().accelerateDirection(6);
            if (epsilonModel.getDirection().getMagnitude() > 4.5) {
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



        for (GeoShapeModel entity : DataBase.getDataBase().findGame(gameID).entities){
            entity.update();
        }



        // TODO move these out of gameLoop ...




        if (acesoInProgress) {
            EpsilonModel epsilon = EpsilonModel.getINSTANCE();
            if (lastHpRegainTime == -1) {
                epsilon.sumHpWith(1);
                lastHpRegainTime = ELAPSED_TIME;
            } else if (ELAPSED_TIME - lastHpRegainTime > hpRegainRate) {
                epsilon.sumHpWith(1);
                lastHpRegainTime = ELAPSED_TIME;
            }
        }


        DataBase dataBase = DataBase.getDataBase();
        GameData gameData = dataBase.findGameData(gameID);
        gameData.clearModels();

        for (GeoShapeModel entity : DataBase.getDataBase().findGame(gameID).entities) {

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
            if (entity instanceof ArchmireModel){
                LinkedList<TimedLocation> timedLocation = ((ArchmireModel)entity).getLocationHistory();
                for (TimedLocation location : timedLocation) {
                    polygons.add(myPolToPolygon(location.getMyPolygon()));
                }
            }

            if (entity instanceof NecropickModel){
                boolean res = ((NecropickModel) entity).isHovering();
                model.setShowNextLoc(res);
            }



            model.setPolygons(polygons);
            gameData.addUpdatedModels(model);
        }

        for (FinalPanelModel panelModel : finalPanelModels){
            DummyPanel panel = new DummyPanel();

            panel.setId(panelModel.getId());

            int x = (int) panelModel.getLocation().getX();
            int y = (int) panelModel.getLocation().getY();
            panel.setLocation(new Point(x,y));

            Dimension2D size = panelModel.getSize();
            Dimension dimension = new Dimension((int) size.getWidth(), (int) size.getHeight());

            panel.setDimension(dimension);
            gameData.addUpdatedPanels(panel);
        }



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
        final double timeU = (double) 1000000000 / Profile.getCurrent().UPS;
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


//    public static GameLoop getINSTANCE() {
//        if (INSTANCE == null) INSTANCE = new GameLoop();
//        return INSTANCE;
//    }

    public boolean isRunning() {
        return running.get();
    }

    public boolean isOn() {
        return !exit.get();
    }

}










