package controller;

import model.BulletModel;
import model.CollectibleModel;
import model.charactersModel.EpsilonModel;
import model.charactersModel.SquarantineModel;
import model.charactersModel.TrigorathModel;
import model.collision.Impactable;
import model.movement.Direction;
import model.movement.Movable;
import view.*;
import view.charactersView.SquarantineView;
import view.charactersView.TrigorathView;
import view.charactersView.EpsilonView;

import javax.swing.*;
import javax.swing.Timer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;
import java.util.*;

import static controller.Constants.*;
import static controller.Controller.*;
import static controller.Game.*;
import static controller.Game.SkillTreeAbility.*;
import static controller.MouseController.*;
import static controller.Sound.*;
import static controller.Utils.*;
import static model.CollectibleModel.collectibleModels;
import static model.charactersModel.SquarantineModel.squarantineModels;
import static model.charactersModel.TrigorathModel.trigorathModels;
import static model.collision.Collidable.collidables;
import static model.collision.Impactable.impactables;
import static model.movement.Movable.movables;
import static org.example.Main.sensitivity;
import static view.BulletView.bulletViews;
import static view.CollectibleView.collectibleViews;
import static view.charactersView.SquarantineView.squarantineViews;
import static view.charactersView.TrigorathView.trigorathViews;

public class Update implements KeyListener {
    private long lastTickTime = System.currentTimeMillis();

    // Add FPS tracking variables
    private long lastUpdateTime = System.currentTimeMillis();
    private int frameCount = 0;
    private long lastUpdateTimeUPS = System.currentTimeMillis();
    private int updateCount = 0;
    private Set<Integer> keysPressed = new HashSet<>();
    public static boolean movementInProgress = false;
    private final int MOVEMENT_DELAY = 10; // Delay in milliseconds
    private Timer movementTimer;
    private Timer gameLoop;
    private ShopPanel shopPanel=null;
    private int extraBullet=0;
    private boolean acesoInProgress=false;
    public static int epsilonMeleeDamage=10;
    public static int epsilonRangedDamage=5;
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



    public Update() {
        decreaseVelocities=false;
        decrementRation=1;
        lastShot = 0;
        shopAbility=null;
        epsilonMeleeDamage=10;
        epsilonRangedDamage=5;
        movementInProgress = false;
        firstLoop= true;
        createdNumberOfEnemies=0;
        aliveEnemies=0;
        playThemeSound();


        MainFrame.getINSTANCE().addKeyListener(this);


        int delay = 10; //milliseconds
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                //...Perform a task...
                try {
                    updateView();
                    updateModel();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        };

        gameLoop = new Timer(delay, taskPerformer);
        gameLoop.start();

    }


    private void startMovementTimer() {}

    private void stopMovementTimer() {
        if (movementTimer != null) {
            movementTimer.stop();
            movementTimer = null;

        }
    }

    private void updateMovement() {

        double deltaX=0;
        double deltaY=0;

        Map<String, Integer> keyBindings = KeyBindingMenu.getINSTANCE().getKeyBindings();
        double ratio=1;

        if (sensitivity<50) EPSILON_MAX_SPEED=3;
        if (50<=sensitivity && sensitivity<60) EPSILON_MAX_SPEED=3.5;
        if (60<sensitivity && sensitivity<70) EPSILON_MAX_SPEED=4;
        if (70<sensitivity && sensitivity<80) EPSILON_MAX_SPEED=4.5;
        if (80<sensitivity && sensitivity<90) EPSILON_MAX_SPEED=5;
        if (90<sensitivity && sensitivity<=100) EPSILON_MAX_SPEED=5.5;




        if (keysPressed.contains(keyBindings.get("Move Right"))) {
            deltaX += 0.7;
        }
        if (keysPressed.contains(keyBindings.get("Move Left"))) {
            deltaX -= 0.7;
        }
        if (keysPressed.contains(keyBindings.get("Move Up"))) {
            deltaY -= 0.7;
        }
        if (keysPressed.contains(keyBindings.get("Move Down"))) {
            deltaY += 0.7;
        }


        Point2D.Double vector = new Point2D.Double(deltaX, deltaY);
        Point2D point = multiplyVector(EpsilonModel.getINSTANCE().getDirection().getNormalizedDirectionVector(),
                EpsilonModel.getINSTANCE().getDirection().getMagnitude());
        Direction direction = new Direction(addVectors(point, vector));
        direction.adjustEpsilonDirectionMagnitude();
        EpsilonModel.getINSTANCE().setDirection(direction);
    }

