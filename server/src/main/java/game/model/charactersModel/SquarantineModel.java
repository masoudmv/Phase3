package game.model.charactersModel;

import game.controller.UserInterfaceController;
import game.controller.Utils;
import shared.constants.Constants;
import shared.constants.EntityConstants;
import shared.Model.MyPolygon;
import game.model.charactersModel.blackOrb.Orb;
import game.model.collision.Collidable;
import game.model.collision.CollisionState;
import game.model.collision.Impactable;
import game.model.movement.Direction;
import game.model.movement.Movable;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static shared.Model.imagetools.ToolBox.getBufferedImage;
import static shared.constants.EntityConstants.SQUARANTINE_COLLECTIBLES_XP;
import static shared.constants.EntityConstants.SQUARANTINE_NUM_OF_COLLECTIBLES;

public class SquarantineModel extends GeoShapeModel implements Movable, Collidable, Impactable {
    static BufferedImage image;
    double nextDash = Double.MAX_VALUE;




    private boolean impactInProgress = false;
    public double impactMaxVelocity;
    private double angularVelocity;
    private double angularAcceleration;
    public static List<SquarantineModel> squarantineModels = new ArrayList<>();
    private static double edgeLength;


    public SquarantineModel(Point2D anchor) {
        super(anchor, image, new MyPolygon(new double[4], new double[4], 4), true);
        initMyPolygon();
        updateNextDashTime();
        squarantineModels.add(this);
        collidables.add(this);
        movables.add(this);
        impactables.add(this);
        this.health = 10;
        UserInterfaceController.createSquarantineView(id);

    }

    private void initMyPolygon() {
        double halfEdgeLength = edgeLength / 2;
        double x = anchor.getX();
        double y = anchor.getY();

        Point2D.Double[] vertices = new Point2D.Double[4];
        vertices[0] = new Point2D.Double(x - halfEdgeLength, y - halfEdgeLength); // Top-left
        vertices[1] = new Point2D.Double(x + halfEdgeLength, y - halfEdgeLength); // Top-right
        vertices[2] = new Point2D.Double(x + halfEdgeLength, y + halfEdgeLength); // Bottom-right
        vertices[3] = new Point2D.Double(x - halfEdgeLength, y + halfEdgeLength); // Bottom-left

        myPolygon.setVertices(vertices);
    }


    public static BufferedImage loadImage() {
        Image img = new ImageIcon("./client/src/squarantine.png").getImage();
        SquarantineModel.image = getBufferedImage(img);
        edgeLength = 35;
        return SquarantineModel.image;
    }

//    public String getId() {
//        return id;
//    }

    @Override
    public boolean isImpactInProgress() {
        return impactInProgress;
    }
    @Override
    public void setImpactInProgress(boolean impactInProgress) {
        this.impactInProgress = impactInProgress;
    }

    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public Direction getDirection() {
        return this.direction;
    }

//    @Override
//    public Point2D getAnchor() {
//        return anchor;
//    }

    public void setAnchor(Point2D anchor) {
        this.anchor = anchor;
    }

    @Override
    public void setMyPolygon(MyPolygon myPolygon) {
        // todo
    }

    @Override
    public void impact(CollisionState collisionState) {
        Point2D collisionPoint = collisionState.collisionPoint;
        Point2D collisionRelativeVector = Utils.relativeLocation(this.getAnchor(), collisionPoint);
        double impactCoefficient = getImpactCoefficient(collisionRelativeVector);
//        impactCoefficient *= 2;
        Point2D impactVector = Utils.normalizeVector(Utils.relativeLocation(this.getAnchor(), collisionState.collisionPoint));
        impactVector = Utils.multiplyVector(impactVector ,impactCoefficient);
        Point2D r2 = Utils.addVectors(this.getDirection().getNormalizedDirectionVector(), impactVector);
        Direction direction = new Direction(Utils.normalizeVector(r2));
        this.setDirection(direction);

    }


    @Override
    public void impact(Point2D normalVector, Point2D collisionPoint, Collidable polygon) {
        Point2D collisionRelativeVector = Utils.relativeLocation(this.getAnchor(), collisionPoint);
        double impactCoefficient = getImpactCoefficient(collisionRelativeVector);
        Point2D impactVector = Utils.relativeLocation(collisionPoint, polygon.getAnchor());
        if(!(polygon instanceof  EpsilonModel)) impactVector = Utils.multiplyVector(Utils.normalizeVector(impactVector) ,1);
        impactVector = Utils.addVectors(impactVector, getDirection().getNormalizedDirectionVector());
        impactVector = Utils.multiplyVector(impactVector ,impactCoefficient);
        this.setDirection(new Direction(Utils.normalizeVector(impactVector)));

        // Angular motion
        setAngularMotion(collisionPoint, polygon, 34000);
        createImpactWave(this, polygon, collisionPoint);
    }

