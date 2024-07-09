package view;

//import view.charactersView.EpsilonView;

import controller.Constants;
import controller.MouseController;
import model.BulletModel;
import model.charactersModel.SquarantineModel;
import model.collision.Collidable;
import model.collision.CollisionState;
import model.collision.Impactable;
import model.movement.Direction;
import model.movement.Movable;
import org.example.Main;
import view.charactersView.Drawable;
import view.charactersView.SquarantineView;

import javax.swing.*;

import java.awt.*;
import java.awt.geom.Point2D;

import static controller.Constants.*;
import static controller.Game.bufferedImageResult;
//import static controller.Game.squarantine;
import static controller.Utils.addVectors;
import static view.charactersView.Drawable.drawables;
import static view.charactersView.SquarantineView.squarantineViews;

//public final class myPanel extends JPanel  implements Collidable {
public final class MainPanel extends JPanel implements Collidable, Impactable, Movable {
    double anchor;
    double height = 600;
    double width = 600;
    Dimension panelSize = new Dimension((int) width, (int) height);
    Point2D vertex1;
    Point2D vertex2;
    Point2D vertex3;
    Point2D vertex4 ;
    Point2D[] vertices = new Point2D[4];

    private static MainPanel INSTANCE;
    public boolean moveRight = false;
    public boolean moveDown = false;
    public boolean moveLeft = false;
    public boolean moveUp = false;
    private double velocity = 0;
    private double acceleration = 0;
    private final MouseController mouseController;


    private MainPanel() {
        INSTANCE = this;
        mouseController = new MouseController();
        MainFrame frame = MainFrame.getINSTANCE();
        setSize(panelSize);
        setLocationToCenter(frame);

        vertex1 = new Point2D.Double((double) frame.getWidth() /2- (double) getWidth() /2, (double) frame.getHeight() /2- (double) getHeight() /2);
        vertex2 = addVectors(vertex1, new Point2D.Double(width, 0));
        vertex3 = addVectors(vertex2, new Point2D.Double(0, height));
        vertex4 = addVectors(vertex3, new Point2D.Double(-width, 0));
        vertices = new Point2D[]{vertex1, vertex2, vertex3, vertex4};

        frame.addMouseListener(mouseController);
        frame.addMouseMotionListener(mouseController);
        Collidable.collidables.add(this);
        frame.add(this);
    }
    public void setLocationToCenter(MainFrame glassFrame){
        setLocation(glassFrame.getWidth()/2-getWidth()/2,glassFrame.getHeight()/2-getHeight()/2);
    }

    public void adjustLocation(){
        setLocation((int) vertex1.getX(), (int) vertex1.getY());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        setBackground(Color.BLACK);


        for(Drawable obj: drawables){
            obj.draw(g);
        }
    }

    public static MainPanel getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new MainPanel();
        return INSTANCE;
    }

    @Override
    public boolean isCircular() {
        return false;
    }

    @Override
    public void setDirection(Direction direction) {

    }

    @Override
    public void bulletImpact(BulletModel bulletModel, Point2D collisionPoint) {
        double minDistance = Double.MAX_VALUE;
        Point2D closest = null;
        int edgeNumber = 5;
        for (int i=0; i<vertices.length; i++){

            Point2D temp = getClosestPointOnSegment(vertices[i],vertices[(i+1)%vertices.length], collisionPoint);
            double distance = temp.distance(collisionPoint);
            if (distance < minDistance) {
                minDistance = distance;
                edgeNumber = i;
            }
        }

        if (edgeNumber==0) moveUp=true;
        if (edgeNumber==1) moveRight = true;
        if (edgeNumber==2) moveDown=true;
        if (edgeNumber==3) moveLeft=true;

//        switch (edgeNumber) {
//            case 0:
//                moveUp = true;
//                break;
//            case 1:
//                moveRight = true;
//                break;
//            case 2:
//                moveDown = true;
//                break;
//            case 3:
//                moveLeft = true;
//                break;
//        }
        acceleration = 0.45; velocity = 0.1;
    }

