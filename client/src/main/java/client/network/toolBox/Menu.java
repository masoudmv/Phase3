package client.network.toolBox;

import client.network.Status;
import client.network.socket.SocketRequestSender;
import shared.request.HiRequest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static client.network.toolBox.utils.tryConnection;

public class Menu extends JPanel implements MouseListener {
    private static Menu INSTANCE;
    JLabel startLabel = new JLabel("Start");
    JLabel settingsLabel = new JLabel("Settings");
    JLabel tutorialLabel = new JLabel("Tutorial");
    JLabel skillTreeLabel = new JLabel("SkillTree");
    JLabel exitLabel = new JLabel("Exit");
    JLabel toggleModeLabel = new JLabel("Toggle Mode");
    JLabel statusLabel = new JLabel();
    Point start;
    Point settings;
    Point tutorial;
    Point skillTree;
    Point exit;
    Point toggleMode;
    private int buttonHeight = 30;
    private int buttonWidth = 120;

    private volatile boolean isOnline; // Use volatile to ensure visibility across threads

    private Menu() {
        MainFrame frame = MainFrame.getINSTANCE();
        Dimension dimension = new Dimension(600, 600);
        setSize(dimension);
        setVisible(true);
        setLocationToCenter(frame);

        // Set the positions of labels and buttons relative to the menu's width
        int labelX = getWidth() / 2 - buttonWidth / 2;
        int buttonX = getWidth() / 2 - buttonWidth / 2;
        start = new Point(buttonX, 150);
        settings = new Point(buttonX, 200);
        tutorial = new Point(buttonX, 250);
        skillTree = new Point(buttonX, 300);
        exit = new Point(buttonX, 350);
        toggleMode = new Point(buttonX, 400);

        startLabel.setBounds(labelX + 25, 150, buttonWidth, buttonHeight);
        settingsLabel.setBounds(labelX + 15, 200, buttonWidth, buttonHeight);
        tutorialLabel.setBounds(labelX + 15, 250, buttonWidth, buttonHeight);
        skillTreeLabel.setBounds(labelX + 15, 300, buttonWidth, buttonHeight);
        exitLabel.setBounds(labelX + 25, 350, buttonWidth, buttonHeight);
        toggleModeLabel.setBounds(labelX + 15, 400, buttonWidth, buttonHeight);
        statusLabel.setBounds(labelX, 450, buttonWidth, buttonHeight);

        updateStatusLabel();

        add(startLabel);
        add(settingsLabel);
        add(tutorialLabel);
        add(skillTreeLabel);
        add(exitLabel);
        add(toggleModeLabel);
        add(statusLabel);
        setLayout(null);
        addMouseListener(this);
        frame.add(this);
        frame.repaint();

        if (isOnline) new Thread(new ConnectionChecker()).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.gray);
        g.fillRect(start.x, start.y, buttonWidth, buttonHeight);
        g.fillRect(settings.x, settings.y, buttonWidth, buttonHeight);
        g.fillRect(tutorial.x, tutorial.y, buttonWidth, buttonHeight);
        g.fillRect(skillTree.x, skillTree.y, buttonWidth, buttonHeight);
        g.fillRect(exit.x, exit.y, buttonWidth, buttonHeight);
        g.fillRect(toggleMode.x, toggleMode.y, buttonWidth, buttonHeight);
        labelRepaint();
    }

    private void labelRepaint() {
        startLabel.repaint();
        settingsLabel.repaint();
        tutorialLabel.repaint();
        skillTreeLabel.repaint();
        exitLabel.repaint();
        toggleModeLabel.repaint();
        statusLabel.repaint();
    }

    public void setLocationToCenter(MainFrame frame) {
        setLocation(frame.getWidth() / 2 - getWidth() / 2, frame.getHeight() / 2 - getHeight() / 2);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getY() > start.y && e.getY() < start.y + buttonHeight && start.x < e.getX() && e.getX() < start.x + buttonWidth) {
            removeMainMenu();
            Robot r;
            try {
                r = new Robot();
            } catch (AWTException ex) {
                throw new RuntimeException(ex);
            }
            r.setAutoDelay(25);
            r.keyPress(KeyEvent.VK_WINDOWS);
            r.keyPress(KeyEvent.VK_D);
            r.keyRelease(KeyEvent.VK_D);
            r.keyRelease(KeyEvent.VK_WINDOWS);

            r.keyPress(KeyEvent.VK_ALT);
            r.keyPress(KeyEvent.VK_TAB);
            r.keyRelease(KeyEvent.VK_TAB);
            r.keyRelease(KeyEvent.VK_ALT);
        }
        if (e.getY() > settings.y && e.getY() < settings.y + buttonHeight && settings.x < e.getX() && e.getX() < settings.x + buttonWidth) {
            removeMainMenu();
        }
        if (e.getY() > tutorial.y && e.getY() < tutorial.y + buttonHeight && tutorial.x < e.getX() && e.getX() < tutorial.x + buttonWidth) {
            System.out.println("tutorial chosen");
        }
        if (e.getY() > skillTree.y && e.getY() < skillTree.y + buttonHeight && skillTree.x < e.getX() && e.getX() < skillTree.x + buttonWidth) {
            removeMainMenu();
        }
        if (e.getY() > exit.y && e.getY() < exit.y + buttonHeight && exit.x < e.getX() && e.getX() < exit.x + buttonWidth) {
            System.exit(0);
        }
        if (e.getY() > toggleMode.y && e.getY() < toggleMode.y + buttonHeight && toggleMode.x < e.getX() && e.getX() < toggleMode.x + buttonWidth) {
            toggleOnlineOfflineMode();

            eliminate();

            if (isOnline) {
                Status.getINSTANCE().getSocket().close();
                Status.getINSTANCE().setConnectedToServer(false);
                isOnline = false;
            }

            else {
                tryConnection();
            }

            Menu.getINSTANCE();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    private void removeMainMenu() {
        MainFrame frame = MainFrame.getINSTANCE();
        frame.remove(this);
        frame.repaint();
    }

    public static void addMenu() {
        MainFrame.getINSTANCE().add(Menu.getINSTANCE());
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

    public void eliminate(){
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
