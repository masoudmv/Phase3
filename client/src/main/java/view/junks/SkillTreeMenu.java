package view.junks;

import controller.Game;
import model.entities.Profile;
import model.entities.Skill;
import view.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import static model.entities.Skill.buySkill;

public class SkillTreeMenu extends JPanel implements MouseListener {
    JLabel backLabel = new JLabel("Back");
    JLabel xpLabel = new JLabel("XP: 0");  // XP label to display XP points
    Point backButton = new Point(22, 22);

    private final int branchWidth = 170;
    private final int branchHeight = 440;
    private final int optionWidth = 150;
    private final int optionHeight = 50;

    private boolean touchingBackButton = false;

    private Point attackBranch = new Point(22, 140);
    private Point defendBranch = new Point(214, 140);
    private Point polymorphBranch = new Point(406, 140);

    private List<JButton> attackButtons = new ArrayList<>();
    private List<JButton> defendButtons = new ArrayList<>();
    private List<JButton> polymorphButtons = new ArrayList<>();

    private Point lastSelectedBranch = null;
    private int lastSelectedOptionIndex = -1;

    public SkillTreeMenu() {
        MainFrame frame = MainFrame.getINSTANCE();
        Dimension dimension = new Dimension(600, 600);
        setSize(dimension);
        setVisible(true);
        setLocationToCenter(frame);
        setLayout(null);

        // Add labels to the panel
        backLabel.setFont(new Font("Arial", Font.BOLD, 16));
        backLabel.setForeground(Color.WHITE);
        add(backLabel);
        backLabel.setBounds(backButton.x + 20, backButton.y, 75, 35);

        // Add XP label to the panel
        xpLabel.setFont(new Font("Arial", Font.BOLD, 16));
        xpLabel.setForeground(Color.WHITE);
        add(xpLabel);
        xpLabel.setBounds(dimension.width - 150, 22, 120, 35);  // Position in the right-top part

        // Initialize options
        initializeOptions();
        updateXP();

        addMouseListener(this);
        frame.add(this);
        frame.repaint();
    }

    private void initializeOptions() {
        addSkillButton(Skill.ARES, attackBranch, 0, attackButtons);
        addSkillButton(Skill.ASTRAPE, attackBranch, 1, attackButtons);
        addSkillButton(Skill.CERBERUS, attackBranch, 2, attackButtons);

        addSkillButton(Skill.ACESO, defendBranch, 0, defendButtons);
        addSkillButton(Skill.MELAMPUS, defendBranch, 1, defendButtons);
        addSkillButton(Skill.CHIRON, defendBranch, 2, defendButtons);
        addSkillButton(Skill.ATHENA, defendBranch, 3, defendButtons);

        addSkillButton(Skill.PROTEUS, polymorphBranch, 0, polymorphButtons);
        addSkillButton(Skill.EMPUSA, polymorphBranch, 1, polymorphButtons);
        addSkillButton(Skill.DOLUS, polymorphBranch, 2, polymorphButtons);
    }

    private void addSkillButton(Skill skill, Point branchPosition, int index, List<JButton> buttonList) {
        int offsetY = 40 + (index * 60);
        JButton button = new JButton(skill.getName());
        button.setBounds(branchPosition.x + 10, branchPosition.y + offsetY, optionWidth, optionHeight);
        button.addActionListener(e -> {
            System.out.println(skill.getName());
            String message = buySkill(skill, Profile.getCurrent().totalXP);
            JOptionPane.showMessageDialog(this, message);
            updateXP();
            repaint();
        });
        buttonList.add(button);
        add(button);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.black);

        // Draw back button
        g.setColor(Color.gray);
        g.fillRect(backButton.x, backButton.y, 75, 35);

        if (touchingBackButton) {
            backLabel.setFont(new Font("Arial", Font.BOLD, 16));
        } else {
            backLabel.setFont(new Font("Arial", Font.BOLD, 12));
        }

        // Draw branches
        drawBranch(g, "Attack", attackBranch);
        drawBranch(g, "Guard", defendBranch);
        drawBranch(g, "Polymorph", polymorphBranch);
    }

    private void drawBranch(Graphics g, String branchName, Point branchPosition) {
        g.setColor(Color.gray);
        g.fillRect(branchPosition.x, branchPosition.y, branchWidth, branchHeight);
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString(branchName, branchPosition.x + 10, branchPosition.y + 30);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (isCursorInRectangle(e, backButton, 75, 35)) {
            removeSkillTreeMenu();
            new Menu();
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

    private void removeSkillTreeMenu() {
        MainFrame frame = MainFrame.getINSTANCE();
        frame.remove(this);
        frame.repaint();
    }

    public void setLocationToCenter(MainFrame frame) {
        setLocation(frame.getWidth() / 2 - getWidth() / 2, frame.getHeight() / 2 - getHeight() / 2);
    }

    // Method to update the XP display
    public void updateXP() {
        xpLabel.setText("XP: " + Profile.getCurrent().totalXP);
    }
}
