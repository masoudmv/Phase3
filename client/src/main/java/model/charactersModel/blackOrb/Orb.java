package model.charactersModel.blackOrb;

import controller.Game;
import javafx.scene.shape.Circle;
import model.FinalPanelModel;
import model.charactersModel.BulletModel;
import model.charactersModel.CollectibleModel;
import model.charactersModel.EpsilonModel;
import model.charactersModel.GeoShapeModel;
import model.MyPolygon;
import model.collision.Collidable;
import model.entities.AttackTypes;
import model.entities.Entity;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import static controller.constants.EntityConstants.*;
import static model.charactersModel.EpsilonModel.epsilons;
import static model.charactersModel.blackOrb.BlackOrb.blackOrbs;
import static model.charactersModel.blackOrb.BlackOrb.lasers;
import static model.imagetools.ToolBox.getBufferedImage;

public class Orb extends GeoShapeModel implements Collidable {
    static BufferedImage image; // transient to avoid serialization
    private final Circle circle;
    private FinalPanelModel panel;

    public Orb(Point2D anchor) {
        super(anchor, image);
        this.circle = new Circle(anchor.getX(), anchor.getY(), (double) image.getHeight() / 2);
        collidables.add(this);
        this.health = ORB_HEALTH.getValue();

        /**
         checking for intersection with epsilon
         if it touches the orb in the creation process, YOU DIE!
         */
        for (EpsilonModel epsilon : epsilons){
            for (GeoShapeModel model : entities){
                if (model.intersects(this) && !model.equals(this)) model.eliminate();

            } if (epsilon.intersects(this)) System.out.println("YOU ARE DEAD!");
        }
    }

    public static BufferedImage loadImage() {
        Image img = new ImageIcon("./client/src/orb.png").getImage();
        Orb.image = getBufferedImage(img);
        return Orb.image;
    }


    public Circle getCircle() {
        return circle;
    }

    @Override
    public boolean isCircular() {
        return true;
    }

    @Override
    public double getRadius() {
        return radius;
    }

    @Override
    public Point2D getAnchor() {
        return super.getAnchor();
    }

    @Override
    public void setMyPolygon(MyPolygon myPolygon) {}

    @Override
    public Point2D[] getVertices() {
        return null;
    }

    @Override
    public ArrayList<Line2D> getEdges() {
        return null;
    }

    @Override
    public void onCollision(Collidable other, Point2D intersection) {
    }

    @Override
    public void onCollision(Collidable other, Point2D coll1, Point2D coll2) {}

    public FinalPanelModel getPanel() {
        return panel;
    }

    public void setPanel(FinalPanelModel panel) {
        this.panel = panel;
    }

    @Override
    public void eliminate() {
        super.eliminate();
        CollectibleModel.dropCollectible(getAnchor(), ORB_NUM_OF_COLLECTIBLES.getValue(), ORB_COLLECTIBLES_XP.getValue());
        collidables.remove(this);
        CopyOnWriteArrayList<Laser> lasersToRemove = new CopyOnWriteArrayList<>();
        // Iterate and collect lasers to be removed
        for (Laser laser : lasers) {
            if (laser.getOrbsOfALaser()[0] == this || laser.getOrbsOfALaser()[1] == this) {
                laser.eliminate();
                lasersToRemove.add(laser);
            }
        }
        lasers.removeAll(lasersToRemove);
        panel.eliminate();
        Game.getINSTANCE().incrementDeadEnemies();
    }
}
