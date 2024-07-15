package model.charactersModel;

import model.FinalPanelModel;
import model.MyPolygon;
import model.collision.entities.Entity;
import model.movement.Direction;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.UUID;

import static controller.UserInterfaceController.*;
import static controller.Utils.*;


//todo extend mypolygon

public abstract class GeoShapeModel extends Entity {
    protected String id;
    protected Point2D anchor;
    public MyPolygon myPolygon;
    protected Direction direction;
//    public boolean isLaser = false; // WTF
    public static ArrayList<GeoShapeModel> entities = new ArrayList<>();
    protected double radius;
    public FinalPanelModel localPanel;
    protected double angle;

    public GeoShapeModel(Point2D anchor, BufferedImage image, MyPolygon myPolygon) {
        this.id = UUID.randomUUID().toString(); //todo swap image and anchor set logic
        this.anchor = new Point2D.Double(
                anchor.getX() , anchor.getY()
        );
        this.myPolygon = myPolygon;
        radius = (double) image.getWidth()/2;
        // following line is extra??
//        this.myPolygon.npoints = myPolygon.npoints;
        Point2D img = new Point2D.Double((double) -image.getWidth()/2, (double) -image.getHeight()/2);


        moveVertices(addVectors(anchor, img));
        entities.add(this);
        createPolygonalEnemyView(id, image);
    }


    public GeoShapeModel(Point2D anchor, BufferedImage image, MyPolygon myPolygon, boolean necropick) { // exclusive for Necropick
        this.id = UUID.randomUUID().toString();
        this.anchor = new Point2D.Double(anchor.getX() , anchor.getY());
        this.myPolygon = myPolygon;
        radius = (double) image.getHeight()/2;
        Point2D img = new Point2D.Double((double) -image.getWidth()/2, (double) -image.getHeight()/2);
        moveVertices(addVectors(anchor, img));
        entities.add(this);
    }

    public GeoShapeModel(BufferedImage image, MyPolygon myPolygon){
        this.id = UUID.randomUUID().toString(); //todo swap image and anchor set logic
        this.myPolygon = myPolygon;
        entities.add(this);
        createPolygonalEnemyView(id, image);
    }

    public GeoShapeModel(Point2D anchor, BufferedImage image){
//        System.out.println("EPSILON");
        this.id = UUID.randomUUID().toString(); //todo swap image and anchor set logic(almost done. needs one final check)
        this.anchor = new Point2D.Double(
                anchor.getX() , anchor.getY()
        );
        radius = (double) image.getHeight()/2;
//        moveVertices(anchor);
        entities.add(this);
        createPolygonalEnemyView(id, image);
    }




    public GeoShapeModel(Point2D anchor){
        this.anchor = anchor;
        this.id = UUID.randomUUID().toString();
        entities.add(this);
        createPolygonalEnemyView(id);
    }

    public GeoShapeModel(){
        this.id = UUID.randomUUID().toString();
        entities.add(this);
//        createPolygonalEnemyView(id);
    }


    public String getId() {
        return id;
    }

    public Point2D getAnchor() {
        return anchor;
    }

    public double getAngle() {
        return angle;
    }

    public void setAnchor(Point2D anchor) {
        Point2D movement = relativeLocation(anchor,  this.anchor);
        movePolygon(movement);
    }

    public abstract void setMyPolygon(MyPolygon myPolygon);

    public void moveVertices(Point2D movement){
        double[] xpoints = new double[myPolygon.npoints];
        double[] ypoints = new double[myPolygon.npoints];
        for (int i = 0; i < myPolygon.npoints; i++) {
            xpoints[i] = this.myPolygon.xpoints[i] + movement.getX();
            ypoints[i] = this.myPolygon.ypoints[i] + movement.getY();
        }
        myPolygon = new MyPolygon(xpoints, ypoints, myPolygon.npoints);
    }

    public Point2D[] getVertices() { //todo
        Point2D[] vertices = new Point2D[myPolygon.npoints];
        for (int i = 0; i < myPolygon.npoints; i++) {
            double x = myPolygon.xpoints[i];
            double y = myPolygon.ypoints[i];
            vertices[i] = new Point2D.Double(x, y);
        }
        return vertices;
    }

    public void movePolygon(Point2D movement){
        anchor = addVectors(anchor, movement);
        moveVertices(movement);
    }

//    public void updateEntityLocation(Component component, )

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void eliminate(){
        entities.remove(this);
        findGeoShapeView(id).eliminate();
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
    public ArrayList<Line2D> getEdges(){
        return findEdges(myPolygon);
    }
}

