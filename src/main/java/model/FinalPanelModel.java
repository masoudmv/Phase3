package model;

import controller.Utils;
import model.charactersModel.BulletModel;
import model.collision.Coll;
import model.collision.Collidable;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.UUID;

import static controller.Controller.*;
import static controller.Utils.*;
import static controller.constants.Constants.FRAME_DIMENSION;

public class FinalPanelModel implements Coll {
    private String id;
    private ArrayList<Point2D> vertices;
    private ArrayList<Line2D> edges;

    protected Dimension size;
    protected Point2D location;
    public static ArrayList<FinalPanelModel> finalPanelModels = new ArrayList<>();
    private boolean isIsometric;
    private boolean isRigid;
    private boolean isStatic;





    private boolean moveUp = false;
    private boolean moveRight = false;
    private boolean moveDown = false;
    private boolean moveLeft = false;
    private double acceleration; private double velocity;
//    private double acceleration = 0.45; private  double velocity = 0.1;

    public FinalPanelModel(Point2D location, Dimension size) {
        this.id = UUID.randomUUID().toString();
        this.location = location;
        this.size = size;
        this.vertices = new ArrayList<>();

        updateVertices();
        finalPanelModels.add(this);
        createFinalPanelView(id, location, size);


        colls.add(this);
    }

    public void moveLocation(Point2D movement){
        this.location = addVectors(location, movement);
    }


    private void moveTopEdge(double shrinkCoefficient){
        Point2D movement = new Point2D.Double(0, shrinkCoefficient);
        this.location = addVectors(location, movement);
        this.size = new Dimension((int) size.getWidth(), (int) (size.getHeight() - shrinkCoefficient));
        updateVertices();
    }

    private void moveBottomEdge(double shrinkCoefficient){
        this.size = new Dimension((int) size.getWidth(), (int) (size.getHeight() - shrinkCoefficient));
        updateVertices();
    }

    private void moveLeftEdge(double shrinkCoefficient){
        Point2D movement = new Point2D.Double(shrinkCoefficient, 0);
        this.location = addVectors(location, movement);
        this.size = new Dimension((int) (size.getWidth() - shrinkCoefficient), (int) size.getHeight());
        updateVertices();

    }

    private void moveRightEdge(double shrinkCoefficient){
        this.size = new Dimension((int) (size.getWidth() - shrinkCoefficient), (int) size.getHeight());
        updateVertices();

    }

    public Dimension getSize() {
        return size;
    }

    public Point2D getLocation() {
        return location;
    }

    public String getId() {
        return id;
    }

    private void updateVertices() {
        vertices.clear();
        double x = location.getX();
        double y = location.getY();
        double width = size.getWidth();
        double height = size.getHeight();

        // Top-left corner (location)
        vertices.add(new Point2D.Double(x, y));
        // Top-right corner
        vertices.add(new Point2D.Double(x + width, y));
        // Bottom-right corner
        vertices.add(new Point2D.Double(x + width, y + height));
        // Bottom-left corner
        vertices.add(new Point2D.Double(x, y + height));
    }

    public void eliminate(){

    }

    public ArrayList<Point2D> getVertices() {
        return vertices;
    }

    private void handleCollisionWithBullet(Point2D intersection){
        int index = Utils.findPanelEdgeIndex(getVertices(), intersection);
        switch (index){
            case 0 -> moveUp = true;
            case 1 -> moveRight = true;
            case 2 -> moveDown = true;
            case 3 -> moveLeft = true;
        }
        acceleration = 0.45; velocity = 0.1;
    }

    private void handleCollisionWithRigidPanel(){

    }

    private void handleCollisionWithNonRigidPanel(){

    }

    public void moveRight(){
        if (vertices.get(1).getX() + 1 < FRAME_DIMENSION.getWidth()) {
            size.width += 3 * velocity / 4;
            Point2D movement = new Point2D.Double(velocity/4, 0);
            updateVertices();
            moveLocation(movement);
        } if (velocity < 0) moveRight = false;
    }

    public void moveDown(){
        if (vertices.get(2).getY() < FRAME_DIMENSION.getHeight()) {
            size.height += 3 * velocity / 4;
            Point2D movement = new Point2D.Double(0, velocity/4);
            moveLocation(movement);
            updateVertices();
        } if (velocity < 0) moveDown = false;

    }

    public void moveLeft(){
        if (vertices.get(0).getX() > 0) {
            size.width += 3 * velocity / 4;
            Point2D movement = new Point2D.Double(-velocity, 0);
            moveLocation(movement);
            updateVertices();
        } if (velocity<0) moveLeft = false;
    }

    public void moveUp(){
        if (vertices.get(0).getY() > 0) {
            size.height += 3*velocity/4;
            Point2D movement = new Point2D.Double(0, -velocity);
            moveLocation(movement);
            updateVertices();
        }  if (velocity<0) moveUp = false;
    }


    public void verticalShrink(double contraction){
        if (size.getHeight() > 300){
            size.height -= contraction * 2;
            Point2D movement = new Point2D.Double(0, contraction);
            moveLocation(movement);
            updateVertices();
//            updateEdges();
        }
    }

    public void horizontalShrink(double contraction){
        if (size.getWidth() > 300){
            size.width -= contraction * 2;
            Point2D movement = new Point2D.Double(contraction, 0);
            moveLocation(movement);
            updateVertices();
//            adjustLocation();
        }
    }


    public void panelMotion(){
        velocity = acceleration + velocity;
        if (velocity < 4) {
            if (moveRight) moveRight();
            if (moveDown) moveDown();
            if (moveLeft) moveLeft();
            if (moveUp) moveUp();
        }
        if (velocity > 4){
            acceleration = -0.45;
        }

//        moveRight = true;

        if (!moveRight && !moveLeft){
            // don't change contraction coefficient!
            horizontalShrink(0.25);
        }

        if (!moveDown && !moveUp) {
            // don't change contraction coefficient!
            verticalShrink(0.25);
        }
//        updateEdges();
    }


    @Override
    public Point2D getAnchor() {

        return null;
    }

    public void onCollision(Coll other, Point2D intersection){
        if (other instanceof BulletModel) handleCollisionWithBullet(intersection);
        if (other instanceof FinalPanelModel) handleCollisionWithBullet(intersection);
        if (other instanceof FinalPanelModel) handleCollisionWithBullet(intersection);
    }




    public void onCollision(Coll other) {
//        if (!(other instanceof BulletModel)) return;

        double minDistance = Double.MAX_VALUE;
        Point2D closest = null;
        for (int i=0;i<vertices.size();i++){

            Point2D temp = getClosestPointOnSegment(vertices.get(i),vertices.get((i+1)%vertices.size()), other.getAnchor()); // todo this calculation most be done with edges
            double distance = temp.distance(other.getAnchor());
            if (distance < minDistance) {
                minDistance = distance;
                closest = temp;
            }
        }


        Point2D intersection = closest;





        if (intersection.distance(other.getAnchor()) <= 5) { // todo where is get Radius?
            this.handleCollisionWithBullet(intersection);
            ((BulletModel) other).remove();
        }
    }

    @Override
    public boolean isCircular() {
        return false;
    }
}