    public void updateView() throws InterruptedException {

        MainFrame.label.setText("<html>Wave: "+ Game.wave + "<br>Elapsed Time: "+ (int) Game.elapsedTime
                + "<br> XP: "+Game.inGameXP +"<br>HP: "+ EpsilonModel.getINSTANCE().getHp());



        long currentTickTime = System.currentTimeMillis();
        long interval = currentTickTime - lastTickTime;
//        System.out.println(interval);
//        double wait = 0;
        if (interval < 15) {
//            wait = 15.38461538 - interval;
//            interval = (long) 15.38461538;
            decreaseVelocities=true;
//            System.out.println(decrementRation);
        }
        else {
            decreaseVelocities=false;
        }



        lastTickTime = currentTickTime;

        elapsedTime += (double) interval / 1000;

        if (!EpsilonModel.getINSTANCE().isImpactInProgress()) updateMovement();


        // Increment frame count every time updateView is called
        frameCount++;

        // Current time in milliseconds
        long currentTime = System.currentTimeMillis();

        // Check if one second has passed
        if (currentTime - lastUpdateTime >= 1000) {
            // Print the FPS (which is frameCount since it's been a second)
//            System.out.println("FPS: " + frameCount);

            // Reset frame counter and last update time for the next second
            frameCount = 0;
            lastUpdateTime = currentTime;
        }

        for (EpsilonView epsilonView: EpsilonView.epsilonViews){
            epsilonView.setCurrentLocation(
                    calculateViewLocationEpsilon(MainPanel.getINSTANCE(),epsilonView.getId())
            );
            epsilonView.setVertices(
                    calculateViewLocationOfEpsilonVertices(MainPanel.getINSTANCE(), epsilonView.getId())
            );
        }
        for (SquarantineView squarantineView: squarantineViews){
            squarantineView.setVertices(calculateViewLocationSquarantine(MainPanel.getINSTANCE(),squarantineView.getId()));
        }
        for (TrigorathView trigorathView : trigorathViews){
            trigorathView.setVertices(calculateViewLocationTrigorath(MainPanel.getINSTANCE(), trigorathView.getId()));
        }
        for (BulletView bulletView : bulletViews){
            bulletView.setCurrentLocation(calculateViewLocationBullet(MainPanel.getINSTANCE(), bulletView.getId()));
        }
        for (CollectibleView collectibleView : collectibleViews){
            collectibleView.setCurrentLocation(calculateViewLocationCollectible(MainPanel.getINSTANCE(), collectibleView.getId()));
        }


        MainFrame.getINSTANCE().repaint();
    }

