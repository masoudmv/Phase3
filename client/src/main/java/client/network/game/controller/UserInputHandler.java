package client.network.game.controller;



import client.network.game.view.junks.KeyBindingMenu;
import shared.model.Input;


import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static client.network.RequestFactory.*;
import static client.network.game.controller.onlineGame.ClientGameLoop.movementInProgress;


public class UserInputHandler implements KeyListener {
    private static UserInputHandler INSTANCE;
    public static Set<Integer> keysPressed = new HashSet<>();
    private Timer movementTimer;

//    private AbilityShopPanel abilityShopPanel = null;


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

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.out.println("escape");
            createPauseRequest();
        }

//        if (GameLoop.getINSTANCE().isRunning()) {
        if (e.getKeyCode() == KeyEvent.VK_R) createActivateAbilityRequest();

        if (e.getKeyCode() == KeyEvent.VK_F) createActivateSkillRequest();
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
}




//    public JFrame getAbilityShopFrame() {
//        return abilityShopFrame;
//    }
//
//    public void setAbilityShopFrame(JFrame abilityShopFrame) {
//        this.abilityShopFrame = abilityShopFrame;
//    }
//}
