package view.panel;

import model.charactersModel.blackOrb.BlackOrb;
import model.movement.Direction;
import view.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import static controller.constants.Constants.SPEED;
import static controller.Utils.addVectors;
import static controller.Utils.multiplyVector;

public class GamePanelView extends JPanel {
    protected Point2D location; // absolute location of the frame
    protected Dimension size;
    private Direction direction;
    public static ArrayList<GamePanelView> gamePanelViews = new ArrayList<>();

    public GamePanelView() {
        this(new Point2D.Double(100, 100), new Dimension(150, 150)); // Default position and size
    }

    public GamePanelView(Point2D position) {
        this(position, new Dimension(250, 250)); // Default position and size
    }

    public GamePanelView(Point2D position, Dimension size) {
        this.location = position;
        this.size = size;
        this.direction = new Direction(new Point2D.Double(1, 0)); //todo
        direction.setMagnitude(SPEED); //todo

        setSize(size);
        setVisible(true);
        setBackground(Color.BLACK);
        setLocation((int) location.getX(), (int) location.getY());

        MainFrame frame = MainFrame.getINSTANCE();
        frame.setLayout(null); // Absolute layout
        frame.add(this);
        gamePanelViews.add(this);
        repaint();
    }

    public void move() {
        move(direction);
        repaint();
    }

    private void move(Direction direction) {
        Point2D movement = multiplyVector(direction.getNormalizedDirectionVector(), direction.getMagnitude());
        moveLocation(movement);
    }

    private void moveLocation(Point2D movement) {
        this.location = addVectors(location, movement);
        setLocation((int) this.location.getX(), (int) this.location.getY());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.gray);
        BlackOrb.drawBlackOrb(this, g);

    }
}