//    public void shrink(){
//        if (height >400 || width > 400) {
//            double contraction = 0.5;
//            this.width -=contraction*2;
//            this.height -=contraction*2;
//            vertex1 = addVectors(vertex1, new Point2D.Double(contraction, contraction));
//            vertex2 = addVectors(vertex2, new Point2D.Double(-contraction, contraction));
//            vertex3 = addVectors(vertex3, new Point2D.Double(-contraction, -contraction));
//            vertex4 = addVectors(vertex4, new Point2D.Double(contraction, -contraction));
//            updateVertices();
//            adjustLocation();
//            panelSize = new Dimension((int) width, (int) height);
//            setSize(panelSize);
//        }
//
//    }


    public void verticalShrink(double contraction){
        if (height > 300){
//            double contraction = 0.25;
            this.height -=contraction*2;
            vertex1 = addVectors(vertex1, new Point2D.Double(0, contraction));
            vertex2 = addVectors(vertex2, new Point2D.Double(0, contraction));
            vertex3 = addVectors(vertex3, new Point2D.Double(0, -contraction));
            vertex4 = addVectors(vertex4, new Point2D.Double(0, -contraction));
            updateVertices();
            adjustLocation();
            panelSize = new Dimension((int) width, (int) height);
            setSize(panelSize);
        }
    }

    public void horizontalShrink(double contraction){
        if (width > 300){
//            double contraction = 0.25;
            this.width -=contraction*2;
            vertex1 = addVectors(vertex1, new Point2D.Double(contraction, 0));
            vertex2 = addVectors(vertex2, new Point2D.Double(-contraction, 0));
            vertex3 = addVectors(vertex3, new Point2D.Double(-contraction, 0));
            vertex4 = addVectors(vertex4, new Point2D.Double(contraction, 0));
            updateVertices();
            adjustLocation();
            panelSize = new Dimension((int) width, (int) height);
            setSize(panelSize);
        }
    }



    private void updateVertices(){
        vertices[0] = vertex1; vertices[1] = vertex2; vertices[2] = vertex3; vertices[3] = vertex4;
    }

    public void moveRight(){
        if (vertex3.getX()+1<FRAME_DIMENSION.getWidth()) {
            this.width += 3*velocity/4;
            vertex1 = addVectors(vertex1, new Point2D.Double(velocity/4, 0));
            vertex2 = addVectors(vertex2, new Point2D.Double(velocity, 0));
            vertex3 = addVectors(vertex3, new Point2D.Double(velocity, 0));
            vertex4 = addVectors(vertex4, new Point2D.Double(velocity/4, 0));
            updateVertices();
            adjustLocation();
            panelSize = new Dimension((int) width, (int) height);
            setSize(panelSize);
            MainFrame.getINSTANCE().repaint();
            MainPanel.getINSTANCE().repaint();
        } if (velocity<0)moveRight = false;
    }

    public void moveDown(){
        if (vertex3.getY()<FRAME_DIMENSION.getHeight()) {
            this.height += 3*velocity/4;
            vertex1 = addVectors(vertex1, new Point2D.Double(0, velocity/4));
            vertex2 = addVectors(vertex2, new Point2D.Double(0, velocity/4));
            vertex3 = addVectors(vertex3, new Point2D.Double(0, velocity));
            vertex4 = addVectors(vertex4, new Point2D.Double(0, velocity));
            updateVertices();
            adjustLocation();
            panelSize = new Dimension((int) width, (int) height);
            setSize(panelSize);
            MainFrame.getINSTANCE().repaint();
            MainPanel.getINSTANCE().repaint();


        } if (velocity<0) moveDown = false;
    }

    public void moveLeft(){
        if (vertex1.getX()>0) {
            this.width += 3*velocity/4;
            vertex1 = addVectors(vertex1, new Point2D.Double(-velocity, 0));
            vertex2 = addVectors(vertex2, new Point2D.Double(-velocity/4, 0));
            vertex3 = addVectors(vertex3, new Point2D.Double(-velocity/4, 0));
            vertex4 = addVectors(vertex4, new Point2D.Double(-velocity, 0));
            updateVertices();
            adjustLocation();
            panelSize = new Dimension((int) width, (int) height);
            setSize(panelSize);
            MainFrame.getINSTANCE().repaint();
            MainPanel.getINSTANCE().repaint();

        } if (velocity<0) moveLeft = false;
    }

    public void moveUp(){
        if (vertex1.getY()>0) {
            this.height += 3*velocity/4;
            vertex1 = addVectors(vertex1, new Point2D.Double(0, -velocity));
            vertex2 = addVectors(vertex2, new Point2D.Double(0, -velocity));
            vertex3 = addVectors(vertex3, new Point2D.Double(0, -velocity/4));
            vertex4 = addVectors(vertex4, new Point2D.Double(0, -velocity/4));
            updateVertices();
            adjustLocation();
            panelSize = new Dimension((int) width, (int) height);
            setSize(panelSize);
            MainFrame.getINSTANCE().repaint();
            MainPanel.getINSTANCE().repaint();

        }  if (velocity<0) moveUp = false;
    }


    public void panelMotion(){
        setVelocity(getAcceleration() + getVelocity());
        if (getVelocity()<4) {
            if (moveRight) moveRight();
            if (moveDown) moveDown();
            if (moveLeft) moveLeft();
            if (moveUp) moveUp();
        }
        if (getVelocity() > 4){
            setAcceleration(-0.45);
        }

        if (!moveRight&&!moveLeft){
            // don't change contraction coefficient!
            horizontalShrink(0.25);
        }

        if (!moveDown && !moveUp) {
            // don't change contraction coefficient!
            verticalShrink(0.25);
        }
    }

    public void expansion(){
        setVelocity(getAcceleration() + getVelocity());
        if (getVelocity()<4) {
            if (moveRight) moveRight();
            if (moveDown) moveDown();
            if (moveLeft) moveLeft();
            if (moveUp) moveUp();
        }
        if (getVelocity() > 4){
            setAcceleration(-0.45);
        }
    }


    @Override
    public Direction getDirection() {
        return null;
    }

    @Override
    public void move(Direction direction) {

    }

    @Override
    public void move() {

    }

    @Override
    public void friction() {

    }

    @Override
    public double getRadius() {
        return 0;
    }

    @Override
    public Point2D getAnchor() {
        return new Point2D.Double((double) MainFrame.getINSTANCE().getHeight() /2,  (double) MainFrame.getINSTANCE().getWidth() /2);
    }

    @Override
    public Point2D[] getVertices() {
        return vertices;
    }

    @Override
    public boolean isImpactInProgress() {
        return false;
    }

    @Override
    public void setImpactInProgress(boolean impactInProgress) {

    }

    @Override
    public void impact(CollisionState collisionState) {

    }



    @Override
    public void impact(Point2D normalVector, Point2D collisionPoint, Collidable polygon) {

    }

    @Override
    public double getImpactCoefficient(Point2D collisionRelativeVector) {
        return 0;
    }

    @Override
    public void banish() {

    }


    public double getVelocity() {
        return this.velocity;
    }

    public double getAcceleration() {
        return this.acceleration;
    }


    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }

    public MouseController getMouseController() {
        return mouseController;
    }
    public static void nullifyMainPanel(){
        INSTANCE = null;
    }
}