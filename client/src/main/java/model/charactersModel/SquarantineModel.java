package model.charactersModel;

import controller.Game;
import model.MyPolygon;
import model.charactersModel.blackOrb.Orb;
import model.collision.Collidable;
import model.collision.CollisionState;
import model.collision.Impactable;
import model.entities.AttackTypes;
import model.interfaces.Enemy;
import model.movement.Direction;
import model.movement.Movable;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static controller.constants.Constants.*;
import static controller.UserInterfaceController.*;
import static controller.Utils.*;
import static controller.constants.EntityConstants.*;
import static model.imagetools.ToolBox.getBufferedImage;

public class SquarantineModel extends GeoShapeModel implements Movable, Collidable, Impactable, Enemy {
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
        createSquarantineView(id);
        damageSize.put(AttackTypes.MELEE, 6);
    }

    public SquarantineModel() {
    }

    private void initMyPolygon() {
        double halfEdgeLength = edgeLength / 2;
        double x = anchor.getX();
        double y = anchor.getY();

        Point2D[] vertices = new Point2D[4];
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
        Point2D collisionRelativeVector = relativeLocation(this.getAnchor(), collisionPoint);
        double impactCoefficient = getImpactCoefficient(collisionRelativeVector);
//        impactCoefficient *= 2;
        Point2D impactVector = normalizeVector(relativeLocation(this.getAnchor(), collisionState.collisionPoint));
        impactVector = multiplyVector(impactVector, impactCoefficient);
        Point2D r2 = addVectors(this.getDirection().getNormalizedDirectionVector(), impactVector);
        Direction direction = new Direction(normalizeVector(r2));
        this.setDirection(direction);

    }


    @Override
    public void impact(Point2D normalVector, Point2D collisionPoint, Collidable polygon) {
        Point2D collisionRelativeVector = relativeLocation(this.getAnchor(), collisionPoint);
        double impactCoefficient = getImpactCoefficient(collisionRelativeVector);
        Point2D impactVector = relativeLocation(collisionPoint, polygon.getAnchor());
        if (!(polygon instanceof EpsilonModel)) impactVector = multiplyVector(normalizeVector(impactVector), 1);
        impactVector = addVectors(impactVector, getDirection().getNormalizedDirectionVector());
        impactVector = multiplyVector(impactVector, impactCoefficient);
        this.setDirection(new Direction(normalizeVector(impactVector)));

        // Angular motion
        setAngularMotion(collisionPoint, polygon, 34000);
        createImpactWave(this, polygon, collisionPoint);
    }

    //todo duplicated version. needs to be eiminated ...
    public void impact(Point2D normalVector, Point2D collisionPoint, Collidable polygon, double inertia) {
        double distanceByEpsilon = getAnchor().distance(EpsilonModel.getINSTANCE().getAnchor());
        if (distanceByEpsilon < TRIGORATH_MAX_VEL_RADIUS) {
            Point2D collisionRelativeVector = relativeLocation(this.getAnchor(), collisionPoint);
            double impactCoefficient = getImpactCoefficient(collisionRelativeVector);
            Point2D impactVector = relativeLocation(collisionPoint, polygon.getAnchor());
            if (!(polygon instanceof EpsilonModel)) impactVector = multiplyVector(normalizeVector(impactVector), 1);
            impactVector = addVectors(impactVector, getDirection().getNormalizedDirectionVector());
            impactVector = multiplyVector(impactVector, impactCoefficient);
            this.setDirection(new Direction(normalizeVector(impactVector)));
        } else {
            Point2D collisionRelativeVector = relativeLocation(this.getAnchor(), collisionPoint);
            double impactCoefficient = getImpactCoefficient(collisionRelativeVector);
            Point2D impactVector = relativeLocation(collisionPoint, polygon.getAnchor());
            if (!(polygon instanceof EpsilonModel))
                impactVector = multiplyVector(normalizeTrigorathVector(impactVector), 1);
            impactVector = addVectors(impactVector, getDirection().getTrigorathNormalizedDirectionVector());
            impactVector = multiplyVector(impactVector, impactCoefficient);
            this.setDirection(new Direction(normalizeTrigorathVector(impactVector)));

        }
        // Angular motion
        setAngularMotion(collisionPoint, polygon, inertia);
        createImpactWave(this, polygon, collisionPoint);
    }

    protected void setAngularMotion(Point2D collisionPoint, Collidable polygon, double inertia) {
        Point2D r = relativeLocation(collisionPoint, this.getAnchor());
        Point2D f = relativeLocation(collisionPoint, polygon.getAnchor());
        double torque = -r.getX() * f.getY() + r.getY() * f.getX();
        if (torque > 400) torque = 400;
        if (torque < -400) torque = -400;
        double momentOfInertia = inertia;
        angularAcceleration = torque / momentOfInertia;
        angularVelocity = 0;
    }


    private void updateNextDashTime() {
        Random random = new Random();
        nextDash = Math.abs(random.nextGaussian(0.5, 0.5));
        if (nextDash < 0.25) nextDash = 0.25;
        if (nextDash > 0.75) nextDash = 0.75;
        nextDash *= 4;
    }


    @Override
    public double getImpactCoefficient(Point2D collisionRelativeVector) {
        double distance = Math.hypot(collisionRelativeVector.getX(), collisionRelativeVector.getY());
        double impactCoefficient;
        if (distance < SMALL_IMPACT_RADIUS) {
            setImpactInProgress(true);
            impactMaxVelocity = 2 * IMPACT_COEFFICIENT / 5;
            impactCoefficient = IMPACT_COEFFICIENT;
        } else if (distance > (LARGE_IMPACT_RADIUS + SMALL_IMPACT_RADIUS) / 2) {
            setImpactInProgress(false);
            impactCoefficient = 0;
        } else {
            setImpactInProgress(true);
            double coefficient = 1 - (distance - SMALL_IMPACT_RADIUS) / (LARGE_IMPACT_RADIUS - SMALL_IMPACT_RADIUS);
            impactCoefficient = coefficient * IMPACT_COEFFICIENT;
            impactMaxVelocity = 2 * coefficient * IMPACT_COEFFICIENT / 5;
        }
        return impactCoefficient;
    }

    @Override
    public void banish() {
        Point2D collisionPoint = EpsilonModel.getINSTANCE().getAnchor();
        Point2D collisionRelativeVector = relativeLocation(this.getAnchor(), collisionPoint);
        double distance = Math.hypot(collisionRelativeVector.getX(), collisionRelativeVector.getY());
        double impactCoefficient;
        if (distance < 100) {
            setImpactInProgress(true);
            impactMaxVelocity = 2.4 * IMPACT_COEFFICIENT / 5;
            impactCoefficient = IMPACT_COEFFICIENT;
        } else if (distance > 225) {
            setImpactInProgress(false);
            impactCoefficient = 0;
        } else {
            setImpactInProgress(true);
            double coefficient = 1 - (distance - 100) / (500 - 100);
            impactCoefficient = coefficient * IMPACT_COEFFICIENT;
            impactMaxVelocity = 2.4 * coefficient * impactCoefficient / 5;
        }
        Point2D impactVector = normalizeVector(relativeLocation(this.getAnchor(), collisionPoint));
        impactVector = multiplyVector(impactVector, impactCoefficient);
        Point2D r2 = addVectors(this.getDirection().getNormalizedDirectionVector(), impactVector);
        if (!isCircular()) {
            Direction direction = new Direction(normalizeVector(r2));
            this.setDirection(direction);
        } else {
            Direction direction = new Direction(normalizeVector(r2));
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
        Point2D movement = multiplyVector(direction.getDirectionVector(), direction.getMagnitude());
        Random random = new Random();
        Point2D dir = normalizeVector(relativeLocation(EpsilonModel.getINSTANCE().getAnchor(), getAnchor()));
        double angle = findAngleBetweenTwoVectors(dir, getDirection().getDirectionVector());
        nextDash -= 0.010;

        if (nextDash <= 0 && !impactInProgress && angle < 1) {
            updateNextDashTime();
            impactMaxVelocity = 2 * IMPACT_COEFFICIENT / 5;
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

    @Override
    public void friction() {
        direction.setMagnitude(direction.getMagnitude() * 0.97);
        if (direction.getMagnitude() < 1) {
            setDirection(
                    new Direction(relativeLocation(EpsilonModel.getINSTANCE().getAnchor(), getAnchor())));
            getDirection().adjustDirectionMagnitude();
        }
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void rotate() {
        if (Math.abs(angularVelocity) < 0.0001 && angularAcceleration == 0) {
            angularVelocity = 0;
        }

        // Angular Friction
        if (angularVelocity < 0 && angularAcceleration == 0) {
            angularVelocity += 0.0004;
        } else if (angularVelocity > 0 && angularAcceleration == 0) {
            angularVelocity -= 0.0004;
        }
        if (Math.abs(angularVelocity) < Math.abs(angularAcceleration * 10)) {
            angularVelocity += angularAcceleration;
        } else angularAcceleration = 0;
        angle += angularVelocity;


        myPolygon = rotateMyPolygon(myPolygon, Math.toDegrees(-angularVelocity), anchor);


    }


    @Override
    public ArrayList<Point2D> getBoundingPoints() {
        ;
        ArrayList<Point2D> bound = new ArrayList<>();
        for (int i = 0; i < myPolygon.npoints; i++) {
            bound.add(new Point2D.Double(myPolygon.xpoints[i], myPolygon.ypoints[i]));
        }
        return bound;
    }


    @Override
    public void eliminate() {
        super.eliminate();
        collidables.remove(this);
        movables.remove(this);
        squarantineModels.remove(this);
        Game.getINSTANCE().incrementDeadEnemies();
        CollectibleModel.dropCollectible(getAnchor(), SQUARANTINE_NUM_OF_COLLECTIBLES.getValue(), SQUARANTINE_COLLECTIBLES_XP.getValue());
    }


    public List<SquarantineModel> getModels() {
        return squarantineModels;
    }

    @Override
    public void onCollision(Collidable other, Point2D intersection) {
        if (other instanceof EpsilonModel) impact(relativeLocation(intersection, anchor), intersection, other);
        if (other instanceof Orb) impact(relativeLocation(intersection, anchor), intersection, other);
        if (other instanceof BulletModel) {
            impact(relativeLocation(intersection, anchor), intersection, other, 7200);
            createImpactWave(this, other, intersection);
        }

    }

    @Override
    public void onCollision(Collidable other, Point2D coll1, Point2D coll2) {
        ((Impactable) this).impact(coll1, coll2, other);

    }

    @Override
    public void create() {
        Point2D anchor;
        boolean isValid;
        double MIN_DISTANCE = 100.0; // The minimum distance to avoid collision
        int maxAttempts = 100;
        int attempts = 0;

        do {
            isValid = true;
            anchor = findRandomPoint();
            attempts++;

            for (GeoShapeModel model : entities) {
                double distance = model.getAnchor().distance(anchor);
                if (distance < MIN_DISTANCE) {
                    isValid = false;
                    break;
                }
            }
        } while (!isValid && attempts < maxAttempts);

        if (isValid) {
            // Add the new enemy to the game's entities
            new SquarantineModel(anchor);
        } else {
            System.out.println("Failed to create SquarantineModel without intersection after " + maxAttempts + " attempts.");
        }
    }

    @Override
    public int getMinSpawnWave() {
        return 1;
    }

    @Override
    public boolean isUniquePerWave() {
        return false;
    }
}