package model.charactersModel.blackOrb;

import controller.Game;
import model.FinalPanelModel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import static controller.UserInterfaceController.updateGeoShapeViewProperties;
import static controller.Utils.*;
import static controller.constants.EntityConstants.ORB_PANEL_CREATION_DELAY;

public class BlackOrb { // todo panels should be created with delay?
    Random random = new Random();
    FinalPanelModel[] panels = new FinalPanelModel[5];
    Orb[] orbs = new Orb[5];
    int numCreatedOrbs = 0;
    public static CopyOnWriteArrayList<Laser> lasers = new CopyOnWriteArrayList<>();
    double lastCreatedOrbTime = 0;
    private final Point2D[] vertices = new Point2D[5];
    private final Point2D movePanelLocation = new Point2D.Double(125, 125);
    public static CopyOnWriteArrayList<BlackOrb> blackOrbs = new CopyOnWriteArrayList<>();
    private boolean avalancheIsSet = false;
    private double avalancheBirthTime;

    public BlackOrb() throws InterruptedException {
        super();
        this.avalancheBirthTime = random.nextInt((int) (Game.ELAPSED_TIME + 6), (int) (Game.ELAPSED_TIME + 15));
        Point2D pivot = new Point2D.Double(500, 400); // Center of the pentagon
        double edgeLength = 350; // Distance between adjacent vertices
        double radius = edgeLength / (2 * Math.sin(Math.PI / 5)); // Circumradius of the pentagon
        for (int i = 0; i < 5; i++) {
            double angle = 2 * Math.PI * i / 5 + Math.PI / 2; // Central angle for each vertex, adjusted for upside-down orientation
            vertices[i] = new Point2D.Double(
                    pivot.getX() + radius * Math.cos(angle),
                    pivot.getY() + radius * Math.sin(angle)
            );
        }
        blackOrbs.add(this);
    }

    public void initiateBlackOrb(){
        initiateAvalanche();
        Laser.performAoeDamage();

        double now = Game.ELAPSED_TIME;
        if (now - lastCreatedOrbTime > ORB_PANEL_CREATION_DELAY && numCreatedOrbs == 5) {
            // Run initializeOrbs in a separate thread
//            executorService.submit(this::initializedOrbs); // TODO ADD CONURENCY TO REQUIRED METHODS IN GAMELOOP.
            new Thread(new Runnable() {
                @Override
                public void run() {
                    initializedOrbs();
                }
            }).start();

            numCreatedOrbs++;
        }
        if ( now - lastCreatedOrbTime < ORB_PANEL_CREATION_DELAY || numCreatedOrbs > 4) return;
        FinalPanelModel p = new FinalPanelModel(vertices[numCreatedOrbs], getPanelDimension());
        p.setIsometric(true);
        panels[numCreatedOrbs] = p;
        lastCreatedOrbTime = now;
        numCreatedOrbs ++;


    }

    private void initializedOrbs(){
        for (int i = 0; i < 5; i++) {
            orbs[i] = new Orb(addVectors(vertices[i], movePanelLocation));
//            updateGeoShapeViewProperties();

        }
        setLasers();
//        updateGeoShapeViewProperties();
    }


    private Dimension getPanelDimension(){
        return new Dimension(250, 250);

    }

    private void setLasers() {
        for (int i = 0; i < 5; i++) {
            for (int j = i+1; j < 5; j++) {
                new Laser(orbs[i], orbs[j]);
//                updateGeoShapeViewProperties();

            }
        }
    }

    public void initiateAvalanche(){
        double now = Game.ELAPSED_TIME;
        if (now - lastCreatedOrbTime < avalancheBirthTime || avalancheIsSet) return;
        int index = random.nextInt(lasers.size());
        lasers.get(index).setAvalanche(true);
        avalancheIsSet = true;
    }
}
