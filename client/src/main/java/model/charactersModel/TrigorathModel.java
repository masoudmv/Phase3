
package model.charactersModel;

import controller.Game;
import controller.Utils;
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

import static controller.constants.Constants.*;
import static controller.UserInterfaceController.*;
import static controller.Utils.*;
import static controller.constants.EntityConstants.*;
import static model.imagetools.ToolBox.getBufferedImage;

public class TrigorathModel extends GeoShapeModel implements Movable, Collidable, Impactable, Enemy {
    public double impactMaxVelocity;
    private boolean impactInProgress = false;
    public static List<TrigorathModel> trigorathModels =new ArrayList<>();
    private double angularVelocity;
    private double angularAcceleration;
    static BufferedImage image;
    private static double edgeLength;

    public TrigorathModel(Point2D anchor) {
        super(anchor, image, new MyPolygon(new double[3], new double[3], 3), true);
        initVertices();
        trigorathModels.add(this);
        collidables.add(this);
        movables.add(this);
        impactables.add(this);
        this.health = 15;
        createTrigorathView(id);
        damageSize.put(AttackTypes.MELEE, 10);
    }

    public TrigorathModel() {
        super();
    }

    private void initVertices(){
        double radius = edgeLength / Math.sqrt(3);
        Point2D point1 = new Point2D.Double(anchor.getX(), anchor.getY()-radius);
        Point2D point2 = new Point2D.Double(anchor.getX()+ radius*Math.cos(Math.PI/6), anchor.getY()+radius/2);
        Point2D point3 = new Point2D.Double(anchor.getX()-radius*Math.cos(Math.PI/6), anchor.getY()+radius/2);
        Point2D[] vertices = new Point2D[]{point1, point2, point3};
        myPolygon.setVertices(vertices);
    }

