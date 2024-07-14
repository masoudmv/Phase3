package controller;

import model.charactersModel.EpsilonModel;
import model.movement.Direction;
import view.MainFrame;
import view.junks.KeyBindingMenu;
import view.junks.ShopPanel;


import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static controller.Game.SkillTreeAbility.*;
import static controller.Game.activeAbility;
import static controller.GameLoop.movementInProgress;
import static controller.Utils.addVectors;
import static controller.Utils.multiplyVector;
import static controller.constants.Constants.EPSILON_MAX_SPEED;
import static org.example.Main.sensitivity;

public class UserInputHandler implements KeyListener {
    private static UserInputHandler INSTANCE;
    public static Set<Integer> keysPressed = new HashSet<>();
    private Timer movementTimer;
    private ShopPanel shopPanel=null;

    public UserInputHandler() {
    }

    public static void updateMovement() {
        double deltaX=0;
        double deltaY=0;
        Map<String, Integer> keyBindings = KeyBindingMenu.getINSTANCE().getKeyBindings();
        if (sensitivity<50) EPSILON_MAX_SPEED=3;
        if (50 <= sensitivity && sensitivity<60) EPSILON_MAX_SPEED=3.5;
        if (60 < sensitivity && sensitivity<70) EPSILON_MAX_SPEED=4;
        if (70<sensitivity && sensitivity<80) EPSILON_MAX_SPEED=4.5;
        if (80<sensitivity && sensitivity<90) EPSILON_MAX_SPEED=5;
        if (90<sensitivity && sensitivity<=100) EPSILON_MAX_SPEED=5.5;
        if (keysPressed.contains(keyBindings.get("Move Right"))) deltaX += 0.7;
        if (keysPressed.contains(keyBindings.get("Move Left"))) deltaX -= 0.7;
        if (keysPressed.contains(keyBindings.get("Move Up"))) deltaY -= 0.7;
        if (keysPressed.contains(keyBindings.get("Move Down"))) deltaY += 0.7;
        Point2D.Double vector = new Point2D.Double(deltaX, deltaY);
        Point2D point = multiplyVector(EpsilonModel.getINSTANCE().getDirection().getNormalizedDirectionVector(),
                EpsilonModel.getINSTANCE().getDirection().getMagnitude());
        Direction direction = new Direction(addVectors(point, vector));
        direction.adjustEpsilonDirectionMagnitude();
        EpsilonModel.getINSTANCE().setDirection(direction);

    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        Map<String, Integer> keyBindings = KeyBindingMenu.getINSTANCE().getKeyBindings();

        if (e.getKeyCode() == keyBindings.get("Open Shop")){
            System.out.println(GameLoop.getINSTANCE().isRunning());

            if (GameLoop.getINSTANCE().isRunning()) {
                shopPanel = new ShopPanel();
//                MainFrame.getINSTANCE().removeMouseListener(MainPanel.getINSTANCE().getMouseController());
                MainFrame.getINSTANCE().repaint();
                MainFrame.getINSTANCE().addMouseListener(shopPanel);
                GameLoop.getINSTANCE().stop();
            }
            else {
//                MainFrame.getINSTANCE().addMouseListener(MainPanel.getINSTANCE().getMouseController());
                MainFrame.getINSTANCE().remove(shopPanel);
                MainFrame.getINSTANCE().removeMouseListener(shopPanel);
                GameLoop.getINSTANCE().start();
            }

        }

        if (GameLoop.getINSTANCE().isRunning()) {
            if (e.getKeyCode() == keyBindings.get("Activate Skill Tree Ability")){
//                if (activeAbility == ares) ares();
//                if (activeAbility == aceso) aceso();
//                if (activeAbility == proteus)  proteus();
            }
        }

        if (GameLoop.getINSTANCE().isRunning()) {
            if (e.getKeyCode() == keyBindings.get("Activate Shop Ability")){
//                ShopAbility ability = Game.shopAbility;
//                if (shopAbility == GameLoop.ShopAbility.heal) {
//                    heal();
//                } else if (shopAbility == GameLoop.ShopAbility.empower){
//                    empower();
//                } else if (shopAbility == GameLoop.ShopAbility.banish){
//                    banish();
//                }

            }
        }
        keysPressed.add(e.getKeyCode());
        if (!movementInProgress) {
//            startMovementTimer();
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

    public static UserInputHandler getINSTANCE(){
        if (INSTANCE == null) INSTANCE = new UserInputHandler();
        return INSTANCE;
    }

    public void stopMovementTimer() {
        if (movementTimer != null) {
            movementTimer.stop();
            movementTimer = null;

        }
    }
}
