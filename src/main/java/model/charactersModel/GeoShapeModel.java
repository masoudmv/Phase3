package model.charactersModel;

import model.FinalPanelModel;
import model.MyPolygon;
import model.entities.Entity;
import model.movement.Direction;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.UUID;

import static controller.Controller.createPolygonalEnemyView;
import static controller.Utils.*;


//todo extend mypolygon

public abstract class GeoShapeModel extends Entity {
    String id;
    Point2D anchor;
    public MyPolygon myPolygon;
    protected Direction direction;
    public boolean isLaser = false;
    public static ArrayList<GeoShapeModel> entities = new ArrayList<>();
    private double radius;




    private FinalPanelModel localFrame;
    protected double angle;
    private double angularVelocity;
    private double angularAcceleration;
    public GeoShapeModel(Point2D anchor, BufferedImage image, MyPolygon myPolygon) {
        this.id = UUID.randomUUID().toString(); //todo swap image and anchor set logic
        this.anchor = new Point2D.Double(
                anchor.getX() , anchor.getY()
        );
        this.myPolygon = myPolygon;
        radius = image.getHeight()/2;
        // following line is extra??
//        this.myPolygon.npoints = myPolygon.npoints;
        Point2D img = new Point2D.Double(-image.getWidth()/2, -image.getHeight()/2);


        moveVertices(addVectors(anchor, img));
        entities.add(this);
        createPolygonalEnemyView(id, image);
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
        radius = image.getHeight()/2;
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
        createPolygonalEnemyView(id);
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

    public abstract void eliminate();

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
    public ArrayList<Line2D> getEdges(){
        return findEdges(myPolygon);
    }
}