    //todo duplicated version. needs to be eiminated ...
    public void impact(Point2D normalVector, Point2D collisionPoint, Collidable polygon, double inertia) {
        double distanceByEpsilon = getAnchor().distance(EpsilonModel.getINSTANCE().getAnchor());
        if (distanceByEpsilon< Constants.TRIGORATH_MAX_VEL_RADIUS) {
            Point2D collisionRelativeVector = Utils.relativeLocation(this.getAnchor(), collisionPoint);
            double impactCoefficient = getImpactCoefficient(collisionRelativeVector);
            Point2D impactVector = Utils.relativeLocation(collisionPoint, polygon.getAnchor());
            if(!(polygon instanceof  EpsilonModel)) impactVector = Utils.multiplyVector(Utils.normalizeVector(impactVector) ,1);
            impactVector = Utils.addVectors(impactVector, getDirection().getNormalizedDirectionVector());
            impactVector = Utils.multiplyVector(impactVector ,impactCoefficient);
            this.setDirection(new Direction(Utils.normalizeVector(impactVector)));
        }
        else {
            Point2D collisionRelativeVector = Utils.relativeLocation(this.getAnchor(), collisionPoint);
            double impactCoefficient = getImpactCoefficient(collisionRelativeVector);
            Point2D impactVector = Utils.relativeLocation(collisionPoint, polygon.getAnchor());
            if(!(polygon instanceof  EpsilonModel)) impactVector = Utils.multiplyVector(Utils.normalizeTrigorathVector(impactVector) ,1);
            impactVector = Utils.addVectors(impactVector, getDirection().getTrigorathNormalizedDirectionVector());
            impactVector = Utils.multiplyVector(impactVector ,impactCoefficient);
            this.setDirection(new Direction(Utils.normalizeTrigorathVector(impactVector)));

        }
        // Angular motion
        setAngularMotion(collisionPoint, polygon, inertia);
        createImpactWave(this, polygon, collisionPoint);
    }

    protected void setAngularMotion(Point2D collisionPoint, Collidable polygon, double inertia){
        Point2D r = Utils.relativeLocation(collisionPoint, this.getAnchor());
        Point2D f = Utils.relativeLocation(collisionPoint, polygon.getAnchor());
        double torque = -r.getX()*f.getY()+r.getY()*f.getX();
        if (torque > 400) torque = 400;
        if (torque < -400) torque = -400;
        double momentOfInertia = inertia;
        angularAcceleration = torque/momentOfInertia;
        angularVelocity = 0;
    }


    private void updateNextDashTime(){
        Random random = new Random();
        nextDash = Math.abs(random.nextGaussian(0.5, 0.5));
        if (nextDash<0.25) nextDash=0.25;
        if (nextDash>0.75) nextDash=0.75;
        nextDash *=4;
    }


    @Override
    public double getImpactCoefficient(Point2D collisionRelativeVector) {
        double distance = Math.hypot(collisionRelativeVector.getX(), collisionRelativeVector.getY());
        double impactCoefficient;
        if (distance < Constants.SMALL_IMPACT_RADIUS) {
            setImpactInProgress(true);
            impactMaxVelocity = 2 * Constants.IMPACT_COEFFICIENT / 5;
            impactCoefficient = Constants.IMPACT_COEFFICIENT;
        } else if (distance > (Constants.LARGE_IMPACT_RADIUS + Constants.SMALL_IMPACT_RADIUS ) /2) {
            setImpactInProgress(false);
            impactCoefficient = 0;
        } else {
            setImpactInProgress(true);
            double coefficient = 1 - (distance- Constants.SMALL_IMPACT_RADIUS)/(Constants.LARGE_IMPACT_RADIUS - Constants.SMALL_IMPACT_RADIUS);
            impactCoefficient = coefficient * Constants.IMPACT_COEFFICIENT;
            impactMaxVelocity = 2 * coefficient * Constants.IMPACT_COEFFICIENT / 5;
        }
        return impactCoefficient;
    }

    @Override
    public void banish() {
        Point2D collisionPoint = EpsilonModel.getINSTANCE().getAnchor();
        Point2D collisionRelativeVector = Utils.relativeLocation(this.getAnchor(), collisionPoint);
        double distance = Math.hypot(collisionRelativeVector.getX(), collisionRelativeVector.getY());
        double impactCoefficient;
        if (distance < 100) {
            setImpactInProgress(true);
            impactMaxVelocity = 2.4 * Constants.IMPACT_COEFFICIENT / 5;
            impactCoefficient = Constants.IMPACT_COEFFICIENT;
        } else if (distance > 225) {
            setImpactInProgress(false);
            impactCoefficient = 0;
        } else {
            setImpactInProgress(true);
            double coefficient = 1 - (distance- 100)/(500 - 100);
            impactCoefficient = coefficient * Constants.IMPACT_COEFFICIENT;
            impactMaxVelocity = 2.4 * coefficient * impactCoefficient / 5;
        }
        Point2D impactVector = Utils.normalizeVector(Utils.relativeLocation(this.getAnchor(), collisionPoint));
        impactVector = Utils.multiplyVector(impactVector, impactCoefficient);
        Point2D r2 = Utils.addVectors(this.getDirection().getNormalizedDirectionVector(), impactVector);
        if(!isCircular()){
            Direction direction = new Direction(Utils.normalizeVector(r2));
            this.setDirection(direction);
        } else {
            Direction direction = new Direction(Utils.normalizeVector(r2));
            this.setDirection(direction);
        }
    }

