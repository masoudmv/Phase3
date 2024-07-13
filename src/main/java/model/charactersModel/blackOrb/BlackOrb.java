package model.charactersModel.blackOrb;

import view.panel.GamePanelView;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import static controller.Utils.*;

public class BlackOrb { // todo panels should be created with delay?
    private double radiusOfOrb = 50;
    GamePanelView[] panels = new GamePanelView[5];
    Orb[] orbs = new Orb[5];
    public static ArrayList<Laser> lasers = new ArrayList<>();

    public BlackOrb() throws InterruptedException {
        super();
        Point2D pivot = new Point2D.Double(500, 500); // Center of the pentagon
        double edgeLength = 350; // Distance between adjacent vertices
        double radius = edgeLength / (2 * Math.sin(Math.PI / 5)); // Circumradius of the pentagon

        Point2D[] vertices = new Point2D[5];
        for (int i = 0; i < 5; i++) {
            double angle = 2 * Math.PI * i / 5 + Math.PI / 2; // Central angle for each vertex, adjusted for upside-down orientation
            vertices[i] = new Point2D.Double(
                    pivot.getX() + radius * Math.cos(angle),
                    pivot.getY() + radius * Math.sin(angle)
            );
        }

        Point2D movePanelLocation = new Point2D.Double(125, 125);

        for (int i = 0; i < vertices.length; i++) {
            panels[i] = new GamePanelView(vertices[i]);
            orbs[i] = new Orb(addVectors(vertices[i], movePanelLocation), radiusOfOrb);
        }

        setLasers();

        for (GamePanelView panel : panels) {
            panel.repaint();
        }
    }

    private void setLasers() {
        for (int i = 0; i < 5; i++) {
            for (int j = i + 1; j < 5; j++) {
                lasers.add(new Laser(orbs[i], orbs[j]));
            }
        }
    }

    public static void drawBlackOrb(Component component, Graphics g) {
        Orb.drawOrbs(component, g);
        Laser.drawLasers(component, g);
    }
}
