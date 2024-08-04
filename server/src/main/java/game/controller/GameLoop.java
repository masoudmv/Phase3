package game.controller;

import game.controller.constants.Constants;
import game.model.FinalPanelModel;
import game.model.PanelManager;
import game.model.charactersModel.*;
import game.model.entities.Profile;
import game.view.FinalPanelView;
import game.view.MainFrame;

import game.model.charactersModel.blackOrb.BlackOrb;

import game.view.junks.GameOverPanel;

import game.view.junks.VictoryPanel;
import server.DataBase;
import shared.Model.dummies.DummyModel;
import shared.Model.dummies.DummyPanel;

import javax.swing.*;
import javax.swing.Timer;

import java.awt.*;
import java.awt.geom.Dimension2D;

import java.awt.geom.Point2D;
import java.util.concurrent.atomic.AtomicBoolean;

import static game.controller.Game.*;
import static game.controller.Sound.*;
import static game.model.FinalPanelModel.finalPanelModels;
import static game.model.charactersModel.CollectibleModel.collectibleModels;
//import static model.NonRigid.nonRigids;
import static game.model.charactersModel.GeoShapeModel.entities;
import static game.model.charactersModel.SquarantineModel.squarantineModels;
import static game.model.charactersModel.TrigorathModel.trigorathModels;
//import static model.collision.Coll.colls;
import static game.model.collision.Collidable.collidables;
import static game.view.FinalPanelView.finalPanelViews;
import static game.view.MainFrame.label;
//import static view.Panel.panels;


public class GameLoop implements Runnable {

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