    @Override
    public Point2D[] getVertices() {
        return myPolygon.getVertices();
    }

    @Override
    public ArrayList<Line2D> getEdges() {
        return null;
    }

    @Override
    public void update(Direction direction) {
//        System.out.println("HP:  " + health);
        Point2D movement = Utils.multiplyVector(direction.getDirectionVector(), direction.getMagnitude());
        Random random = new Random();
        Point2D dir = Utils.normalizeVector(Utils.relativeLocation(EpsilonModel.getINSTANCE().getAnchor(), getAnchor()));
        double angle = Utils.findAngleBetweenTwoVectors(dir, getDirection().getDirectionVector());
        nextDash -= 0.010;

        if (nextDash <= 0 && !impactInProgress && angle < 1){
            updateNextDashTime();
            impactMaxVelocity = 2 * Constants.IMPACT_COEFFICIENT / 5;
            setImpactInProgress(true);
        }
        movePolygon(movement);

        friction();
        rotate();
    }

    @Override
    public void update() {
        if (dontUpdate()) return;
        update(direction);
    }

    @Override
    public boolean isCircular() {
        return false;
    }

    @Override
    public double getRadius() {
        return 0;
    }

//    public Point2D getCurrentLocation() {
//        return currentLocation;
//    }

    @Override
    public void friction(){
        direction.setMagnitude(direction.getMagnitude() * 0.97);
        if (direction.getMagnitude() < 1){
            setDirection(
                    new Direction(Utils.relativeLocation(EpsilonModel.getINSTANCE().getAnchor(), getAnchor())));
            getDirection().adjustDirectionMagnitude();
        }
    }
    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void rotate(){
        if (Math.abs(angularVelocity) < 0.0001 && angularAcceleration ==0){
            angularVelocity = 0;
        }

        // Angular Friction
        if (angularVelocity<0 && angularAcceleration==0){
            angularVelocity += 0.0004;
        } else if (angularVelocity>0 && angularAcceleration==0) {
            angularVelocity -= 0.0004;
        }
        if (Math.abs(angularVelocity) < Math.abs(angularAcceleration*10)) {
            angularVelocity += angularAcceleration;
        }
        else angularAcceleration = 0;
        angle += angularVelocity;


        myPolygon = Utils.rotateMyPolygon(myPolygon, Math.toDegrees(-angularVelocity), anchor);



    }

    public Point2D reflect(Point2D normalVector){
        double dotProduct = Utils.dotVectors(getDirection().getDirectionVector(), normalVector);
        Point2D reflection = Utils.addVectors(
                getDirection().getDirectionVector(),
                Utils.multiplyVector(normalVector,-2*dotProduct
                ));
        return Utils.normalizeVector(reflection);
    }
    private double calculateSquarantineInertia() {
        double mass = 200;
        double height = Constants.SQUARANTINE_EDGE;
        double width = Constants.SQUARANTINE_EDGE;
        return 50000;

    }


    @Override
    public ArrayList<Point2D> getBoundingPoints(){;
        ArrayList<Point2D> bound = new ArrayList<>();
        for (int i = 0; i < myPolygon.npoints; i++) {
            bound.add( new Point2D.Double(myPolygon.xpoints[i], myPolygon.ypoints[i]) );
        } return bound;
    }


    @Override
    public void eliminate(){
        super.eliminate();
        collidables.remove(this);
        movables.remove(this);
        squarantineModels.remove(this);

//        aliveEnemies--;
        CollectibleModel.dropCollectible(
                getAnchor(),
                SQUARANTINE_NUM_OF_COLLECTIBLES.getValue(),
                SQUARANTINE_COLLECTIBLES_XP.getValue()
        );

    }



    public List<SquarantineModel> getModels() {
        return squarantineModels;
    }

    @Override
    public void onCollision(Collidable other, Point2D intersection) {
        if (other instanceof EpsilonModel) impact(Utils.relativeLocation(intersection, anchor), intersection, other);
        if (other instanceof Orb) impact(Utils.relativeLocation(intersection, anchor), intersection, other);
        if (other instanceof BulletModel) {
            impact(Utils.relativeLocation(intersection, anchor), intersection, other, 7200);
            createImpactWave(this, other, intersection);
        }

    }

    @Override
    public void onCollision(Collidable other, Point2D coll1, Point2D coll2) {
        ((Impactable) this).impact(coll1, coll2, other);

    }
}