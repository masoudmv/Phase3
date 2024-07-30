package model.charactersModel;

import model.FinalPanelModel;
import model.MyPolygon;
import model.entities.Entity;
import model.entities.AttackTypes;
import model.movement.Direction;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static controller.UserInterfaceController.*;
//import static controller.UserInterfaceController.playHitSoundEffect;
import static controller.Utils.*;


public abstract class GeoShapeModel extends Entity {

    protected String id;
    protected Point2D anchor;
    public MyPolygon myPolygon;
    protected Direction direction = new Direction(new Point2D.Double(0, 0));
//    public ConcurrentHashMap<AttackTypes, Integer> damageSize = new ConcurrentHashMap<>();
//    public static ArrayList<GeoShapeModel> entities = new ArrayList<>();
    protected double radius;
    public FinalPanelModel localPanel;
    protected double angle;
//    private double lastMeleeTime = 0;
    protected boolean isOnFall = false;

    public GeoShapeModel(Point2D anchor, BufferedImage image, MyPolygon myPolygon) {
        this.id = UUID.randomUUID().toString();
        this.anchor = new Point2D.Double(
                anchor.getX() , anchor.getY()
        );
        this.myPolygon = myPolygon;
        radius = (double) image.getWidth()/2;
        Point2D img = new Point2D.Double((double) -image.getWidth()/2, (double) -image.getHeight()/2);


        moveVertices(addVectors(anchor, img));
        entities.add(this);
        createGeoShapeView(id, image);
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
        this.id = UUID.randomUUID().toString();
        this.myPolygon = myPolygon;
        entities.add(this);
        createGeoShapeView(id, image);
    }

    public GeoShapeModel(Point2D anchor, BufferedImage image){
        setDummyPolygon();
        this.id = UUID.randomUUID().toString();
        this.anchor = new Point2D.Double(
                anchor.getX() , anchor.getY()
        );
        radius = (double) image.getHeight()/2;
        entities.add(this);
        createGeoShapeView(id, image);
    }

    private void setBoundingRect(){

    }



    // deprecated
    public GeoShapeModel(Point2D anchor){
        this.anchor = anchor;
        this.id = UUID.randomUUID().toString();
        entities.add(this);
        createGeoShapeView(id);
    }

    public GeoShapeModel(){
        this.id = UUID.randomUUID().toString();
        entities.add(this);
        setDummyPolygon();
    }


    private void setDummyPolygon(){
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        myPolygon = new MyPolygon(x, y, 3);
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





    public void addHealth(int units) {
        this.health = Math.min(fullHealth, health + units);
    }

    // is it safe to use arraylist instead of copyOnRightArrayList?
    public ArrayList<Point2D> getBoundingPoints(){
        ArrayList<Point2D> bound = new ArrayList<>();
        for (Integer i : myPolygon.getBoundingPointIndexes()){
            bound.add(new Point2D.Double(myPolygon.xpoints[i], myPolygon.ypoints[i]));
        }
        return bound;
    }

    public void initiateFall(){
        isOnFall = true;
        //        direction.setMagnitude(5);
        this.direction = new Direction(new Point2D.Double(0, 1));
    }

    public void updateVelocityOnFall(){
        double magnitude = direction.getMagnitude();
        magnitude += 9.81 / 3;
        direction.setMagnitude(magnitude);
    }

}

