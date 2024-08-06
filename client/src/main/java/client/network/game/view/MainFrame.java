package client.network.game.view;

import client.network.game.controller.MouseController;
import client.network.game.controller.constants.Constants;
import client.network.game.view.junks.AbilityShopPanel;

import javax.swing.*;
import java.awt.*;

public final class MainFrame extends JFrame {
    private static MainFrame INSTANCE;
    public static JLabel label;

    public JFrame abilityShopFrame = null;

    private MainFrame() {
        INSTANCE = this;
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setSize(Constants.FRAME_DIMENSION);
        setAlwaysOnTop(true);
        setLocationRelativeTo(null);
        setVisible(true);
        setLayout(null);


    }

    public void handleAbilityShopPanelToggle() {
        if (abilityShopFrame == null) {
            abilityShopFrame = new JFrame("Ability Shop");
            abilityShopFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            abilityShopFrame.setSize(400, 600);
            abilityShopFrame.setLocationRelativeTo(null); // Center the frame on the screen
            abilityShopFrame.setAlwaysOnTop(true); // Ensure the frame is always on top
            AbilityShopPanel abilityShopPanel = new AbilityShopPanel(abilityShopFrame);
            abilityShopFrame.add(abilityShopPanel);
            abilityShopFrame.setVisible(true);
            abilityShopFrame.toFront(); // Bring the frame to the front
            abilityShopFrame.requestFocus(); // Request focus
        } else {
            abilityShopFrame.dispose();
            abilityShopFrame = null;
//            abilityShopPanel = null;
        }
    }

    public static MainFrame getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new MainFrame();
        return INSTANCE;
    }
}
