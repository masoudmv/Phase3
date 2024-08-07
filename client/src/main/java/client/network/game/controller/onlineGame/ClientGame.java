package client.network.game.controller.onlineGame;

import client.network.game.controller.constants.Constants;
import client.network.game.controller.UserInputHandler;
import client.network.game.view.MainFrame;

import javax.swing.*;
import java.awt.*;

public class ClientGame {
    private static ClientGame INSTANCE;
    private boolean isPaused = false;
    private static ClientGameLoop gameLoop;
    private int time;
    private int health;
    private int XP;
    private int wave;
    private String skill;
    private int otherHP;

    private JLabel timeLabel;
    private JLabel waveLabel;
    private JLabel healthLabel;
    private JLabel xpLabel;
    private JLabel skillLabel;
    private JLabel otherHPLabel;

    public ClientGame() {
        INSTANCE = this;
        Constants.RADIUS = 15;
        MainFrame frame = MainFrame.getINSTANCE();

        // Initialize the status panel
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new GridLayout(6, 1));
        statusPanel.setBackground(Color.black);
        statusPanel.setOpaque(true);

        // Initialize the labels
        timeLabel = new JLabel("Time: " + time);
        waveLabel = new JLabel("Wave: " + wave);
        healthLabel = new JLabel("Health: " + health);
        xpLabel = new JLabel("XP: " + XP);
        skillLabel = new JLabel("Skill: " + skill);
        otherHPLabel = new JLabel("other health: " + otherHP);

        // Set the label text color to white
        timeLabel.setForeground(Color.white);
        waveLabel.setForeground(Color.white);
        healthLabel.setForeground(Color.white);
        xpLabel.setForeground(Color.white);
        skillLabel.setForeground(Color.white);
        otherHPLabel.setForeground(Color.white);

        // Add the labels to the panel
        statusPanel.add(timeLabel);
        statusPanel.add(waveLabel);
        statusPanel.add(healthLabel);
        statusPanel.add(xpLabel);
        statusPanel.add(skillLabel);
        statusPanel.add(otherHPLabel);

        // Set the position of the status panel
        frame.setLayout(null);
        statusPanel.setBounds(10, 10, 150, 100);

        // Add the status panel to the frame
        frame.add(statusPanel);
        frame.revalidate();
        frame.repaint();

        SwingUtilities.invokeLater(() -> {
            MainFrame.getINSTANCE().addKeyListener(UserInputHandler.getINSTANCE());
            gameLoop = new ClientGameLoop();
        });
    }

    public static ClientGame getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new ClientGame();
        return INSTANCE;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public void openShop() {
    }

    public void closeShop() {
    }

    public static void nullifyGameInstance() {
        INSTANCE = null;
    }

    // Methods to update the status panel
    public void updateWave(int wave) {
        this.wave = wave;
        waveLabel.setText("Wave: " + wave);
    }

    public void updateHealth(int health) {
        this.health = health;
        healthLabel.setText("Health: " + health);
    }

    public void updateXP(int XP) {
        this.XP = XP;
        xpLabel.setText("XP: " + XP);
    }

    public void updateTime(int time) {
        this.time = time;
        timeLabel.setText("Time: " + time);
    }

    public void updateSkill(String skill) {
        this.skill = skill;
        skillLabel.setText("Skill: " + skill);
    }

    public void updateOtherHP(int otherHP) {
        this.otherHP = otherHP;
        otherHPLabel.setText("other health: " + otherHP);
    }

    public static ClientGameLoop getGameLoop() {
        return gameLoop;
    }
}
