package model.charactersModel.blackOrb;

import controller.UserInterfaceController;
import javafx.scene.shape.Circle;
import model.charactersModel.EpsilonModel;
import model.charactersModel.GeoShapeModel;
import model.MyPolygon;
import model.collision.Collidable;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static model.imagetools.ToolBox.getBufferedImage;

public class Orb extends GeoShapeModel implements Collidable {
    static BufferedImage image; // transient to avoid serialization
    private Circle circle;
//    private double radius;
    public static ArrayList<Orb> orbs = new ArrayList<>();

    public Orb(Point2D anchor) {
        super(anchor, image);
        myPolygon = new MyPolygon(new double[]{0, 0,0,}, new double[]{0, 0,0},3);
        this.circle = new Circle(anchor.getX(), anchor.getY(), (double) image.getHeight()/2);
s
        orbs.add(this);
        collidables.add(this);
    }

    public static void drawOrbs(Component component, Graphics g){
        for (int i = 0; i < orbs.size(); i++) {
            Point anc = UserInterfaceController.calculateEntityView(component, orbs.get(i).getAnchor());
            g.fillOval(anc.x-50,anc.y-50,100,100);
        }
    }

    public static BufferedImage loadImage() {
        Image img = new ImageIcon("./src/epsilon.png").getImage();
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
    public void onCollision(Collidable other) {

    }

    public void eliminate() {

    }
}