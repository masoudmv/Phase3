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
import java.util.Random;

import static controller.Utils.*;
import static controller.constants.EntityConstants.*;
import static controller.constants.SmileyConstants.*;
import static model.charactersModel.smiley.Smiley.smilies;
import static model.imagetools.ToolBox.getBufferedImage;

public class Hand extends GeoShapeModel implements Collidable {
    static BufferedImage image;

    public static ArrayList<Hand> hands = new ArrayList<>();
    protected static MyPolygon pol;
    private FinalPanelModel finalPanelModel;
    private MovementState movementState;
    private RotationState rotationState;
    private ProjectileState projectileState;
    public Point2D beforeSlapPosition;
    private int pointerVertexIndex;  // Added missing field

    public static boolean slapInProgress = false;
    public  boolean squeezeInProgress = false;
    public boolean projectileInProgress = false;

    public static double lastSlapTime = 0;
    public double lastSqueezeTime = -20;
    public double lastProjectileTime = -20;

    public Hand(Point2D anchor) {
        super(anchor, image, Hand.pol);
        init();
//        initializeProjectile();
    }

    public Hand(Point2D anchor, MyPolygon pol) {
        super(anchor, LeftHand.image, pol);
        init();
//        initializeProjectile();

    }

    private void init() {
        hands.add(this);
        setFinalPanelModel();
        setPointerVertex();
        collidables.add(this);
        finalPanelModel.setIsometric(true);
        movementState = new MovementState();
        rotationState = new RotationState();
        projectileState = new ProjectileState();
    }

    public void initializeSlap() {
        Point2D eAnchor = EpsilonModel.getINSTANCE().getAnchor();
        if (findDistance(getAnchor(), eAnchor) > 400) return;
        for(Smiley smiley : smilies){
            if (smiley.getAnchor().distance(eAnchor) < 300) return;
        }

        if (projectileInProgress || squeezeInProgress || slapInProgress) return;
        slapInProgress = true;
        lastSlapTime = Game.ELAPSED_TIME;
        rotationState.startRotation(pointToEpsilon());
        beforeSlapPosition = new Point2D.Double(getAnchor().getX(), getAnchor().getY()); // Store exact position
        moveTo(EpsilonModel.getINSTANCE().getAnchor());
    }


    public void initializeProjectile() {
        if (squeezeInProgress || slapInProgress || projectileInProgress) return;
        if (movementState.isMovingToDestination) return;
        if (rotationState.rotating) return;
        lastProjectileTime = Game.ELAPSED_TIME;
        projectileInProgress = true;
        rotationState.startRotation(pointToEpsilon());
        beforeSlapPosition = new Point2D.Double(getAnchor().getX(), getAnchor().getY()); // Store exact position
        projectileState.start(this.anchor);
    }

    public void initializeSqueeze() {
        if (projectileInProgress || squeezeInProgress || slapInProgress) return;
        if (movementState.isMovingToDestination) return;
        if (rotationState.rotating) return;

        lastSqueezeTime = Game.ELAPSED_TIME;
        squeezeInProgress = true;
        finalPanelModel.setRigid(true);
        beforeSlapPosition = new Point2D.Double(getAnchor().getX(), getAnchor().getY()); // Store exact position
        FinalPanelModel epsilonPanel = EpsilonModel.getINSTANCE().getLocalPanel();
        double x = epsilonPanel.getLocation().getX() + (isRightHand() ?
                epsilonPanel.getSize().getWidth() + finalPanelModel.getSize().getWidth()/2 : -finalPanelModel.getSize().getWidth()/2);
        double y = epsilonPanel.getLocation().getY() + epsilonPanel.getSize().getHeight()/2;
        moveTo(new Point2D.Double(x, y));

//
//        moveTo(new Point2D.Double(isRightHand() ? 1500 : 500, 400));
//        finalPanelModel.setRigid(false);
    }






    public boolean isAlive() {
        // TODO: Implement method
        return true;
    }

    protected boolean isRightHand() {
        return true;
    }

    public FinalPanelModel getFinalPanelModel() {
        return finalPanelModel;
    }

    public void moveTo(Point2D destination) {
        movementState.startMove(destination, getAnchor());
        direction = new Direction(relativeLocation(destination, getAnchor()));
        direction.setMagnitude(movementState.getSpeed());
        // Move immediately to ensure initial position is updated
        Point2D movement = multiplyVector(direction.getNormalizedDirectionVector(), direction.getMagnitude());
        movePolygon(movement);
        finalPanelModel.moveLocation(movement);
    }


