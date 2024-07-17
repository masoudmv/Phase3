package model.charactersModel.smiley;

import controller.Game;
import controller.Utils;
import model.FinalPanelModel;
import model.MyPolygon;
import model.charactersModel.EpsilonModel;
import model.charactersModel.GeoShapeModel;
import model.charactersModel.SmileyBullet;
import model.collision.Collidable;
import model.movement.Direction;
import org.example.GraphicalObject;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static controller.Utils.*;
import static controller.constants.SmileyConstants.*;
import static model.imagetools.ToolBox.getBufferedImage;

public class Hand extends GeoShapeModel implements Collidable {
    static BufferedImage image;

    public static ArrayList<Hand> hands = new ArrayList<>();
    protected static MyPolygon pol;
    private FinalPanelModel finalPanelModel;
    private boolean isMovingToDestination;
    private boolean isProjecting;
    private boolean rotating;
    private double totalRotationAngle;
    private double angularSpeed = 5;
    double angleToEpsilon;
    private Point2D destination;
    private double speed = 0;
    private double acceleration;
    private double deceleration;
    private boolean isDecelerating = false;
    private double distance;
    private double halfwayDistance;
    private int pointerVertexIndex;
    private double targetAngle;  // Target angle to rotate to
    private double lastShotBulletTime = 0;
    private Point2D beforeSlapPosition;
    private double lastSlapTime = 0;
    private double squeezing;




    private Point2D startPosition = getAnchor();
    private Point2D projectileCenter = EpsilonModel.getINSTANCE().getAnchor();
    private double dt = 1.0 / 60; // Assuming 60 FPS, so dt is approximately 1/60

    public Hand(Point2D anchor) {
        super(anchor, image, Hand.pol);
        hands.add(this);
        angleToEpsilon = findAngleToEpsilon();
        setFinalPanelModel();
        setPointerVertex();
        collidables.add(this);
        finalPanelModel.setRigid(true);
        finalPanelModel.setIsometric(true);
//        initializeSlap();
//        initializeRotation(pointToEpsilon());
//        initializeProjectile();
    }

    public  Hand(Point2D anchor, MyPolygon pol) {
        super(anchor, LeftHand.image, pol);
        hands.add(this);
        angleToEpsilon = findAngleToEpsilon();
        setFinalPanelModel();
        setPointerVertex();
        collidables.add(this);

        finalPanelModel.setRigid(true);
        finalPanelModel.setIsometric(true);


//        initializeSlap();


//        initializeRotation(pointToEpsilon());
//        initializeSlap();

//        initializeProjectile();
    }

    public void squeeze(){
//        System.out.println("SQU");
        if (isRightHand()) {
            squeezing = Game.ELAPSED_TIME;
            finalPanelModel.setRigid(true);
//            finalPanelModel.setIsometric(true);
            FinalPanelModel epsilonPanel = EpsilonModel.getINSTANCE().localPanel;
            double x = epsilonPanel.getLocation().getX() + epsilonPanel.getSize().getWidth() + finalPanelModel.getSize().getWidth()/2;
            double y = epsilonPanel.getLocation().getY() + epsilonPanel.getSize().getHeight()/2;
            moveTo(new Point2D.Double(x, y));

        }

        else {
            squeezing = Game.ELAPSED_TIME;
            finalPanelModel.setRigid(true);
//            finalPanelModel.setIsometric(true);
            FinalPanelModel epsilonPanel = EpsilonModel.getINSTANCE().localPanel;
            double x = epsilonPanel.getLocation().getX()  - finalPanelModel.getSize().getWidth()/2;
            double y = epsilonPanel.getLocation().getY() + epsilonPanel.getSize().getHeight()/2;
            moveTo(new Point2D.Double(x, y));
        }
    }

    public boolean isAlive(){
        //TODO complete methode
        return true;
    }

    protected boolean isRightHand(){
        return true;
    }

    public FinalPanelModel getFinalPanelModel() {
        return finalPanelModel;
    }

