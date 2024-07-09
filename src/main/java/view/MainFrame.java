package view;

import controller.Game;

import javax.swing.*;
import java.awt.*;

import static controller.Constants.FRAME_DIMENSION;

public final class MainFrame extends JFrame {
    private static MainFrame INSTANCE;
    public static JLabel label;
    private MainFrame() {
        INSTANCE = this;
        setUndecorated(true);
        setBackground(new Color(0,0,0,0));
        setSize(FRAME_DIMENSION);
        setAlwaysOnTop(true);
        setLocationRelativeTo(null);
        setVisible(true);
        setLayout(null);
        label = new JLabel("<html>Wave: "+ Game.wave + "<br>Elapsed Time: "+ Game.elapsedTime
                + "<br> XP: "+Game.inGameXP +"<br>HP: "+ 100);
        label.setForeground(Color.red);
        label.setBounds(0,0,120,100);
        label.setBackground(Color.black);
        label.setOpaque(true);

    }

    public static MainFrame getINSTANCE() {
        if (INSTANCE==null) INSTANCE=new MainFrame();
        return INSTANCE;
    }
}