    private void setFinalPanelModel() {
        Dimension size = new Dimension(300, 300);
        Point2D loc = new Point2D.Double(getAnchor().getX() - size.getWidth()/2, getAnchor().getY() - size.getHeight()/2);
        finalPanelModel = new FinalPanelModel(loc, size);
        finalPanelModel.setRigid(false);
    }

    private Point2D getPointingVertexPoint2D() {
        double x = myPolygon.xpoints[pointerVertexIndex] - 25;
        double y = myPolygon.ypoints[pointerVertexIndex] - 25;
        return new Point2D.Double(x, y);
    }

    private void creatBulletFromPointingVertex() {
        if (Game.ELAPSED_TIME - projectileState.getLastShotBulletTime() < 0.5) return;
        BufferedImage ba = SmileyBullet.loadImage();
        GraphicalObject bos = new GraphicalObject(ba);
        MyPolygon pl = bos.getMyBoundingPolygon();
        Point2D startPos = getPointingVertexPoint2D();
        new SmileyBullet(startPos).setDirection(findBulletDirection(startPos));
        projectileState.updateLastShotBulletTime(Game.ELAPSED_TIME);
    }

    private Direction findBulletDirection(Point2D startPos) {
        Point2D dest = EpsilonModel.getINSTANCE().getAnchor();
        Point2D vec = relativeLocation(dest, startPos);
        Direction direction = new Direction(vec);
        direction.setMagnitude(BULLET_SPEED);
        return direction;
    }


    @Override
    public void update() {
        updateDirection();
        move(direction);
    }

    private void updateActions(){
        double now = Game.ELAPSED_TIME;
        if (squeezeInProgress && now - lastSqueezeTime > SMILEY_SQUEEZE_DURATION.getValue())  {
            squeezeInProgress = false;
            finalPanelModel.setRigid(false);
        }
        if (projectileInProgress && now - lastProjectileTime > SMILEY_PROJECTILE_DURATION.getValue()) {
            projectileInProgress = false;
        }
        if (slapInProgress && now - lastSlapTime > SMILEY_SLAP_DURATION.getValue()) {
            slapInProgress = false;
        }

    }

    private void move(Direction direction) {
        if (direction == null) return;
        Point2D movement = multiplyVector(direction.getNormalizedDirectionVector(), direction.getMagnitude());
        movePolygon(movement);
        finalPanelModel.moveLocation(movement);
    }


    public void projectile() {

        creatBulletFromPointingVertex();
        if (projectileState.updateRotation()) {

            Point2D center = projectileState.getCenter();
            double radius = findDistance(getAnchor(), center);
            double radians = Math.toRadians(projectileState.getAngleToEpsilon());
            double newX = center.getX() + radius * Math.cos(radians);
            double newY = center.getY() - radius * Math.sin(radians);
            Point2D newAnchor = new Point2D.Double(newX, newY);
            Point2D movement = new Point2D.Double(newX - getAnchor().getX(), newY - getAnchor().getY());
            finalPanelModel.moveLocation(movement);
            setAnchor(newAnchor);
            rotate(-projectileState.getAngularSpeed());  // Rotate method used here
        }
    }





    public void updateDirection() {

        updateActions();


//    System.out.println(beforeSlapPosition);

//        if (!slapInProgress) {
//            checkForSqueezeCoolDown();
//            checkForProjectileCoolDown();
//        }

        double now = Game.ELAPSED_TIME;



//        if (now -  lastSqueezeTime > SQUEEZE_DURATION && lastSqueezeTime != -1) {
//            moveTo(new Point2D.Double(isRightHand() ? 1500 : 500, 400));
//            finalPanelModel.setRigid(false);
//            lastSqueezeTime = -1;
//        }
//        if (now - lastSqueezeTime > 5){
//            squeezeInProgress = true;
//        }

//        System.out.println(beforeSlapPosition);
//
//
//        if (now - lastSqueezeTime > 5 && squeezeInProgress){
//            squeezeInProgress = false;
//            moveTo(beforeSlapPosition);
//        }

//        else if (squeezeInProgress && now - lastSqueezeTime > 2){
//            System.out.println("SSSSSSSSSSS");
//        }

        if (rotationState.isRotating()) {
            rotationState.updateRotation();
        } else if (projectileState.isProjecting()) {
            projectile();
        }

        else if (movementState.isMoving()) {
//            System.out.println("Moving ...");
            movementState.updateSpeed();
            direction.setMagnitude(movementState.getSpeed());
            move(direction); // Ensure this method is called to apply movement
        }
//
        else if (beforeSlapPosition != null) {
            if (squeezeInProgress && now - lastSqueezeTime < SMILEY_SQUEEZE_DURATION.getValue()-3){

            } else {
                Point2D currentPos = getAnchor();
                Point2D destination = beforeSlapPosition;
                if (currentPos.distance(destination) > 1) { // Adding a small threshold
                    moveTo(beforeSlapPosition); // Continue moving to exact position
                } else if (!squeezeInProgress) {
                    setAnchor(destination); // Set exact position to avoid overshoot
                    beforeSlapPosition = null;
                    lastSlapTime = now;
                }
            }
        }


        else if (now - lastSlapTime > SMILEY_SLAP_COOLDOWN.getValue() && !slapInProgress) {
            if (findDistance(getAnchor(), EpsilonModel.getINSTANCE().getAnchor()) < 700) {
//                System.out.println("initiate ...");
                initializeSlap();
//                movementState.setMovingToDestination(false);
                lastSlapTime = now;
            }
        }






    }

