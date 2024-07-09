package view;

import controller.Game;
import org.example.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import static controller.Game.activeAbility;
import static controller.Game.bufferedImage;
import static org.example.Main.*;

public class SkillTreeMenu extends JPanel implements MouseListener, MouseMotionListener {
    JLabel backLabel = new JLabel("back");
    Point backButton = new Point(22, 22);

    Point aresRect = new Point(22, 140);

    Point acesoRect = new Point(214, 140);

    Point proteusRect = new Point(406, 140);
    private int width = 170;
    private int height = 440;

    private boolean touchingBackButton = false;
    private boolean touchingAresButton = false;
    private boolean touchingAcesoButton = false;
    private boolean touchingProteusButton = false;



    // Offset to move rectangles
    private int offsetY = -20;  // Amount to move rectangle up

    public SkillTreeMenu() {
        MainFrame frame = MainFrame.getINSTANCE();
        Dimension dimension = new Dimension(600, 600);
        setSize(dimension);
        setVisible(true);
        setLocationToCenter(frame);
        setLayout(null);


        // Add labels to the panel
        add(backLabel);
//        add(aresLabel);
//        add(acesoLabel);
//        add(proteusLabel);

        // Set label positions
        backLabel.setBounds(backButton.x + 20, backButton.y, 75, 35);

        addMouseListener(this);
        addMouseMotionListener(this);
        frame.add(this);
        frame.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
//        setBackground(Color.black);
        g.drawImage(Main.background, 0, 0, null);

        // Adjust position and color based on whether the mouse is over the rectangle
        g.setColor(Color.gray);
        g.fillRect(backButton.x, backButton.y, 75, 35);

        if (aresIsActivated) g.drawImage(ares, aresRect.x, (touchingAresButton || activeAbility== Game.SkillTreeAbility.ares) ? aresRect.y + offsetY : aresRect.y, this);
        if (!aresIsActivated) g.drawImage(ares_, aresRect.x, (touchingAresButton || activeAbility== Game.SkillTreeAbility.ares) ? aresRect.y + offsetY : aresRect.y, this);

        if (acesoIsActivated) g.drawImage(aceso, acesoRect.x, (touchingAcesoButton || activeAbility== Game.SkillTreeAbility.aceso) ? acesoRect.y + offsetY : acesoRect.y, this);
        if (!acesoIsActivated) g.drawImage(aceso_, acesoRect.x, (touchingAcesoButton || activeAbility== Game.SkillTreeAbility.aceso) ? acesoRect.y + offsetY : acesoRect.y, this);

        if (proteusIsActivated) g.drawImage(proteus, proteusRect.x, (touchingProteusButton || activeAbility== Game.SkillTreeAbility.proteus) ? proteusRect.y + offsetY : proteusRect.y, this);
        if (!proteusIsActivated) g.drawImage(proteus_, proteusRect.x, (touchingProteusButton || activeAbility== Game.SkillTreeAbility.proteus) ? proteusRect.y + offsetY : proteusRect.y, this);
    }


    @Override
    public void mouseDragged(MouseEvent e) {

    }

    public void mouseMoved(MouseEvent e) {
        touchingBackButton = isCursorInRectangle(e, backButton, 75, 35);
        touchingAresButton = isCursorInRectangle(e, aresRect, width, height);
        touchingAcesoButton = isCursorInRectangle(e, acesoRect, width, height);
        touchingProteusButton = isCursorInRectangle(e, proteusRect, width, height);
        repaint();  // This ensures the component is repainted with the new positions
    }

    private boolean isCursorInRectangle(MouseEvent e, Point rectPosition, int rectWidth, int rectHeight) {
        return e.getX() >= rectPosition.x && e.getX() <= rectPosition.x + rectWidth &&
                e.getY() >= rectPosition.y && e.getY() <= rectPosition.y + rectHeight;
    }

    // Other methods (mouseClicked, mousePressed, etc.) remain unchanged

    public void setLocationToCenter(MainFrame frame){
        setLocation(frame.getWidth() / 2 - getWidth() / 2, frame.getHeight() / 2 - getHeight() / 2);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (isCursorInRectangle(e, backButton, 75, 35)){
            removeSkillTreeMenu();
            new Menu();
        }
        if (!aresIsActivated && totalXP>750 && isCursorInRectangle(e, aresRect, width, height)){
            totalXP-=750;
            aresIsActivated=true;
        }
        if (isCursorInRectangle(e, aresRect, width, height) && aresIsActivated) activeAbility = Game.SkillTreeAbility.ares;

        if (isCursorInRectangle(e, acesoRect, width, height) && !acesoIsActivated && totalXP>500){
            totalXP-=500;
            acesoIsActivated=true;
        } if (isCursorInRectangle(e, acesoRect, width, height) && acesoIsActivated) activeAbility = Game.SkillTreeAbility.aceso;

        if (isCursorInRectangle(e, proteusRect, width, height) && !proteusIsActivated && totalXP>1000){
            totalXP-=1000;
            proteusIsActivated=true;
        } if (isCursorInRectangle(e, proteusRect, width, height) && proteusIsActivated) activeAbility = Game.SkillTreeAbility.proteus;
        MainFrame.getINSTANCE().repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    public Game.SkillTreeAbility getActiveAbility() {
        return activeAbility;
    }

    private void removeSkillTreeMenu(){
        MainFrame frame = MainFrame.getINSTANCE();
        frame.remove(this);
        frame.repaint();
    }
}
