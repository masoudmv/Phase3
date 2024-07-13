package model.charactersModel.blackOrb;

import controller.Controller;
import javafx.scene.shape.Circle;
import model.charactersModel.GeoShapeModel;
import model.MyPolygon;
import model.collision.Collidable;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Orb extends GeoShapeModel implements Collidable {
    private Circle circle;
    private double radius;
    public static ArrayList<Orb> orbs = new ArrayList<>();

    public Orb(Point2D anchor, double radius) {
        super(anchor);
        this.circle = new Circle(anchor.getX(), anchor.getY(), radius);
        this.radius = radius;
        orbs.add(this);
    }

    public static void drawOrbs(Component component, Graphics g){
        for (int i = 0; i < orbs.size(); i++) {
            Point anc = Controller.calculateEntityView(component, orbs.get(i).getAnchor());
            g.fillOval(anc.x-50,anc.y-50,100,100);
        }
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
        return circle.getRadius();
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