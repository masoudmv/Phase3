package client.network.toolBox;

import client.network.Status;
import client.network.socket.SocketRequestSender;
import shared.Model.Player;
import shared.request.HiRequest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static client.network.toolBox.utils.tryConnection;

public class Menu extends JPanel {
    private static Menu INSTANCE;
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

    private Menu() {
        MainFrame frame = MainFrame.getINSTANCE();
        Dimension dimension = new Dimension(600, 600);
        setSize(dimension);
        setVisible(true);
        setLocationToCenter(frame);

        int buttonX = getWidth() / 2 - buttonWidth / 2;
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

        frame.add(this);
        frame.repaint();

        if (isOnline) new Thread(new ConnectionChecker()).start();
    }

    private class StartButtonAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            removeMainMenu();
            // Start button logic
        }
    }

    private class SquadButtonAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            eliminate();
            Status status = Status.getINSTANCE();
            String name;
            if (status.getPlayer() == null) {
                name = JOptionPane.showInputDialog("Please Enter Your username");
            } else {
                name = status.getPlayer().getUsername();
            }
            if (name == null) {
                Menu.getINSTANCE();
            } else {
                status.setPlayer(new Player(name));
                showSquadMenu();
            }
        }
    }

    private class SettingsButtonAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            removeMainMenu();
            // Settings button logic
        }
    }

    private class TutorialButtonAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("tutorial chosen");
            // Tutorial button logic
        }
    }

    private class SkillTreeButtonAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            removeMainMenu();
            // SkillTree button logic
        }
    }

    private class ToggleModeButtonAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            toggleOnlineOfflineMode();
            eliminate();
            if (isOnline) {
                Status.getINSTANCE().getSocket().close();
                Status.getINSTANCE().setConnectedToServer(false);
                isOnline = false;
            } else {
                tryConnection();
            }
            Menu.getINSTANCE();
        }
    }

    private class ExitButtonAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    private void showSquadMenu() {
        MainFrame frame = MainFrame.getINSTANCE();
        SquadMenu squadMenu = new SquadMenu();
        frame.switchToPanel(squadMenu);
        squadMenu.updateSquadStatus();
    }

    private void removeMainMenu() {
        MainFrame frame = MainFrame.getINSTANCE();
        frame.remove(this);
        frame.revalidate();
        frame.repaint();
    }

    public void setLocationToCenter(MainFrame frame) {
        setLocation(frame.getWidth() / 2 - getWidth() / 2, frame.getHeight() / 2 - getHeight() / 2);
    }

    public static void addMenu() {
        MainFrame.getINSTANCE().add(Menu.getINSTANCE());
        MainFrame.getINSTANCE().revalidate();
        MainFrame.getINSTANCE().repaint();
    }

    public static Menu getINSTANCE() {
        if (Menu.INSTANCE == null) INSTANCE = new Menu();
        return INSTANCE;
    }

    private void toggleOnlineOfflineMode() {
        synchronized (this) {
            isOnline = !isOnline;
        }
        updateStatusLabel();
    }

    private void updateStatusLabel() {
        synchronized (this) {
            isOnline = Status.getINSTANCE().isConnectedToServer();
            statusLabel.setText("Status: " + (isOnline ? "Online" : "Offline"));
        }
        statusLabel.repaint();
    }

    public void eliminate() {
        SwingUtilities.invokeLater(() -> {
            MainFrame.getINSTANCE().remove(this);
            Menu.INSTANCE = null;
            MainFrame.getINSTANCE().repaint();
        });
    }

    private class ConnectionChecker implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);

                    synchronized (Menu.getINSTANCE()) {
                        if (!isOnline) break;
                    }
                    SocketRequestSender socketRequestSender = Status.getINSTANCE().getSocket();
                    socketRequestSender.sendRequest(new HiRequest());
                    System.out.println("Connection check successful.");
                } catch (Exception e) {
                    eliminate();
                    tryConnection();
                    break;
                }
            }
        }
    }
}
