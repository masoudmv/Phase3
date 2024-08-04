package client.network.game.view;

import client.network.game.controller.MouseController;
import client.network.game.controller.constants.Constants;

import javax.swing.*;
import java.awt.*;

public final class MainFrame extends JFrame {
    private static MainFrame INSTANCE;
    public static JLabel label;

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

    public static MainFrame getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new MainFrame();
        return INSTANCE;
    }
}