    private double pointToEpsilon() {
        Point2D center = EpsilonModel.getINSTANCE().getAnchor();
        Point2D handVec = new Point2D.Double(1, 0);
        Point2D toEpsilonVec = relativeLocation(center, getAnchor());
        double dotProduct = handVec.getX() * toEpsilonVec.getX() + handVec.getY() * toEpsilonVec.getY();
        double crossProduct = handVec.getX() * toEpsilonVec.getY() - handVec.getY() * toEpsilonVec.getX();
        return Math.toDegrees(Math.atan2(crossProduct, dotProduct));
    }

    public static BufferedImage loadImage() {
        Image img = new ImageIcon("./client/src/hand.png").getImage();
        Hand.image = getBufferedImage(img);
        GraphicalObject bowser = new GraphicalObject(image);
        pol = bowser.getMyBoundingPolygon();
        return Hand.image;
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
        pointerVertexIndex = index;  // Set pointerVertexIndex here
    }

    @Override
    public boolean isCircular() {
        return false;
    }

    @Override
    public void onCollision(Collidable other, Point2D intersection) {
    }

    @Override
    public void onCollision(Collidable other, Point2D coll1, Point2D coll2) {

    }


    // Rotate method added here
    public void rotate(double angle) {
        // Implementation for rotating the hand by the given angle
        // This can be specific to how the rotation should affect the hand's state
        this.angle += angle;
        setMyPolygon(Utils.rotateMyPolygon(myPolygon, angle, getAnchor()));

    }

    // New method to set the target angle
    public void rotateTo(double targetAngle) {
        rotationState.setTargetAngle(targetAngle);
    }

    private class MovementState {
        private boolean isMovingToDestination;
        private boolean isDecelerating = false;
        private double speed = 0;
        private double acceleration;
        private double deceleration;
        private double distance;
        private double halfwayDistance;
        private Point2D destination;
        private double dt = 1.0 / 60;
        private double MAX_SPEED;

        public void startMove(Point2D destination, Point2D anchor) {
            this.destination = destination;
            isMovingToDestination = true;
            isDecelerating = false;
            distance = findDistance(anchor, destination);
            halfwayDistance = distance / 2;
            double t = 10; // Total time in frames (assuming 4 seconds at 60 FPS)
            acceleration = 4 * distance / (t * t);
            deceleration = -acceleration;
            MAX_SPEED = acceleration * t / 2;
        }

        public void updateSpeed() {
            double currentDistance = findDistance(getAnchor(), destination);
            if (!isDecelerating) {
//                System.out.println("AAAAAAAAAAAA");
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
                    isMovingToDestination = false;
                }
            }
        }

        public double getSpeed() {
            return speed;
        }

        public boolean isMoving() {
            return isMovingToDestination;
        }

        public void setMovingToDestination(boolean movingToDestination) {
            isMovingToDestination = movingToDestination;
        }
    }

    private class RotationState {
        private boolean rotating;
        private double totalRotationAngle;
        private double angularSpeed = 5;
        private double targetAngle;

        public void startRotation(double angle) {
            rotating = true;
            rotateTo(angle);
        }

        public void updateRotation() {
            double difference = Math.abs(angle - targetAngle);
            if ( Math.abs(difference % 360 - 360) > 1  &&  Math.abs(difference % 360) > 1 )  {
//                System.out.println("updating rotation");
                System.out.println("angle: "+ angle);
                System.out.println("target: "+ targetAngle);
                double angleDifference = (targetAngle - angle) % 360;
                double rotationStep = Math.min(Math.abs(angleDifference), angularSpeed) * Math.signum(angleDifference);
//                angle += rotationStep;
                rotate(rotationStep);
            } else {
                System.out.println("Seting rotation to false");
                rotating = false;
            }
        }

        public boolean isRotating() {
            return rotating;
        }

        public void setTargetAngle(double targetAngle) {
            this.targetAngle = targetAngle;
        }
    }

}
