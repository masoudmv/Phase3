package view;
import controller.Game;
import org.example.Main;
import view.MainFrame;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import static controller.Constants.*;

public class SettingsMenu extends JPanel implements MouseListener, MouseMotionListener, ChangeListener {
    JLabel backLabel = new JLabel("back");
    Point backButton = new Point(22, 22);

    JLabel difficultyLabel = new JLabel("Difficulty:", SwingConstants.CENTER);
    JSlider difficultySlider = new JSlider(0, 100, 100); // Assuming range 0-100

    JLabel volumeLabel = new JLabel("Sound Volume:", SwingConstants.CENTER);
    JSlider volumeSlider = new JSlider(0, 100, 100);

    JLabel sensitivityLabel = new JLabel("Movement Sensitivity:", SwingConstants.CENTER);
    JSlider sensitivitySlider = new JSlider(0, 100, 50);

    JLabel keyBindingsLabel = new JLabel("Key Bindings");
    Point keyBindingsButton = new Point(22, 550); // Position for the key bindings button

    private int width = 120;
    private int height = 35; // Changed to a reasonable height for a button

    public SettingsMenu() {
        MainFrame frame = MainFrame.getINSTANCE();
        Dimension dimension = new Dimension(600, 600);
        setSize(dimension);
        setVisible(true);
        setLocationToCenter(frame);
        setLayout(null);

        // Adding labels and sliders
        add(backLabel);
        backLabel.setBounds(backButton.x + 20, backButton.y, 75, 35);

        add(difficultyLabel);
        difficultyLabel.setBounds(22, 140, 150, 20);
        difficultyLabel.setBackground(Color.gray);
        difficultyLabel.setOpaque(true);
        add(difficultySlider);
        difficultySlider.setBounds(22, 165, 300, 30);
        difficultySlider.addChangeListener(this);


        add(volumeLabel);
        volumeLabel.setBounds(22, 245, 150, 20);
        volumeLabel.setBackground(Color.gray);
        volumeLabel.setOpaque(true);
        add(volumeSlider);
        volumeSlider.setBounds(22, 270, 300, 30);
        volumeSlider.addChangeListener(this);

        add(sensitivityLabel);
        sensitivityLabel.setBounds(22, 350, 180, 20);
        sensitivityLabel.setBackground(Color.gray);
        sensitivityLabel.setOpaque(true);
        add(sensitivitySlider);
        sensitivitySlider.setBounds(22, 375, 300, 30);
        sensitivitySlider.addChangeListener(this);

        add(keyBindingsLabel);
        keyBindingsLabel.setBounds(keyBindingsButton.x + 20, keyBindingsButton.y, width, height);

        addMouseListener(this);
        addMouseMotionListener(this);
        frame.add(this);
        frame.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(Main.background, 0, 0, null);
        g.setColor(Color.gray);
        g.fillRect(backButton.x, backButton.y, 75, 35);
        g.fillRect(keyBindingsButton.x, keyBindingsButton.y, width, height);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (isCursorInRectangle(e, backButton, 75, 35)) {
            removeSettingsMenu();
            new Menu();
        } else if (isCursorInRectangle(e, keyBindingsButton, width, height)) {
            removeSettingsMenu();
            KeyBindingMenu.getINSTANCE().addKeyBindingsMenuToFrame();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    private boolean isCursorInRectangle(MouseEvent e, Point rectPosition, int rectWidth, int rectHeight) {
        return e.getX() >= rectPosition.x && e.getX() <= rectPosition.x + rectWidth &&
                e.getY() >= rectPosition.y && e.getY() <= rectPosition.y + rectHeight;
    }

    private void setLocationToCenter(MainFrame frame) {
        setLocation(frame.getWidth() / 2 - getWidth() / 2, frame.getHeight() / 2 - getHeight() / 2);
    }

    private void removeSettingsMenu() {
        MainFrame frame = MainFrame.getINSTANCE();
        frame.remove(this);
        frame.repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {}

    @Override
    public void stateChanged(ChangeEvent e) {
        double difficultyNumber = difficultySlider.getValue();
        if (difficultyNumber < 33.33) {
            Main.difficulty = Main.Difficulty.easy;
            INTERVAL=8;
            SQUARANTINE_RADIUS = 30;
            TRIGORATH_RADIUS = 30;
        }
        if (33.33 < difficultyNumber && difficultyNumber < 66.67) {
            Main.difficulty = Main.Difficulty.normal;
            INTERVAL=6;
            SQUARANTINE_RADIUS = 25;
            TRIGORATH_RADIUS = 25;

        }
        if (66.67 < difficultyNumber) {
            Main.difficulty = Main.Difficulty.hard;
            INTERVAL=5;
            SQUARANTINE_RADIUS = 20;
            TRIGORATH_RADIUS = 20;

        }

        Main.soundVolume = volumeSlider.getValue();

        Main.sensitivity = sensitivitySlider.getValue();

    }
}
