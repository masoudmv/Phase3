package model.charactersModel.blackOrb;

import controller.UserInterfaceController;
import javafx.scene.shape.Circle;
import model.charactersModel.BulletModel;
import model.charactersModel.GeoShapeModel;
import model.MyPolygon;
import model.collision.Collidable;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

import static model.charactersModel.blackOrb.BlackOrb.lasers;
import static model.imagetools.ToolBox.getBufferedImage;

public class Orb extends GeoShapeModel implements Collidable {
    static BufferedImage image; // transient to avoid serialization
    private Circle circle;
//    public static ArrayList<Orb> orbs = new ArrayList<>();

    public Orb(Point2D anchor) {
        super(anchor, image);
        myPolygon = new MyPolygon(new double[]{0, 0, 0}, new double[]{0, 0, 0}, 3);
        this.circle = new Circle(anchor.getX(), anchor.getY(), (double) image.getHeight() / 2);

        collidables.add(this);
    }

    public static BufferedImage loadImage() {
        Image img = new ImageIcon("./src/orb.png").getImage();
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
        if (other instanceof BulletModel) eliminate();
    }

    @Override
    public void onCollision(Collidable other) {}

    @Override
    public void eliminate() {
        super.eliminate();

        // List to collect lasers to be removed
        ArrayList<Laser> lasersToRemove = new ArrayList<>();

        // Iterate and collect lasers to be removed
        for (Laser laser : lasers) {
            if (laser.getOrbsOfALaser()[0] == this || laser.getOrbsOfALaser()[1] == this) {
                laser.eliminate();
                lasersToRemove.add(laser);
            }
        }

        // Remove collected lasers from the list
        lasers.removeAll(lasersToRemove);

        collidables.remove(this);
    }
}