    public void updateModel(){


        if (createdNumberOfEnemies<5*wave+5 && wave<4) creatEnemy();
        if (createdNumberOfEnemies==5*wave+5 && aliveEnemies==0 && wave<4){
            createdNumberOfEnemies=0;
            wave++;
        }



        for (int i = 0; i < collectibleModels.size(); i++) {
            double age = elapsedTime-collectibleModels.get(i).birthTime;
            if (age>=10) collectibleModels.get(i).remove();

        }


        if (EpsilonModel.getINSTANCE().getHp() <= 0){
            MainFrame.getINSTANCE().remove(MainPanel.getINSTANCE());
            MainFrame.getINSTANCE().repaint();
            MainFrame.getINSTANCE().remove(MainFrame.label);
            gameLoop.stop();
            new GameOverPanel();

        }

        if (wave>3){
            MainFrame.getINSTANCE().removeKeyListener(this);
            MainFrame.getINSTANCE().removeMouseListener(MainPanel.getINSTANCE().getMouseController());
            if (theme.isRunning()) stopThemeSound();
            playVictorySound();
            RADIUS += 1;
        }

        if (RADIUS>500){
            MainFrame.getINSTANCE().remove(MainPanel.getINSTANCE());
            MainFrame.getINSTANCE().repaint();
            MainFrame.getINSTANCE().remove(MainFrame.label);
            gameLoop.stop();
            new VictoryPanel();

        }

        updateCount++;

        for (int i = 0; i < squarantineModels.size(); i++) {
            if (squarantineModels.get(i).isImpactInProgress()){
                squarantineModels.get(i).getDirection().accelerateDirection(squarantineModels.get(i).impactMaxVelocity);
                if (squarantineModels.get(i).getDirection().getMagnitude() > squarantineModels.get(i).impactMaxVelocity){
                    squarantineModels.get(i).setImpactInProgress(false);
                }
            }
            if (squarantineModels.get(i).getHp() <= 0) squarantineModels.get(i).remove();

        }

        for (int i = 0; i < trigorathModels.size(); i++) {
            if (trigorathModels.get(i).isImpactInProgress()){
                trigorathModels.get(i).getDirection().accelerateDirection(trigorathModels.get(i).impactMaxVelocity);
                if (trigorathModels.get(i).getDirection().getMagnitude() > trigorathModels.get(i).impactMaxVelocity) {
                    trigorathModels.get(i).setImpactInProgress(false);
                }
            }
            if (trigorathModels.get(i).getHp() <= 0 ) trigorathModels.get(i).remove();

        }

        EpsilonModel epsilonModel = EpsilonModel.getINSTANCE();
        if (epsilonModel.isImpactInProgress()){
            epsilonModel.getDirection().accelerateDirection(6);
            if (epsilonModel.getDirection().getMagnitude() > 4){
                epsilonModel.setImpactInProgress(false);
            }
        }

        for (CollectibleModel collectibleModel: collectibleModels){

            if (collectibleModel.impactInProgress){
                collectibleModel.getDirection().accelerateDirection(collectibleModel.impactMaxVel);
                if (collectibleModel.getDirection().getMagnitude() > collectibleModel.impactMaxVel){
                    collectibleModel.impactInProgress = false;
                }
            }

            collectibleModel.friction();

            collectibleModel.move();
        }


        // Current time in milliseconds
        long currentTime = System.currentTimeMillis();

        // Check if one second has passed
        if (currentTime - lastUpdateTimeUPS >= 1000) {
            // Print the FPS (which is frameCount since it's been a second)
//            System.out.println("FPS: " + updateCount);

            // Reset frame counter and last update time for the next second
            updateCount = 0;
            lastUpdateTimeUPS = currentTime;
        }
        for (Movable movable: movables){
            movable.move();
            movable.friction();
        }
        for (TrigorathModel t:trigorathModels){
            t.rotate();
        }
        for (SquarantineModel s:squarantineModels){
            s.rotate();
        }
        for (int i=0;i<collidables.size();i++){
            for (int j=i+1;j<collidables.size();j++){
                collidables.get(i).collides(collidables.get(j));
            }
        }




        if (elapsedTime > empowerEndTime){
            empowerIsOn=false;
            extraBullet =0;
            tripleShot=false;
        }
        if (empowerIsOn && tripleShot && mousePosition!=null && extraBullet<2 && lastShot > empowerStartTime){
            if (elapsedTime-lastShot>0.05){
                new BulletModel(EpsilonModel.getINSTANCE().getAnchor(), lastBullet.getDirection());
                extraBullet++;
                lastShot=elapsedTime;
            }

        }
        if (extraBullet==2) {
            extraBullet =0;
            tripleShot=false;
        }

        if (acesoInProgress){
            EpsilonModel epsilon = EpsilonModel.getINSTANCE();
            if (lastHpRegainTime==-1) {
                epsilon.sumHpWith(1);
                lastHpRegainTime = elapsedTime;
            } else if (elapsedTime - lastHpRegainTime > hpRegainRate){
                epsilon.sumHpWith(1);
                lastHpRegainTime = elapsedTime;
            }
        }


        MainPanel panel =MainPanel.getINSTANCE();
        if (elapsedTime < 2) panel.verticalShrink(2);
        if (elapsedTime < 2) panel.horizontalShrink(2);
        if (elapsedTime > 2 && elapsedTime < 10) {
            panel.expansion();
        }
        if (elapsedTime > 10) panel.panelMotion();


    }


    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {

        Map<String, Integer> keyBindings = KeyBindingMenu.getINSTANCE().getKeyBindings();

        if (e.getKeyCode() == keyBindings.get("Open Shop")){

            if (gameLoop.isRunning()) {
                shopPanel = new ShopPanel();
                MainFrame.getINSTANCE().removeMouseListener(MainPanel.getINSTANCE().getMouseController());
                MainFrame.getINSTANCE().repaint();
                MainFrame.getINSTANCE().addMouseListener(shopPanel);
                gameLoop.stop();
            }
            else {
                MainFrame.getINSTANCE().addMouseListener(MainPanel.getINSTANCE().getMouseController());
                MainFrame.getINSTANCE().remove(shopPanel);
                MainFrame.getINSTANCE().removeMouseListener(shopPanel);
                gameLoop.start();
            }

        }

        if (gameLoop.isRunning()) {
            if (e.getKeyCode() == keyBindings.get("Activate Skill Tree Ability")){
                if (activeAbility == ares) ares();
                if (activeAbility == aceso) aceso();
                if (activeAbility == proteus)  proteus();
            }
        }

        if (gameLoop.isRunning()) {
            if (e.getKeyCode() == keyBindings.get("Activate Shop Ability")){
//                ShopAbility ability = Game.shopAbility;
                if (shopAbility == ShopAbility.heal) {
                    heal();
                } else if (shopAbility == ShopAbility.empower){
                    empower();
                } else if (shopAbility == ShopAbility.banish){
                    banish();
                }

            }
        }
        keysPressed.add(e.getKeyCode());
        if (!movementInProgress) {
            startMovementTimer();
            movementInProgress = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {


        keysPressed.remove(e.getKeyCode());
        if (keysPressed.isEmpty()) {
            stopMovementTimer();
            movementInProgress = false;
        }
    }

    public Timer getGameLoop() {
        return gameLoop;
    }

    public void setGameLoop(Timer gameLoop) {
        this.gameLoop = gameLoop;
    }

    public void ares(){
        if (inGameXP >= 100) {
            if (skillAbilityActivateTime == -1) {
                skillAbilityActivateTime = elapsedTime;
                epsilonMeleeDamage += 2;
                epsilonRangedDamage += 2;
                inGameXP -= 100;
            } else if (elapsedTime - skillAbilityActivateTime > 300) {
                skillAbilityActivateTime = elapsedTime;
                epsilonMeleeDamage += 2;
                epsilonRangedDamage += 2;
                inGameXP -= 100;
            }
        }

    }

    public void aceso(){
        if (inGameXP >= 100){
            if (skillAbilityActivateTime==-1) {
                skillAbilityActivateTime = elapsedTime;
                acesoInProgress = true;
                if (hpRegainRate>1) hpRegainRate=1;
                else hpRegainRate /= 2;
                inGameXP -= 100;
            }
            else if (elapsedTime-skillAbilityActivateTime>300){
                skillAbilityActivateTime = elapsedTime;
                acesoInProgress = true;
                if (hpRegainRate>1) hpRegainRate=1;
                else hpRegainRate /= 2;
                inGameXP -= 100;
            }
        }
    }

    public void proteus(){
        if (inGameXP >= 100){
            if (skillAbilityActivateTime == -1){
                skillAbilityActivateTime = elapsedTime;
                EpsilonModel.getINSTANCE().addVertex();
                inGameXP -=100;
            }
            else if (elapsedTime - skillAbilityActivateTime > 300){
                skillAbilityActivateTime = elapsedTime;
                EpsilonModel.getINSTANCE().addVertex();
                inGameXP -=100;
            }
        }
    }

    public void heal(){
        EpsilonModel.getINSTANCE().sumHpWith(10);
        shopAbility = null;
    }

    public void empower(){
            empowerStartTime = elapsedTime;
            empowerEndTime = elapsedTime + 10;
            empowerIsOn = true;
//            TODO move null maker
            shopAbility = null;
    }
    public void banish(){
        for (Impactable impactable : impactables) {
            impactable.banish();
        }
        shopAbility = null;

    }

    private void creatEnemy(){
        double interval = elapsedTime - lastCreatedEnemyTime;
        if ((lastCreatedEnemyTime != -1 && interval < INTERVAL) || elapsedTime < 8) return;
        lastCreatedEnemyTime = elapsedTime;
        createdNumberOfEnemies++;
        aliveEnemies++;
        MainPanel panel = MainPanel.getINSTANCE();
        MainFrame frame = MainFrame.getINSTANCE();
        Point2D vertex1 = panel.getVertices()[0];
        Point2D vertex2 = panel.getVertices()[1];
        Point2D vertex3 = panel.getVertices()[2];
        Point2D vertex4 = panel.getVertices()[3];
        ArrayList<Integer> accessibles = new ArrayList<>();
        boolean leftAccessible = (vertex1.getX()>50) && (vertex4.getX()>50);
        boolean upAccessible = (vertex1.getY()>50) && (vertex2.getY()>50);
        boolean rightAccessible = (frame.getWidth()-vertex2.getX()>50) && (frame.getWidth()-vertex3.getX()>50);
        boolean downAccessible = (frame.getHeight()-vertex3.getY()>50) && (frame.getHeight()-vertex4.getY()>50);
        if (leftAccessible) accessibles.add(0);
        if (upAccessible) accessibles.add(1);
        if (rightAccessible) accessibles.add(2);
        if (downAccessible) accessibles.add(3);

        Random random = new Random();
        int index = random.nextInt(accessibles.size());
        if (accessibles.get(index) == 0){
            double y = random.nextDouble(vertex1.getY(), vertex4.getY());
            int rand = random.nextInt(2);
            Point2D anchor = new Point2D.Double(vertex1.getX()-40, y);
            if (rand==0) new SquarantineModel(anchor);
            if (rand==1) new TrigorathModel(anchor);

        }
        if (accessibles.get(index) == 1){
            double x = random.nextDouble(vertex1.getX(), vertex2.getX());
            int rand = random.nextInt(2);
            Point2D anchor = new Point2D.Double(x, vertex2.getY()-40);
            if (rand==0) new SquarantineModel(anchor);
            if (rand==1) new TrigorathModel(anchor);



        }
        if (accessibles.get(index) == 2){
            double y = random.nextDouble(vertex2.getY(), vertex3.getY());
            int rand = random.nextInt(2);
            Point2D anchor = new Point2D.Double(vertex3.getX()+40, y);
            if (rand==0) new SquarantineModel(anchor);
            if (rand==1) new TrigorathModel(anchor);



        }
        if (accessibles.get(index) == 3){
            double x = random.nextDouble(vertex4.getX(), vertex3.getX());
            int rand = random.nextInt(2);
            Point2D anchor = new Point2D.Double(x, vertex4.getY()+40);
            if (rand==0) new SquarantineModel(anchor);
            if (rand==1) new TrigorathModel(anchor);

        }


    }






    public enum ShopAbility{
        heal,
        empower,
        banish
    }
}