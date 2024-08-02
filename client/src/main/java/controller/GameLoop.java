package controller;

import model.charactersModel.BulletModel;
import model.charactersModel.CollectibleModel;
import model.charactersModel.*;
import model.charactersModel.blackOrb.BlackOrb;
import model.charactersModel.smiley.Fist;
import model.charactersModel.smiley.Hand;
import model.charactersModel.SmileyBullet;
import model.charactersModel.smiley.Smiley;
import model.entities.Profile;
import model.movement.Movable;
import view.*;
import view.junks.GameOverPanel;
import view.junks.ShopPanel;
import view.junks.VictoryPanel;

import javax.swing.*;
import javax.swing.Timer;

import java.util.concurrent.atomic.AtomicBoolean;

import static controller.constants.Constants.*;
import static controller.UserInterfaceController.*;
import static controller.Game.*;
import static controller.MouseController.*;
import static controller.Sound.*;
import static model.FinalPanelModel.finalPanelModels;
import static model.PanelManager.handlePanelPanelCollision;
import static model.charactersModel.CollectibleModel.collectibleModels;
//import static model.NonRigid.nonRigids;
import static model.charactersModel.NecropickModel.necropickModels;
import static model.charactersModel.SquarantineModel.squarantineModels;
import static model.charactersModel.TrigorathModel.trigorathModels;
import static model.charactersModel.smiley.Fist.fists;
import static model.charactersModel.smiley.Hand.hands;
import static model.charactersModel.SmileyBullet.smileyBullets;
//import static model.collision.Coll.colls;
import static model.collision.Collidable.collidables;
import static model.movement.Movable.movables;
import static view.FinalPanelView.finalPanelViews;
import static view.MainFrame.label;
//import static view.Panel.panels;


public class GameLoop implements Runnable {

    public static GameLoop INSTANCE;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicBoolean exit = new AtomicBoolean(false);
    private long updateTimeDiffCapture = 0;
    private long frameTimeDiffCapture = 0;
    private long currentTime;
    private long lastFrameTime;
    private long lastUpdateTime;
    private long timeSaveDiffCapture = 0;
    private long timeSave;
    private volatile String FPS_UPS = "";
    private long lastTickTime = System.currentTimeMillis();
    private int frameCount = 0;
    private long lastUpdateTimeUPS = System.currentTimeMillis();


    private int updateCount = 0;
//    private Set<Integer> keysPressed = new HashSet<>();
    public static boolean movementInProgress = false;
    private final int MOVEMENT_DELAY = 10; // Delay in milliseconds
    private Timer movementTimer;
    private Timer gameLoop;
    private ShopPanel shopPanel=null;
    private int extraBullet=0;
    public static int EPSILON_MELEE_DAMAGE =10;
    public static int EPSILON_RANGED_DAMAGE =5;
    double lastHpRegainTime=-1;
    private double skillAbilityActivateTime=-1;
    private double hpRegainRate = Double.MAX_VALUE;
    public static ShopAbility shopAbility=null;
    private double lastCreatedEnemyTime=-1;
    public static double lastShot = 0;
    public static boolean decreaseVelocities;
    public static double decrementRation;
    private static boolean firstLoop;
    private static int createdNumberOfEnemies;
    public static int aliveEnemies;


    private boolean acesoInProgress=false;

    public GameLoop() {
        decreaseVelocities=false;
        decrementRation=1;
        lastShot = 0;
        shopAbility=null;
        movementInProgress = false;
        firstLoop= true;
        createdNumberOfEnemies=0;
        aliveEnemies=0;
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
            f.setLocation(calculateLocationOfFinalPanelView(f.getId()));
            f.setSize(calculateDimensionOfFinalPanelView(f.getId()));
        }





        updateGeoShapeViewProperties();
        SwingUtilities.invokeLater(MainFrame.getINSTANCE()::repaint);
    }

    public void updateModel() {


        for (int i = 0; i < finalPanelModels.size(); i++) {
            finalPanelModels.get(i).panelMotion();  // todo
        }



        for (int i = 0; i < finalPanelModels.size(); i++) {
            for (int j = i + 1; j < finalPanelModels.size(); j++) {
                handlePanelPanelCollision(finalPanelModels.get(i), finalPanelModels.get(j));
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

        for (Fist f : fists){
            f.update();
        }

        for (Smiley smiley : Smiley.smilies){
            smiley.update();
        }


        for (Hand h : hands){
//            h.rot();
            h.update();
//            h.mySlapAttack();
//            h.rotateTowardsTarget();
//            if (ELAPSED_TIME > 3) h.rot();
        }

        for (SmileyBullet b : smileyBullets){
            b.update();
        }



        for (OmenoctModel omenoctModel : OmenoctModel.omenoctModels) {
            omenoctModel.setOnEpsilonPanel(EpsilonModel.getINSTANCE().getLocalPanel());
            omenoctModel.updateDirection();
        }


        for (NecropickModel n : necropickModels) {   // todo revert
            n.update();
        }


        for (ArchmireModel archmireModel : ArchmireModel.archmireModels) {
            archmireModel.update();
        }

        for (TrigorathModel t : trigorathModels) {
            t.rotate();
        }
        for (SquarantineModel s : squarantineModels) {
            s.rotate();
        }



        for (Movable movable: movables){
            movable.update();
            movable.friction();
        }



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
            RADIUS += 1;
        }
        if (RADIUS > 500) {
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



        // TODO move these out of gameLoop ...

        if (ELAPSED_TIME > empowerEndTime) {
            empowerIsOn = false;
            extraBullet = 0;
            tripleShot = false;
        }
        if (empowerIsOn && tripleShot && mousePosition != null && extraBullet < 2 && lastShot > empowerStartTime) {
            if (ELAPSED_TIME - lastShot > 0.05) {
                new BulletModel(EpsilonModel.getINSTANCE().getAnchor(), lastBullet.getDirection());
                extraBullet++;
                lastShot = ELAPSED_TIME;
            }

        }
        if (extraBullet == 2) {
            extraBullet = 0;
            tripleShot = false;
        }

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

    public enum ShopAbility{
        heal,
        empower,
        banish
    }
}










