package model.charactersModel;

import model.BulletModel;
import model.CollectibleModel;
import model.collision.Collidable;
import model.collision.CollisionState;
import model.collision.Impactable;
import model.movement.Direction;
import model.movement.Movable;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import static controller.Constants.*;
import static controller.Controller.*;
import static controller.Sound.playBubble;
import static controller.Sound.playDeathSound;
import static controller.Update.aliveEnemies;
import static controller.Utils.*;

public class SquarantineModel implements Movable, Collidable, Impactable {
    private int hp = 10;
    String id;
    double nextDash=Double.MAX_VALUE;
    private Point2D anchor;
    Direction direction;
    Point2D currentLocation;
    private final Point2D[] vertices;
    double radius;
    private boolean impactInProgress = false;
    public double impactMaxVelocity;
    private double angle;
    private double angularVelocity;
    private double angularAcceleration;
    public static ArrayList<SquarantineModel> squarantineModels =new ArrayList<>();

    public SquarantineModel(Point2D anchor) {
        this.anchor = anchor;
        this.radius =SQUARANTINE_EDGE /2;
        Point2D point1 = new Point2D.Double((anchor.getX()-SQUARANTINE_EDGE /2), (anchor.getY()-SQUARANTINE_EDGE /2));
        Point2D point2 = new Point2D.Double((anchor.getX()+SQUARANTINE_EDGE /2), (anchor.getY()-SQUARANTINE_EDGE /2));
        Point2D point3 = new Point2D.Double((anchor.getX()+SQUARANTINE_EDGE /2), (anchor.getY()+SQUARANTINE_EDGE /2));
        Point2D point4 = new Point2D.Double((anchor.getX()-SQUARANTINE_EDGE /2), (anchor.getY()+SQUARANTINE_EDGE /2));
        vertices = new Point2D[]{point1, point2, point3, point4};
        this.id= UUID.randomUUID().toString();
        this.direction = new Direction(0);
        updateNextDashTime();
        squarantineModels.add(this);
        collidables.add(this);
        movables.add(this);
        impactables.add(this);
        createSquarantineView(id);
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

    @Override
    public void impact(CollisionState collisionState) {
        Point2D collisionPoint = collisionState.collisionPoint;
        Point2D collisionRelativeVector = relativeLocation(this.getAnchor(), collisionPoint);
        double impactCoefficient = getImpactCoefficient(collisionRelativeVector);
//        impactCoefficient *= 2;
        Point2D impactVector = normalizeVector(relativeLocation(this.getAnchor(), collisionState.collisionPoint));
        impactVector = multiplyVector(impactVector ,impactCoefficient);
        Point2D r2 = addVectors(this.getDirection().getNormalizedDirectionVector(), impactVector);
        Direction direction = new Direction(normalizeVector(r2));
        this.setDirection(direction);

    }


    @Override
    public void impact(Point2D normalVector, Point2D collisionPoint, Collidable polygon) {
        Point2D collisionRelativeVector = relativeLocation(this.getAnchor(), collisionPoint);
        double impactCoefficient = getImpactCoefficient(collisionRelativeVector);
        Point2D impactVector = relativeLocation(collisionPoint, polygon.getAnchor());
        if(!(polygon instanceof  EpsilonModel)) impactVector = multiplyVector(normalizeVector(impactVector) ,1);
        impactVector = addVectors(impactVector, getDirection().getNormalizedDirectionVector());
        impactVector = multiplyVector(impactVector ,impactCoefficient);
        this.setDirection(new Direction(normalizeVector(impactVector)));

        // Angular motion
        Point2D r = relativeLocation(collisionPoint, this.getAnchor());
        Point2D f = relativeLocation(collisionPoint, polygon.getAnchor());
        double torque = -r.getX()*f.getY()+r.getY()*f.getX();
        if (torque>400) torque = 400;
        if (torque<-400) torque = -400;
        double momentOfInertia = calculateSquarantineInertia();
        angularAcceleration = torque/momentOfInertia;
        angularVelocity = 0;
    }

    public void bulletImpact(BulletModel bulletModel, Point2D collisionPoint){
        Point2D impactVector = bulletModel.getDirection().getDirectionVector();
        impactMaxVelocity = 2 * IMPACT_COEFFICIENT / 5;
        setImpactInProgress(true);
        this.setDirection(new Direction(normalizeVector(impactVector)));

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
            double coefficient = 1 - (distance- 100)/(500 - 100);
            impactCoefficient = coefficient * IMPACT_COEFFICIENT;
            impactMaxVelocity = 2.4 * coefficient * impactCoefficient / 5;
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
        return this.vertices;
    }

    @Override
    public void move(Direction direction) {
        Point2D movement = multiplyVector(direction.getDirectionVector(), direction.getMagnitude());
        Random random = new Random();
        Point2D dir = normalizeVector(relativeLocation(EpsilonModel.getINSTANCE().getAnchor(), getAnchor()));
        double angle = findAngleBetweenTwoVectors(dir, getDirection().getDirectionVector());
        nextDash -= 0.010;
        if (nextDash<=0 && !impactInProgress && angle<1){
            updateNextDashTime();
            impactMaxVelocity = 2 * IMPACT_COEFFICIENT / 5;
            setImpactInProgress(true);
        }
        this.anchor = addVectors(anchor, movement);
        for (int i = 0; i < 4; i++) {
            vertices[i] = addVectors(vertices[i], movement);
        }
    }

    @Override
    public void move() {
        move(direction);
    }

    @Override
    public boolean isCircular() {
        return false;
    }

    @Override
    public double getRadius() {
        return 0;
    }

    public Point2D getCurrentLocation() {
        return currentLocation;
    }
    @Override
    public void friction(){
        direction.setMagnitude(direction.getMagnitude() * 0.97);
        if (direction.getMagnitude() < 1){
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
        vertices[0] = new Point2D.Double(
                (anchor.getX()-SQUARANTINE_RADIUS*Math.sin(PI/4+angle)),
                (anchor.getY()-SQUARANTINE_RADIUS*Math.cos(PI/4+angle))
        );
        vertices[1] = new Point2D.Double(
                (anchor.getX()+SQUARANTINE_RADIUS*Math.cos(PI/4+angle)),
                (anchor.getY()-SQUARANTINE_RADIUS*Math.sin(PI/4+angle))
        );
        vertices[2] = new Point2D.Double(
                (anchor.getX()+SQUARANTINE_RADIUS*Math.cos(PI/4-angle)),
                (anchor.getY()+SQUARANTINE_RADIUS*Math.sin(PI/4-angle))
        );
        vertices[3] = new Point2D.Double(
                (anchor.getX()-SQUARANTINE_RADIUS*Math.sin(PI/4-angle)),
                (anchor.getY()+SQUARANTINE_RADIUS*Math.cos(PI/4-angle))
        );

    }
    public Point2D reflect(Point2D normalVector){
        double dotProduct = dotVectors(getDirection().getDirectionVector(), normalVector);
        Point2D reflection = addVectors(
                getDirection().getDirectionVector(),
                multiplyVector(normalVector,-2*dotProduct
                ));
        return normalizeVector(reflection);
    }
    private double calculateSquarantineInertia() {
        double mass = 200;
        double height = SQUARANTINE_EDGE;
        double width = SQUARANTINE_EDGE;
//        System.out.println(mass*(height*height+width*width)/12);
        return 50000;

    }

    public void remove(){
        collidables.remove(this);
        movables.remove(this);
        squarantineModels.remove(this);
        aliveEnemies--;
        findSquarantineView(id).remove();
        dropCollectible();
        playDeathSound();
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    // damage parameter is a positive number
    public void damage(int damage){
        this.hp -= damage;
        playBubble();
    }


    public void dropCollectible() {
        Point2D direction = relativeLocation(getAnchor(), EpsilonModel.getINSTANCE().getAnchor());
        Random random = new Random();
        double theta = random.nextGaussian(Math.PI, 1);
        if (theta<PI/2) theta = PI/2;
        if (theta>3*PI/2) theta = 3*PI/2;
        new CollectibleModel(getAnchor(), rotateVector(direction, theta));
    }
}