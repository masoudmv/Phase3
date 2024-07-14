package model.charactersModel.smiley;

import controller.Utils;
import model.FinalPanelModel;
import model.MyPolygon;
import model.charactersModel.EpsilonModel;
import model.charactersModel.GeoShapeModel;
import model.movement.Direction;
import org.example.GraphicalObject;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static controller.Utils.*;
import static controller.constants.SmileyConstants.BULLET_SPEED;
import static controller.constants.SmileyConstants.MAX_SPEED;
import static model.imagetools.ToolBox.getBufferedImage;

public class Hand extends GeoShapeModel {

    static BufferedImage image;
    public static ArrayList<Hand> hands = new ArrayList<>();
    private FinalPanelModel finalPanelModel;
    private double angularSpeed = 1.5;
    double angleToEpsilon;
    private boolean isMovingToDestination;
    private Point2D destination = EpsilonModel.getINSTANCE().getAnchor();
    private Point2D startPosition = getAnchor();
    private double speed = 0;
    private double acceleration;
    private double deceleration;
    private double dt = 1.0 / 60; // Assuming 60 FPS, so dt is approximately 1/60
    double a;

    private boolean isDecelerating = false;
    private double distance;
    private double halfwayDistance;
    private Point2D projectileCenter = EpsilonModel.getINSTANCE().getAnchor();


    private int pointerVertexIndex;





    public Hand(Point2D anchor, MyPolygon myPolygon) {
        super(anchor, image, myPolygon);
        hands.add(this);

        angleToEpsilon = findAngleToEpsilon();

        setFinalPanelModel();

        Point2D dir = relativeLocation(EpsilonModel.getINSTANCE().getAnchor(), getAnchor());
        direction = new Direction(dir);
        angle = 90; //


        distance = findDistance(getAnchor(), EpsilonModel.getINSTANCE().getAnchor());
        halfwayDistance = distance / 2;
        double t = 15; // Total time in frames (assuming 4 seconds at 60 FPS)
        acceleration = 4 * distance / (t * t);
        deceleration = -acceleration;
        MAX_SPEED = acceleration * t / 2;

        setPointerVertex();
        creatBulletFromPointingVertex();

    }

    private void setFinalPanelModel(){
        Point2D loc = new Point2D.Double(getAnchor().getX() - 80, getAnchor().getY() - 80);
        Dimension size = new Dimension(200, 200);
        finalPanelModel = new FinalPanelModel(loc, size);
    }

    private double findAngleToEpsilon(){
        Point2D right = new Point2D.Double(1, 0);
        Point2D left = new Point2D.Double(-1, 0);
        Point2D center = EpsilonModel.getINSTANCE().getAnchor();
        Point2D vecToEpsilon = relativeLocation(getAnchor(), center);
        double res = 0;
        if (vecToEpsilon.getY() < 0) res = findAngleBetweenTwoVectors(right, vecToEpsilon);
        if (vecToEpsilon.getY() == 0) res = Math.PI;
        if (vecToEpsilon.getY() > 0) res = findAngleBetweenTwoVectors(left, vecToEpsilon) + Math.PI;
        return Math.toDegrees(res);
    }


    private Point2D getPointingVertexPoint2D(){
        double x = myPolygon.xpoints[pointerVertexIndex]-25;
        double y = myPolygon.ypoints[pointerVertexIndex]-25;
        return new Point2D.Double(x, y);
    }

    private void creatBulletFromPointingVertex(){
        BufferedImage ba = SmileyBullet.loadImage();
        GraphicalObject bos = new GraphicalObject(ba);
        MyPolygon pl = bos.getMyBoundingPolygon();
        Point2D startPos = getPointingVertexPoint2D();
        new SmileyBullet(startPos, pl).setDirection(findBulletDirection(startPos));
    }

    private Direction findBulletDirection(Point2D startPos){
        Point2D dest = EpsilonModel.getINSTANCE().getAnchor();
        Point2D vec = relativeLocation(dest, startPos);
        Direction direction = new Direction(vec);
        direction.setMagnitude(BULLET_SPEED);
        return direction;
    }


    public void move() {
        updateDirection();
        move(direction);
    }

    void move(Direction direction) {
        if (direction == null) return;
        Point2D movement = multiplyVector(direction.getNormalizedDirectionVector(), direction.getMagnitude());
        movePolygon(movement);
        finalPanelModel.moveLocation(movement);
    }

    public void updateDirection() {
        if (!isDecelerating) {
            speed += acceleration * dt;
            if (speed > MAX_SPEED) {
                speed = MAX_SPEED;
            }
            if (findDistance(getAnchor(), startPosition) >= halfwayDistance) {
                isDecelerating = true;
            }
        } else {
            speed += deceleration * dt;
            if (speed < 0) {
                speed = 0;
                isMovingToDestination = false; // Stop moving when speed is 0
            }
        }
        direction.setMagnitude(speed);
    }


    public void rot() {

//        creatBulletFromPointingVertex();

        // Normal movement logic (circular motion)
        angleToEpsilon += angularSpeed;
        if (angleToEpsilon >= 360) angleToEpsilon -= 360; // Keep angle within 0-359 degrees
        Point2D center = projectileCenter;

        // Calculate the current radius
        double radius = findDistance(getAnchor(), center);

        // Calculate new position based on the updated angle
        double radians = Math.toRadians(angleToEpsilon);
        double newX = center.getX() + radius * Math.cos(radians);
        double newY = center.getY() + radius * Math.sin(radians);

        // Set the new position
        Point2D newAnchor = new Point2D.Double(newX, newY);

        // Update the polygon's position
        Point2D movement = new Point2D.Double(newX - getAnchor().getX(), newY - getAnchor().getY());
        finalPanelModel.moveLocation(movement);

        setAnchor(newAnchor);

        // Rotate the polygon to face the center
        angle += 1.5;
//        System.out.println(angularSpeed);
        rotate(angularSpeed
        );
    }

    // Method to calculate the angle so that the same face of the object points towards the center
    public static double getAngleTowardsCenter(Point2D center, Point2D objectPosition) {
        // Calculate the vector from center to object
        double dx = objectPosition.getX() - center.getX();
        double dy = objectPosition.getY() - center.getY();

        // Calculate the angle (in radians) using atan2
        double angle = Math.atan2(dy, dx);

        return Math.toDegrees(angle);
    }



    public void rotate(double angle){
        setMyPolygon(Utils.rotateMyPolygon(myPolygon, angle, getAnchor()));
    }

    public static BufferedImage loadImage() {
        Image img = new ImageIcon("./src/hand.png").getImage();
        Hand.image = getBufferedImage(img);
        return Hand.image;
    }

    @Override
    public void setMyPolygon(MyPolygon myPolygon) {
        super.myPolygon = myPolygon;
    }

    @Override
    public void eliminate() {

    }

    public void moveTo(Point2D destination) {
        this.destination = destination;
        this.startPosition = getAnchor();
        this.isMovingToDestination = true;
        this.isDecelerating = false;
        this.speed = 0; // Reset speed at the start of the movement
    }

    private void setPointerVertex(){
        double yMin = Double.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < myPolygon.npoints; i++) {
            if (myPolygon.ypoints[i] < yMin){
                yMin = myPolygon.ypoints[i];
                index = i;
            }
        }
        pointerVertexIndex = index;
    }
}