    public String getId() {
        return id;
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

    @Override
    public Point2D getAnchor() {
        return anchor;
    }


    public static BufferedImage loadImage() {
        Image img = new ImageIcon("./client/src/squarantine.png").getImage();
        TrigorathModel.image = getBufferedImage(img);
        edgeLength = 40;
        return TrigorathModel.image;
    }



    @Override
    public void impact(CollisionState collisionState) {
        double distanceByEpsilon = getAnchor().distance(EpsilonModel.getINSTANCE().getAnchor());
        if (distanceByEpsilon<TRIGORATH_MAX_VEL_RADIUS) {
            Point2D collisionPoint = collisionState.collisionPoint;
            Point2D collisionRelativeVector = relativeLocation(this.getAnchor(), collisionPoint);
            double impactCoefficient = getImpactCoefficient(collisionRelativeVector);
            Point2D impactVector = normalizeVector(relativeLocation(this.getAnchor(), collisionState.collisionPoint));
            impactVector = multiplyVector(impactVector, impactCoefficient);
            Point2D r2 = addVectors(this.getDirection().getNormalizedDirectionVector(), impactVector);

            Direction direction = new Direction(normalizeVector(r2));
            this.setDirection(direction);
        }
        else {
            Point2D collisionPoint = collisionState.collisionPoint;
            Point2D collisionRelativeVector = relativeLocation(this.getAnchor(), collisionPoint);
            double impactCoefficient = getImpactCoefficient(collisionRelativeVector);
//        impactCoefficient *= 2;
            Point2D impactVector = normalizeTrigorathVector(relativeLocation(this.getAnchor(), collisionState.collisionPoint));
            impactVector = multiplyVector(impactVector, impactCoefficient);
            Point2D r2 = addVectors(this.getDirection().getTrigorathNormalizedDirectionVector(), impactVector);

            Direction direction = new Direction(normalizeTrigorathVector(r2));
            this.setDirection(direction);
        }


    }


    public void impact(Point2D normalVector, Point2D collisionPoint, Collidable polygon) {
        double distanceByEpsilon = getAnchor().distance(EpsilonModel.getINSTANCE().getAnchor());
        if (distanceByEpsilon < TRIGORATH_MAX_VEL_RADIUS) {
            Point2D collisionRelativeVector = relativeLocation(this.getAnchor(), collisionPoint);
            double impactCoefficient = getImpactCoefficient(collisionRelativeVector);
            Point2D impactVector = relativeLocation(collisionPoint, polygon.getAnchor());
            if(!(polygon instanceof  EpsilonModel)) impactVector = multiplyVector(normalizeVector(impactVector) ,1);
            impactVector = addVectors(impactVector, getDirection().getNormalizedDirectionVector());
            impactVector = multiplyVector(impactVector ,impactCoefficient);
            this.setDirection(new Direction(normalizeVector(impactVector)));
        }
        else {
            Point2D collisionRelativeVector = relativeLocation(this.getAnchor(), collisionPoint);
            double impactCoefficient = getImpactCoefficient(collisionRelativeVector);
            Point2D impactVector = relativeLocation(collisionPoint, polygon.getAnchor());
            if(!(polygon instanceof  EpsilonModel)) impactVector = multiplyVector(normalizeTrigorathVector(impactVector) ,1);
            impactVector = addVectors(impactVector, getDirection().getTrigorathNormalizedDirectionVector());
            impactVector = multiplyVector(impactVector ,impactCoefficient);
            this.setDirection(new Direction(normalizeTrigorathVector(impactVector)));

        }

        // Angular motion
        setAngularMotion(collisionPoint, polygon, 34000);
        createImpactWave(this, polygon, collisionPoint);

    }



    //todo duplicated version. needs to be eiminated ...
    public void impact(Point2D normalVector, Point2D collisionPoint, Collidable polygon, double inertia) {
        double distanceByEpsilon = getAnchor().distance(EpsilonModel.getINSTANCE().getAnchor());
        if (distanceByEpsilon<TRIGORATH_MAX_VEL_RADIUS) {
            Point2D collisionRelativeVector = relativeLocation(this.getAnchor(), collisionPoint);
            double impactCoefficient = getImpactCoefficient(collisionRelativeVector);
            Point2D impactVector = relativeLocation(collisionPoint, polygon.getAnchor());
            if(!(polygon instanceof  EpsilonModel)) impactVector = multiplyVector(normalizeVector(impactVector) ,1);
            impactVector = addVectors(impactVector, getDirection().getNormalizedDirectionVector());
            impactVector = multiplyVector(impactVector ,impactCoefficient);
            this.setDirection(new Direction(normalizeVector(impactVector)));
        }
        else {
            Point2D collisionRelativeVector = relativeLocation(this.getAnchor(), collisionPoint);
            double impactCoefficient = getImpactCoefficient(collisionRelativeVector);
            Point2D impactVector = relativeLocation(collisionPoint, polygon.getAnchor());
            if(!(polygon instanceof  EpsilonModel)) impactVector = multiplyVector(normalizeTrigorathVector(impactVector) ,1);
            impactVector = addVectors(impactVector, getDirection().getTrigorathNormalizedDirectionVector());
            impactVector = multiplyVector(impactVector ,impactCoefficient);
            this.setDirection(new Direction(normalizeTrigorathVector(impactVector)));

        }
        // Angular motion
        setAngularMotion(collisionPoint, polygon, inertia);
        createImpactWave(this, polygon, collisionPoint);
    }

    protected void setAngularMotion(Point2D collisionPoint, Collidable polygon, double inertia){
        Point2D r = relativeLocation(collisionPoint, this.getAnchor());
        Point2D f = relativeLocation(collisionPoint, polygon.getAnchor());
        double torque = -r.getX()*f.getY()+r.getY()*f.getX();
        if (torque > 400) torque = 400;
        if (torque < -400) torque = -400;
        double momentOfInertia = inertia;
        angularAcceleration = torque/momentOfInertia;
        angularVelocity = 0;
    }

    @Override
    public double getImpactCoefficient(Point2D collisionRelativeVector) {
        double impactCoefficient;
        double distanceByEpsilon = getAnchor().distance(EpsilonModel.getINSTANCE().getAnchor());
        if (distanceByEpsilon<TRIGORATH_MAX_VEL_RADIUS) {
            double distance = Math.hypot(collisionRelativeVector.getX(), collisionRelativeVector.getY());
            if (distance < SMALL_IMPACT_RADIUS) {
                setImpactInProgress(true);
                impactMaxVelocity = 2 * IMPACT_COEFFICIENT / 5;
                impactCoefficient = IMPACT_COEFFICIENT;
            } else if (distance > (LARGE_IMPACT_RADIUS + SMALL_IMPACT_RADIUS ) /2) {
                setImpactInProgress(false);
                impactCoefficient = 0;
            } else {
                setImpactInProgress(true);
                double coefficient = 1 - (distance- SMALL_IMPACT_RADIUS)/(LARGE_IMPACT_RADIUS - SMALL_IMPACT_RADIUS);
                impactCoefficient = coefficient * IMPACT_COEFFICIENT;
                impactMaxVelocity = 2 * coefficient * impactCoefficient / 5;
            }
        } else {
            double distance = Math.hypot(collisionRelativeVector.getX(), collisionRelativeVector.getY());
            if (distance < SMALL_IMPACT_RADIUS) {
                setImpactInProgress(true);
                impactMaxVelocity = 2 * IMPACT_COEFFICIENT / 5;
                impactCoefficient = IMPACT_COEFFICIENT;
            } else if (distance > (LARGE_IMPACT_RADIUS + SMALL_IMPACT_RADIUS ) /2) {
                setImpactInProgress(false);
                impactCoefficient = 0;
            } else {
                setImpactInProgress(true);
                double coefficient = 1 - (distance- SMALL_IMPACT_RADIUS)/(LARGE_IMPACT_RADIUS - SMALL_IMPACT_RADIUS);
                impactCoefficient = coefficient * IMPACT_COEFFICIENT;
                impactMaxVelocity = 2 * coefficient * impactCoefficient / 5;
            }
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
            impactMaxVelocity = 3 * IMPACT_COEFFICIENT / 5;
            impactCoefficient = IMPACT_COEFFICIENT;
        } else if (distance > 400) {
            setImpactInProgress(false);
            impactCoefficient = 0;
        } else {
            setImpactInProgress(true);
            double coefficient = 1 - (distance- 100)/(500 - 100);
            impactCoefficient = coefficient * IMPACT_COEFFICIENT;
            impactMaxVelocity = 3* coefficient * impactCoefficient / 5;
        }
        Point2D impactVector = normalizeVector(relativeLocation(this.getAnchor(), collisionPoint));
        impactVector = multiplyVector(impactVector, impactCoefficient);
        Point2D r2 = addVectors(this.getDirection().getNormalizedDirectionVector(), impactVector);
        if(!isCircular()){
            Direction direction = new Direction(normalizeVector(r2));
            this.setDirection(direction);
        } else {
            Direction direction = new Direction(normalizeVector(r2));
            this.setDirection(direction);
        }
    }

    @Override
    public Point2D[] getVertices() {
        return this.myPolygon.getVertices();
    }

    @Override
    public ArrayList<Line2D> getEdges() {
        return null;
    } // todo implement

    @Override
    public void update(Direction direction) {
        Point2D movement = multiplyVector(direction.getDirectionVector(), direction.getMagnitude());
        movePolygon(movement);
        rotate();
        friction();
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
    public void friction(){
        direction.setMagnitude(direction.getMagnitude() * FRICTION);
        double distanceByEpsilon = getAnchor().distance(EpsilonModel.getINSTANCE().getAnchor());
        if (direction.getMagnitude() < 1) {
            setDirection(new Direction(relativeLocation(EpsilonModel.getINSTANCE().getAnchor(), getAnchor())));
            getDirection().adjustDirectionMagnitude();
            setImpactInProgress(false);
        } if (distanceByEpsilon > TRIGORATH_MAX_VEL_RADIUS && direction.getMagnitude() < 1.5){
            setDirection(new Direction(relativeLocation(EpsilonModel.getINSTANCE().getAnchor(), getAnchor())));
            getDirection().adjustTrigorathDirectionMagnitude();
        }
    }

    public double getAngle() {
        return angle;
    }

    @Override
    public void setMyPolygon(MyPolygon myPolygon) {

    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void rotate(){
        if (Math.abs(angularVelocity) < 0.0001 && angularAcceleration ==0) angularVelocity = 0;
        // Angular Friction
        if (angularVelocity<0 && angularAcceleration==0) angularVelocity += 0.0004;
        else if (angularVelocity>0 && angularAcceleration==0) angularVelocity -= 0.0004;
        if (Math.abs(angularVelocity) < Math.abs(angularAcceleration*10)) angularVelocity += angularAcceleration;
        else angularAcceleration = 0;
        angle += angularVelocity;
        myPolygon = rotateMyPolygon(myPolygon, Math.toDegrees(-angularVelocity), anchor);
    }

    @Override
    public ArrayList<Point2D> getBoundingPoints(){;
        ArrayList<Point2D> bound = new ArrayList<>();
        for (int i = 0; i < myPolygon.npoints; i++) {
            bound.add( new Point2D.Double(myPolygon.xpoints[i], myPolygon.ypoints[i]) );
        } return bound;
    }


    public void eliminate(){
        super.eliminate();
        collidables.remove(this);
        trigorathModels.remove(this);
        movables.remove(this);
        Game.getINSTANCE().incrementDeadEnemies();
        CollectibleModel.dropCollectible(getAnchor(), TRIGORATH_NUM_OF_COLLECTIBLES.getValue(), TRIGORATH_COLLECTIBLES_XP.getValue());
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
            new TrigorathModel(anchor);
        } else {
            System.out.println("Failed to create SquarantineModel without intersection after " + maxAttempts + " attempts.");
        }
    }

    @Override
    public int getMinSpawnWave() {
        return 1;
    }

    @Override
    public boolean isUniquePerWave(){
        return false;
    }

    @Override
    public void onCollision(Collidable other, Point2D intersection) {
        if (other instanceof EpsilonModel) impact(relativeLocation(intersection, anchor), intersection, other);
        if (other instanceof Orb) impact(relativeLocation(intersection, anchor), intersection, other);
        if (other instanceof BulletModel) {
            impact(Utils.relativeLocation(intersection, anchor), intersection, other, 6200);
            createImpactWave(this, other, intersection);
        }
    }

    @Override
    public void onCollision(Collidable other, Point2D coll1, Point2D coll2) {
        if (((GeoShapeModel) other).isHovering) return;
        ((Impactable) this).impact(coll1, coll2, other);
    }


}