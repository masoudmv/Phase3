package client.network.containers;

import client.network.RequestFactory;
import client.network.Status;
import shared.Model.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static client.network.toolBox.utils.tryConnection;

public class MainMenu extends JPanel {
    private static MainMenu INSTANCE;
    JButton startButton = new JButton("Start");
    JButton squadButton = new JButton("Squad");
    JButton settingsButton = new JButton("Settings");
    JButton tutorialButton = new JButton("Tutorial");
    JButton skillTreeButton = new JButton("SkillTree");
    JButton toggleModeButton = new JButton("Toggle Mode");
    JButton exitButton = new JButton("Exit");
    JLabel statusLabel = new JLabel();
    Point start;
    Point squad;
    Point settings;
    Point tutorial;
    Point skillTree;
    Point toggleMode;
    Point exit;
    private int buttonHeight = 30;
    private int buttonWidth = 120;
    private volatile boolean isOnline;

    private MainMenu() {
        Dimension dimension = new Dimension(600, 600);
        setPreferredSize(dimension);
        setVisible(true);

        int buttonX = dimension.width / 2 - buttonWidth / 2;
        start = new Point(buttonX, 150);
        squad = new Point(buttonX, 200);
        settings = new Point(buttonX, 250);
        tutorial = new Point(buttonX, 300);
        skillTree = new Point(buttonX, 350);
        toggleMode = new Point(buttonX, 400);
        exit = new Point(buttonX, 450);

        startButton.setBounds(start.x, start.y, buttonWidth, buttonHeight);
        squadButton.setBounds(squad.x, squad.y, buttonWidth, buttonHeight);
        settingsButton.setBounds(settings.x, settings.y, buttonWidth, buttonHeight);
        tutorialButton.setBounds(tutorial.x, tutorial.y, buttonWidth, buttonHeight);
        skillTreeButton.setBounds(skillTree.x, skillTree.y, buttonWidth, buttonHeight);
        toggleModeButton.setBounds(toggleMode.x, toggleMode.y, buttonWidth, buttonHeight);
        exitButton.setBounds(exit.x, exit.y, buttonWidth, buttonHeight);
        statusLabel.setBounds(buttonX, 500, buttonWidth, buttonHeight);

        updateStatusLabel();

        add(startButton);
        add(squadButton);
        add(settingsButton);
        add(tutorialButton);
        add(skillTreeButton);
        add(toggleModeButton);
        add(exitButton);
        add(statusLabel);
        setLayout(null);

        startButton.addActionListener(new StartButtonAction());
        squadButton.addActionListener(new SquadButtonAction());
        settingsButton.addActionListener(new SettingsButtonAction());
        tutorialButton.addActionListener(new TutorialButtonAction());
        skillTreeButton.addActionListener(new SkillTreeButtonAction());
        toggleModeButton.addActionListener(new ToggleModeButtonAction());
        exitButton.addActionListener(new ExitButtonAction());

    }

    private class StartButtonAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Start button logic
            System.out.println("Start button clicked");
        }
    }

    private class SquadButtonAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            MainFrame frame = MainFrame.getINSTANCE();
            Status status = Status.getINSTANCE();
            Player player = Status.getINSTANCE().getPlayer();
            String username = player.getUsername();

            if (!isOnline){
                JOptionPane.showMessageDialog(frame, "You are not online!", "Error", JOptionPane.ERROR_MESSAGE);
            }

            else if (username == null) {
                username = JOptionPane.showInputDialog(frame, "Please Enter Your username");
                RequestFactory.createIdentificateReq(username);
            }

            if (username != null && isOnline) {

                status.getPlayer().setUsername(username);
                PanelManager.showSquadMenu();
            }
        }
    }

    private class SettingsButtonAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Settings button logic
            System.out.println("Settings button clicked");
        }
    }

    private class TutorialButtonAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Tutorial button clicked");
        }
    }

    private class SkillTreeButtonAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // SkillTree button logic
            System.out.println("SkillTree button clicked");
        }
    }

    private class ToggleModeButtonAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
//            toggleOnlineOfflineMode();

            if (isOnline) {
                Status.getINSTANCE().getSocket().close();
                Status.getINSTANCE().setConnectedToServer(false);
                System.out.println("socket closed successfully ... ");
//                isOnline = false;

            } else {
                System.out.println("in else block");
                tryConnection();
            }


            updateStatusLabel();
//            Menu.getINSTANCE();
        }
    }

    private class ExitButtonAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }



    public static MainMenu getINSTANCE() {
        if (MainMenu.INSTANCE == null) INSTANCE = new MainMenu();
        return INSTANCE;
    }

    private void toggleOnlineOfflineMode() {
        synchronized (this) {
            isOnline = !isOnline;
        }
        updateStatusLabel();
    }

    public void updateStatusLabel() {
        synchronized (this) {
            isOnline = Status.getINSTANCE().isConnectedToServer();
            statusLabel.setText("Status: " + (isOnline ? "Online" : "Offline"));
        }
        statusLabel.repaint();
    }


}
