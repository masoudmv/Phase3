package view;

import controller.Game;
import org.example.Main;
import view.MainFrame;
import view.SettingsMenu;
import view.SkillTreeMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static org.example.Main.totalXP;

public class Menu extends JPanel implements MouseListener {
    private static Menu INSTANCE;
    JLabel startLabel = new JLabel("Start");
    JLabel settingsLabel = new JLabel("Settings");
    JLabel tutorialLabel = new JLabel("Tutorial");
    JLabel skillTreeLabel = new JLabel("SkillTree");
    JLabel exitLabel = new JLabel("Exit");
    Point start;
    Point settings;
    Point tutorial;
    Point skillTree;
    Point exit;
    private int buttonHeight = 30;
    private int buttonWidth = 80;

    public Menu() {
        MainFrame frame = MainFrame.getINSTANCE();
        Dimension dimension = new Dimension(600, 600);
        setSize(dimension);
        setVisible(true);
        setLocationToCenter(frame);

        // Set the positions of labels and buttons relative to the menu's width
        int labelX = getWidth() / 2 - buttonWidth / 2;
        int buttonX = getWidth() / 2 - buttonWidth / 2;
        start = new Point(buttonX, 250);
        settings = new Point(buttonX, 300);
        tutorial = new Point(buttonX, 350);
        skillTree = new Point(buttonX, 400);
        exit = new Point(buttonX, 450);

        startLabel.setBounds(labelX+25, 250, buttonWidth, buttonHeight);
        settingsLabel.setBounds(labelX+15, 300, buttonWidth, buttonHeight);
        tutorialLabel.setBounds(labelX+15, 350, buttonWidth, buttonHeight);
        skillTreeLabel.setBounds(labelX+15, 400, buttonWidth, buttonHeight);
        exitLabel.setBounds(labelX+25, 450, buttonWidth, buttonHeight);


        JLabel totXP = new JLabel("Total XP: " + Integer.toString(totalXP));
        totXP.setBounds(0,0,100,40);
        totXP.setBackground(Color.gray);
        totXP.setOpaque(true);
        add(totXP);

        add(startLabel);
        add(settingsLabel);
        add(tutorialLabel);
        add(skillTreeLabel);
        add(exitLabel);
        setLayout(null);
        addMouseListener(this);
        frame.add(this);
        frame.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(Main.background, 0, 0, null);
        g.setColor(Color.gray);
        g.fillRect(start.x, start.y, buttonWidth, buttonHeight);
        g.fillRect(settings.x, settings.y, buttonWidth, buttonHeight);
        g.fillRect(tutorial.x, tutorial.y, buttonWidth, buttonHeight);
        g.fillRect(skillTree.x, skillTree.y, buttonWidth, buttonHeight);
        g.fillRect(exit.x, exit.y, buttonWidth, buttonHeight);
        labelRepaint();
    }

    private void labelRepaint() {
        startLabel.repaint();
        settingsLabel.repaint();
        tutorialLabel.repaint();
        skillTreeLabel.repaint();
        exitLabel.repaint();
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

//            MainFrame.getINSTANCE().makeFrameNull();
//            MainFrame.getINSTANCE();
            new Game();
        }
        if (e.getY() > settings.y && e.getY() < settings.y + buttonHeight && settings.x < e.getX() && e.getX() < settings.x + buttonWidth) {
            removeMainMenu();
            new SettingsMenu();
        }
        if (e.getY() > tutorial.y && e.getY() < tutorial.y + buttonHeight && tutorial.x < e.getX() && e.getX() < tutorial.x + buttonWidth) {
            System.out.println("tutorial chosen");
        }
        if (e.getY() > skillTree.y && e.getY() < skillTree.y + buttonHeight && skillTree.x < e.getX() && e.getX() < skillTree.x + buttonWidth) {
            removeMainMenu();
            new SkillTreeMenu();
        }
        if (e.getY() > exit.y && e.getY() < exit.y + buttonHeight && exit.x < e.getX() && e.getX() < exit.x + buttonWidth) {
            System.exit(0);
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
    public static void addMenu(){
        MainFrame.getINSTANCE().add(Menu.getINSTANCE());
        MainFrame.getINSTANCE().repaint();
    }

    public static Menu getINSTANCE() {
        if (Menu.INSTANCE == null) INSTANCE = new Menu();
        return INSTANCE;
    }
}
