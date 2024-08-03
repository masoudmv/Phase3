package game.view.junks;

import game.model.entities.Profile;
import game.model.entities.Skill;
import game.view.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;



public class SkillTreeMenu extends JPanel implements MouseListener, MouseMotionListener {
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

    private List<SkillTreeOption> attackOptions = new ArrayList<>();
    private List<SkillTreeOption> defendOptions = new ArrayList<>();
    private List<SkillTreeOption> polymorphOptions = new ArrayList<>();

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
        xpLabel.setBounds(dimension.width - 100, 22, 75, 35);  // Position in the right-top part

        // Initialize options
        initializeOptions();

        addMouseListener(this);
        addMouseMotionListener(this);
        frame.add(this);
        frame.repaint();
    }

    private void initializeOptions() {
        attackOptions.add(new SkillTreeOption(Skill.ARES, new Point(attackBranch.x + 10, attackBranch.y + 40)));
        attackOptions.add(new SkillTreeOption(Skill.ASTRAPE, new Point(attackBranch.x + 10, attackBranch.y + 40 + 60)));
        attackOptions.add(new SkillTreeOption(Skill.CERBERUS, new Point(attackBranch.x + 10, attackBranch.y + 40 + (2 * 60))));

        defendOptions.add(new SkillTreeOption(Skill.ACESO, new Point(defendBranch.x + 10, defendBranch.y + 40)));
        defendOptions.add(new SkillTreeOption(Skill.MELAMPUS, new Point(defendBranch.x + 10, defendBranch.y + 40 + 60)));
        defendOptions.add(new SkillTreeOption(Skill.CHIRON, new Point(defendBranch.x + 10, defendBranch.y + 40 + (2 * 60))));
        defendOptions.add(new SkillTreeOption(Skill.ATHENA, new Point(defendBranch.x + 10, defendBranch.y + 40 + (3 * 60))));

        polymorphOptions.add(new SkillTreeOption(Skill.PROTEUS, new Point(polymorphBranch.x + 10, polymorphBranch.y + 40)));
        polymorphOptions.add(new SkillTreeOption(Skill.EMPUSA, new Point(polymorphBranch.x + 10, polymorphBranch.y + 40 + 60)));
        polymorphOptions.add(new SkillTreeOption(Skill.DOLUS, new Point(polymorphBranch.x + 10, polymorphBranch.y + 40 + (2 * 60))));
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

        // Draw branches and options
        drawBranch(g, "Attack", attackBranch, attackOptions);
        drawBranch(g, "Guard", defendBranch, defendOptions);
        drawBranch(g, "Polymorph", polymorphBranch, polymorphOptions);
    }

    private void drawBranch(Graphics g, String branchName, Point branchPosition, List<SkillTreeOption> options) {
        g.setColor(Color.gray);
        g.fillRect(branchPosition.x, branchPosition.y, branchWidth, branchHeight);
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString(branchName, branchPosition.x + 10, branchPosition.y + 30);

        for (int i = 0; i < options.size(); i++) {
            SkillTreeOption option = options.get(i);
            Point optionPosition = option.getPosition();

            if (option.isSelected()) {
                g.setColor(Color.blue); // Background color for selected options
                g.fillRect(optionPosition.x, optionPosition.y, optionWidth, optionHeight);
                g.setColor(Color.white);
            } else {
                g.setColor(Color.darkGray);
                g.fillRect(optionPosition.x, optionPosition.y, optionWidth, optionHeight);
                g.setColor(Color.white);
            }

            if (option.isTouching()) {
                g.setFont(new Font("Arial", Font.BOLD, 14));
            } else {
                g.setFont(new Font("Arial", Font.PLAIN, 12));
            }

            if (branchPosition.equals(lastSelectedBranch) && i == lastSelectedOptionIndex) {
                g.setColor(Color.red); // Text color for the last chosen option
            }

            g.drawString(option.getName(), optionPosition.x + 10, optionPosition.y + 30);
            g.setColor(Color.white); // Reset color to white for other texts
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        touchingBackButton = isCursorInRectangle(e, backButton, 75, 35);
        updateTouchingOptions(e, attackOptions);
        updateTouchingOptions(e, defendOptions);
        updateTouchingOptions(e, polymorphOptions);
        repaint();
    }

    private void updateTouchingOptions(MouseEvent e, List<SkillTreeOption> options) {
        for (SkillTreeOption option : options) {
            option.setTouching(isCursorInRectangle(e, option.getPosition(), optionWidth, optionHeight));
        }
    }

    private boolean isCursorInRectangle(MouseEvent e, Point rectPosition, int rectWidth, int rectHeight) {
        return e.getX() >= rectPosition.x && e.getX() <= rectPosition.x + rectWidth &&
                e.getY() >= rectPosition.y && e.getY() <= rectPosition.y + rectHeight;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (isCursorInRectangle(e, backButton, 75, 35)) {
            removeSkillTreeMenu();
            new Menu();
        }

        handleBranchClick(e, attackBranch, attackOptions);
        handleBranchClick(e, defendBranch, defendOptions);
        handleBranchClick(e, polymorphBranch, polymorphOptions);

        MainFrame.getINSTANCE().repaint();
    }

    private void handleBranchClick(MouseEvent e, Point branchPosition, List<SkillTreeOption> options) {
        for (int i = 0; i < options.size(); i++) {
            SkillTreeOption option = options.get(i);
            Point optionPosition = option.getPosition();
            if (isCursorInRectangle(e, optionPosition, optionWidth, optionHeight)) {
                option.setSelected(true);
                lastSelectedBranch = branchPosition;
                lastSelectedOptionIndex = i;
//                System.out.println(branchPosition.toString() + " Option " + (i + 1) + " clicked");
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}



    private void removeSkillTreeMenu() {
        MainFrame frame = MainFrame.getINSTANCE();
        frame.remove(this);
        frame.repaint();
    }

    public void setLocationToCenter(MainFrame frame) {
        setLocation(frame.getWidth() / 2 - getWidth() / 2, frame.getHeight() / 2 - getHeight() / 2);
    }

    // Method to update the XP display
    public void updateXP(int xp) {
        xpLabel.setText("XP: " + Profile.getCurrent().totalXP);
    }
}