    private void initializeSlap(){
        initializeRotation(pointToEpsilon());
        beforeSlapPosition = getAnchor();
        moveTo(EpsilonModel.getINSTANCE().getAnchor());
    }


    public void moveTo(Point2D destination) {
        this.destination = destination;
        this.startPosition = getAnchor();
        this.isMovingToDestination = true;
        this.isDecelerating = false;
        this.speed = 0; // Reset speed at the start of the movement

        // Calculate the total distance to the destination
        distance = findDistance(getAnchor(), destination);
        halfwayDistance = distance / 2;

        double t = 8; // Total time in frames (assuming 4 seconds at 60 FPS)
        acceleration = 4 * distance / (t * t);
        deceleration = -acceleration;
        MAX_SPEED = acceleration * t / 2;

        Point2D dir = relativeLocation(destination, getAnchor());
        direction = new Direction(dir);
        direction.setMagnitude(speed);
    }

    private void setFinalPanelModel() {
        Dimension size = new Dimension(300, 300);
        Point2D loc = new Point2D.Double(getAnchor().getX() - size.getWidth()/2, getAnchor().getY() - size.getHeight()/2);
        finalPanelModel = new FinalPanelModel(loc, size);
        finalPanelModel.setRigid(false);
    }

    private double findAngleToEpsilon() {
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

    private Point2D getPointingVertexPoint2D() {
        double x = myPolygon.xpoints[pointerVertexIndex] - 25;
        double y = myPolygon.ypoints[pointerVertexIndex] - 25;
        return new Point2D.Double(x, y);
    }

    private void creatBulletFromPointingVertex() {
        double now = Game.ELAPSED_TIME;
        if (now - lastShotBulletTime < 0.5) return;
        BufferedImage ba = SmileyBullet.loadImage();
        GraphicalObject bos = new GraphicalObject(ba);
        MyPolygon pl = bos.getMyBoundingPolygon();
        Point2D startPos = getPointingVertexPoint2D();
        new SmileyBullet(startPos).setDirection(findBulletDirection(startPos));
        lastShotBulletTime = now;
    }

    private Direction findBulletDirection(Point2D startPos) {
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

    private void initializeRotation(double angle){
        rotating = true;
        rotateTo(angle);
    }

    public void updateDirection() {

        double now = Game.ELAPSED_TIME;

        if (now - squeezing > SQUEEZE_DURATION && squeezing != -1) {
            if (isRightHand()) moveTo(new Point2D.Double(1500, 400));
            if (!isRightHand()) moveTo(new Point2D.Double(500, 401));
            finalPanelModel.setRigid(false);
            squeezing = -1;
        }

        // adjust pointer finger toward epsilon
        if (rotating) {
            updateRotation();
        }

        else if (isProjecting) {
            projectile();
        }

        else if (isMovingToDestination || squeezing != -1){
            double currentDistance = findDistance(getAnchor(), destination);

            if (!isDecelerating) {
                speed += acceleration * dt;
                if (speed > MAX_SPEED) {
                    speed = MAX_SPEED;
                }
                if (currentDistance <= halfwayDistance) {
                    isDecelerating = true;
                }
            } else {
                speed += deceleration * dt;
                if (speed < 0 || currentDistance <= 0) {
                    speed = 0;
                    isMovingToDestination = false; // Stop moving when speed is 0 or destination is reached
                }
            }


            direction.setMagnitude(speed);
        }

//        else if (squeezing) return;
        else if (!isMovingToDestination && beforeSlapPosition != null){
            moveTo(beforeSlapPosition);
            beforeSlapPosition = null;
        }

        else {

            if (now - lastSlapTime > 5){
                double distance = findDistance(getAnchor(), EpsilonModel.getINSTANCE().getAnchor());
                if (distance < 500) {
                    initializeSlap();
                    lastSlapTime = now;
                }
            }
        }
    }

    private void initializeProjectile() {
//        System.out.println("TRUUUUUUUUUUU");
        totalRotationAngle = 0;
        isProjecting = true;
    }

    public void projectile() {
        creatBulletFromPointingVertex();
        if (totalRotationAngle >= 360) {
            isProjecting = false;
            totalRotationAngle = 0;
            return;
        }

        angleToEpsilon += angularSpeed;
        if (angleToEpsilon >= 360) angleToEpsilon -= 360; // Keep angle within 0-359 degrees
        Point2D center = projectileCenter;


        // Calculate the current radius
        double radius = findDistance(getAnchor(), center);

        // Calculate new position based on the updated angle
        double radians = Math.toRadians(angleToEpsilon);
        double newX = center.getX() + radius * Math.cos(radians);
        double newY = center.getY() - radius * Math.sin(radians);

        // Set the new position
        Point2D newAnchor = new Point2D.Double(newX, newY);

        // Update the polygon's position
        Point2D movement = new Point2D.Double(newX - getAnchor().getX(), newY - getAnchor().getY());
        finalPanelModel.moveLocation(movement);


        setAnchor(newAnchor);

        // Rotate the polygon to face the center
        angle -= angularSpeed;
        rotate(-angularSpeed);

        totalRotationAngle += angularSpeed;
    }

    public static double getAngleTowardsCenter(Point2D center, Point2D objectPosition) {
        // Calculate the vector from center to object
        double dx = objectPosition.getX() - center.getX();
        double dy = objectPosition.getY() - center.getY();

        // Calculate the angle (in radians) using atan2
        double angle = Math.atan2(dy, dx);

        return Math.toDegrees(angle);
    }

    public void rotate(double angle) {
        setMyPolygon(Utils.rotateMyPolygon(myPolygon, angle, getAnchor()));
    }

    public static BufferedImage loadImage() {
        Image img = new ImageIcon("./src/hand.png").getImage();
        Hand.image = getBufferedImage(img);
        GraphicalObject bowser = new GraphicalObject(image);
        pol = bowser.getMyBoundingPolygon();
        return Hand.image;
    }

    private double pointToEpsilon() {
        Point2D center = EpsilonModel.getINSTANCE().getAnchor();
        Point2D handVec = new Point2D.Double(1, 0);
        Point2D toEpsilonVec = relativeLocation(center, getAnchor());

        double dotProduct = handVec.getX() * toEpsilonVec.getX() + handVec.getY() * toEpsilonVec.getY();
        double crossProduct = handVec.getX() * toEpsilonVec.getY() - handVec.getY() * toEpsilonVec.getX();

        double angleDifference = Math.toDegrees(Math.atan2(crossProduct, dotProduct));

        return angleDifference;
    }


    @Override
    public void setMyPolygon(MyPolygon myPolygon) {
        super.myPolygon = myPolygon;
    }

    @Override
    public void eliminate() {
    }

    private void setPointerVertex() {
        double yMin = Double.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < myPolygon.npoints; i++) {
            if (myPolygon.ypoints[i] < yMin) {
                yMin = myPolygon.ypoints[i];
                index = i;
            }
        }
        pointerVertexIndex = index;
    }

    @Override
    public boolean isCircular() {
        return false;
    }

    @Override
    public void onCollision(Collidable other, Point2D intersection) {
    }

    @Override
    public void onCollision(Collidable other) {
    }

    // New method to set the target angle
    public void rotateTo(double targetAngle) {
        this.targetAngle = targetAngle;
    }

    // New method to update the rotation in each frame
    private void updateRotation() {
        if (Math.abs(angle- targetAngle) > 0.001) { //check this out
//            System.out.println(angle);
//            System.out.println(angle-targetAngle);
            double angleDifference = targetAngle - angle;
            System.out.println(angleDifference);

            // Normalize the angle to the range [-180, 180]
            angleDifference = (angleDifference) % 360 ;

            // Calculate the step for this frame
            double rotationStep = Math.min(Math.abs(angleDifference), angularSpeed) * Math.signum(angleDifference);

            // Update the current angle
            angle += rotationStep;

            // Apply the rotation to the polygon
            rotate(rotationStep);
        }  else rotating = false;
    }
}
