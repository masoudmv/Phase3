package controller;

import model.charactersModel.EpsilonModel;
import model.movement.Direction;
import view.MainFrame;
import view.junks.AbilityShopPanel;
import view.junks.KeyBindingMenu;


import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static controller.GameLoop.movementInProgress;
import static controller.Utils.addVectors;
import static controller.Utils.multiplyVector;
import static controller.constants.Constants.EPSILON_MAX_SPEED;
import static org.example.Main.sensitivity;

public class UserInputHandler implements KeyListener {
    private static UserInputHandler INSTANCE;
    public static Set<Integer> keysPressed = new HashSet<>();
    private Timer movementTimer;

    public static AbilityShopPanel abilityShopPanel = null;
    public static JFrame abilityShopFrame = null;

    public UserInputHandler() {



    }

    public static void updateMovement() {
        double deltaX = 0;
        double deltaY = 0;
        Map<String, Integer> keyBindings = KeyBindingMenu.getINSTANCE().getKeyBindings();
        if (sensitivity < 50) EPSILON_MAX_SPEED = 3;
        if (50 <= sensitivity && sensitivity < 60) EPSILON_MAX_SPEED = 3.5;
        if (60 < sensitivity && sensitivity < 70) EPSILON_MAX_SPEED = 4;
        if (70 < sensitivity && sensitivity < 80) EPSILON_MAX_SPEED = 4.5;
        if (80 < sensitivity && sensitivity < 90) EPSILON_MAX_SPEED = 5;
        if (90 < sensitivity && sensitivity <= 100) EPSILON_MAX_SPEED = 5.5;



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

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            Game.getINSTANCE().getGameLoop().pauseGame();
            handleAbilityShopPanelToggle();
        }

        if (GameLoop.getINSTANCE().isRunning()) {
            if (e.getKeyCode() == KeyEvent.VK_G) UserInterfaceController.fireAbility();
        }

        if (GameLoop.getINSTANCE().isRunning()) {
            if (e.getKeyCode() == KeyEvent.VK_R) UserInterfaceController.fireSkill();
        }

        keysPressed.add(e.getKeyCode());
        if (!movementInProgress) {
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

    public static UserInputHandler getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new UserInputHandler();
        return INSTANCE;
    }

    public void stopMovementTimer() {
        if (movementTimer != null) {
            movementTimer.stop();
            movementTimer = null;
        }
    }

    private void handleAbilityShopPanelToggle() {
        if (abilityShopFrame == null) {
            abilityShopFrame = new JFrame("Ability Shop");
            abilityShopFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            abilityShopFrame.setSize(400, 600);
            abilityShopFrame.setLocationRelativeTo(null); // Center the frame on the screen
            abilityShopFrame.setAlwaysOnTop(true); // Ensure the frame is always on top
            abilityShopPanel = new AbilityShopPanel(abilityShopFrame);
            abilityShopFrame.add(abilityShopPanel);
            abilityShopFrame.setVisible(true);
            abilityShopFrame.toFront(); // Bring the frame to the front
            abilityShopFrame.requestFocus(); // Request focus
        } else {
            abilityShopFrame.dispose();
            abilityShopFrame = null;
            abilityShopPanel = null;
        }
    }

    public JFrame getAbilityShopFrame() {
        return abilityShopFrame;
    }

    public void setAbilityShopFrame(JFrame abilityShopFrame) {
        this.abilityShopFrame = abilityShopFrame;
    }
}
