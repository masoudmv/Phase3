package client.network.game.controller.input;



import client.network.game.view.junks.KeyBindingMenu;
import shared.Model.Input;


import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static client.network.RequestFactory.createMoveRequest;
import static client.network.game.controller.onlineGame.OnlineGameLoop.movementInProgress;


public class UserInputHandler implements KeyListener {
    private static UserInputHandler INSTANCE;
    public static Set<Integer> keysPressed = new HashSet<>();
    private Timer movementTimer;

//    private AbilityShopPanel abilityShopPanel = null;
    private JFrame abilityShopFrame = null;

    public UserInputHandler() {



    }

    public static void updateMovement() {

        Map<String, Integer> keyBindings = KeyBindingMenu.getINSTANCE().getKeyBindings();



        if (keysPressed.contains(keyBindings.get("Move Up"))) createMoveRequest(Input.move_up);
        if (keysPressed.contains(keyBindings.get("Move Right"))) createMoveRequest(Input.move_right);
        if (keysPressed.contains(keyBindings.get("Move Down"))) createMoveRequest(Input.move_down);
        if (keysPressed.contains(keyBindings.get("Move Left"))) createMoveRequest(Input.move_left);




    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Map<String, Integer> keyBindings = KeyBindingMenu.getINSTANCE().getKeyBindings();

//        if (e.getKeyCode() == keyBindings.get("Open Shop")) {
//            handleAbilityShopPanelToggle();
//        }

//        if (GameLoop.getINSTANCE().isRunning()) {
//            if (e.getKeyCode() == KeyEvent.VK_G) UserInterfaceController.fireAbility();
//        }

//        if (GameLoop.getINSTANCE().isRunning()) {
//            if (e.getKeyCode() == KeyEvent.VK_R) UserInterfaceController.fireSkill();
//        }

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
//            abilityShopPanel = new AbilityShopPanel(abilityShopFrame);
//            abilityShopFrame.add(abilityShopPanel);
            abilityShopFrame.setVisible(true);
            abilityShopFrame.toFront(); // Bring the frame to the front
            abilityShopFrame.requestFocus(); // Request focus
        } else {
            abilityShopFrame.dispose();
            abilityShopFrame = null;
//            abilityShopPanel = null;
        }
    }

    public JFrame getAbilityShopFrame() {
        return abilityShopFrame;
    }

    public void setAbilityShopFrame(JFrame abilityShopFrame) {
        this.abilityShopFrame = abilityShopFrame;
    }
}