    public GameLoop() {
        decreaseVelocities=false;

        movementInProgress = false;


        playThemeSound();



        ELAPSED_TIME = 0;
        inGameXP = 1000;
        wave = 1;
        MainFrame frame = MainFrame.getINSTANCE();
        frame.addMouseListener(new MouseController());
        frame.addMouseMotionListener(new MouseController());



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





    public void updateView() {


        label.setText("<html>Wave: "+ Game.wave + "<br>Elapsed Time: "+ (int) Game.ELAPSED_TIME
                + "<br> XP: "+Game.inGameXP +"<br>HP: "+ EpsilonModel.getINSTANCE().getHp());

        long currentTickTime = System.currentTimeMillis();
        long interval = currentTickTime - lastTickTime;

        lastTickTime = currentTickTime;

        ELAPSED_TIME += (double) interval / 1000;

//        if (!EpsilonModel.getINSTANCE().isImpactInProgress()) updateMovement(); // Single REsponsibility
        if (!EpsilonModel.getINSTANCE().isImpactInProgress()) UserInputHandler.updateMovement();


        // Increment frame count every time updateView is called
        frameCount++;

        // Current time in milliseconds
        long currentTime = System.currentTimeMillis();

        // Check if one second has passed
        if (currentTime - lastUpdateTime >= 1000) {
            // Reset frame counter and last update time for the next second
            frameCount = 0;
            lastUpdateTime = currentTime;
        }









//        for (SquarantineView squarantineView: squarantineViews){
//            squarantineView.setVertices(calculateViewLocationSquarantine(MainPanel.getINSTANCE(),squarantineView.getId()));
//        }
//        for (TrigorathView trigorathView : trigorathViews){
//            trigorathView.setVertices(calculateViewLocationTrigorath(MainPanel.getINSTANCE(), trigorathView.getId()));
//        }
//        for (BulletView bulletView : bulletViews){
//            bulletView.setCurrentLocation(calculateViewLocationBullet(MainPanel.getINSTANCE(), bulletView.getId()));
//        }
//        for (CollectibleView collectibleView : collectibleViews){
//            collectibleView.setCurrentLocation(
//                    calculateViewLocationCollectible(MainPanel.getINSTANCE(), collectibleView.getId())
//            );
//        }


        for (FinalPanelView f : finalPanelViews){
            f.setLocation(UserInterfaceController.calculateLocationOfFinalPanelView(f.getId()));
            f.setSize(UserInterfaceController.calculateDimensionOfFinalPanelView(f.getId()));
        }





        UserInterfaceController.updateGeoShapeViewProperties();
        SwingUtilities.invokeLater(MainFrame.getINSTANCE()::repaint);
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



        if (EpsilonModel.getINSTANCE().getHp() <= 0) {
//            MainFrame.getINSTANCE().remove(MainPanel.getINSTANCE());
            MainFrame.getINSTANCE().repaint();
            MainFrame.getINSTANCE().remove(label);
//            gameLoop.stop();
            new GameOverPanel();
        }


        if (wave > 3) {
//            MainFrame.getINSTANCE().removeKeyListener(this);
//            MainFrame.getINSTANCE().removeMouseListener(MainPanel.getINSTANCE().getMouseController());
            if (theme.isRunning()) stopThemeSound();
            playVictorySound();
            Constants.RADIUS += 1;
        }
        if (Constants.RADIUS > 500) {
//            MainFrame.getINSTANCE().remove(MainPanel.getINSTANCE());
            MainFrame.getINSTANCE().repaint();
            MainFrame.getINSTANCE().remove(label);
//            gameLoop.stop();
            new VictoryPanel();

        }
        updateCount++;


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



        for (GeoShapeModel entity : entities){
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
        dataBase.clearModels();

        for (GeoShapeModel entity : entities) {
            DummyModel model = new DummyModel();
            model.setId(entity.getId());

            Point2D anchor = entity.getAnchor();
            model.setAnchor(new Point((int) anchor.getX(), (int) anchor.getY()));

            model.setMyPolygon(entity.myPolygon);
            model.setAngle(entity.getAngle());
            dataBase.addUpdatedModels(model);
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
            dataBase.addUpdatedPanels(panel);
        }


    }

    public Timer getGameLoop() {
        return gameLoop;
    }

    public void setGameLoop(Timer gameLoop) {
        this.gameLoop = gameLoop;
    }




    // deprecated!
//    private void creatEnemy(){
//        double interval = ELAPSED_TIME - lastCreatedEnemyTime;
//        if ((lastCreatedEnemyTime != -1 && interval < INTERVAL) || ELAPSED_TIME < 8) return;
//        lastCreatedEnemyTime = ELAPSED_TIME;
//        createdNumberOfEnemies++;
//        aliveEnemies++;
//        MainPanel panel = MainPanel.getINSTANCE();
//        MainFrame frame = MainFrame.getINSTANCE();
//        Point2D vertex1 = panel.getVertices()[0];
//        Point2D vertex2 = panel.getVertices()[1];
//        Point2D vertex3 = panel.getVertices()[2];
//        Point2D vertex4 = panel.getVertices()[3];
//        ArrayList<Integer> accessibles = new ArrayList<>();
//        boolean leftAccessible = (vertex1.getX()>50) && (vertex4.getX()>50);
//        boolean upAccessible = (vertex1.getY()>50) && (vertex2.getY()>50);
//        boolean rightAccessible = (frame.getWidth()-vertex2.getX()>50) && (frame.getWidth()-vertex3.getX()>50);
//        boolean downAccessible = (frame.getHeight()-vertex3.getY()>50) && (frame.getHeight()-vertex4.getY()>50);
//        if (leftAccessible) accessibles.add(0);
//        if (upAccessible) accessibles.add(1);
//        if (rightAccessible) accessibles.add(2);
//        if (downAccessible) accessibles.add(3);
//
//        Random random = new Random();
//        int index = random.nextInt(accessibles.size());
//        if (accessibles.get(index) == 0){
//            double y = random.nextDouble(vertex1.getY(), vertex4.getY());
//            int rand = random.nextInt(2);
//            Point2D anchor = new Point2D.Double(vertex1.getX()-40, y);
//            if (rand==0) new SquarantineModel(anchor);
//            if (rand==1) new TrigorathModel(anchor);
//
//        }
//        if (accessibles.get(index) == 1){
//            double x = random.nextDouble(vertex1.getX(), vertex2.getX());
//            int rand = random.nextInt(2);
//            Point2D anchor = new Point2D.Double(x, vertex2.getY()-40);
//            if (rand==0) new SquarantineModel(anchor);
//            if (rand==1) new TrigorathModel(anchor);
//
//        }
//        if (accessibles.get(index) == 2){
//            double y = random.nextDouble(vertex2.getY(), vertex3.getY());
//            int rand = random.nextInt(2);
//            Point2D anchor = new Point2D.Double(vertex3.getX()+40, y);
//            if (rand==0) new SquarantineModel(anchor);
//            if (rand==1) new TrigorathModel(anchor);
//
//        }
//        if (accessibles.get(index) == 3){
//            double x = random.nextDouble(vertex4.getX(), vertex3.getX());
//            int rand = random.nextInt(2);
//            Point2D anchor = new Point2D.Double(x, vertex4.getY()+40);
//            if (rand==0) new SquarantineModel(anchor);
//            if (rand==1) new TrigorathModel(anchor);
//
//        }
//
//    }




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
//        stop();
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

}










