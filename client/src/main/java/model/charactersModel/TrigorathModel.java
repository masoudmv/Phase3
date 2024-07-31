
package model.charactersModel;

import model.MyPolygon;
import model.collision.Collidable;
import model.collision.CollisionState;
import model.collision.Impactable;
import model.movement.Direction;
import model.movement.Movable;
import view.charactersView.TrigorathView;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static controller.constants.Constants.*;
import static controller.UserInterfaceController.*;
import static controller.Sound.playBubble;
import static controller.Sound.playDeathSound;
import static controller.GameLoop.aliveEnemies;
import static controller.Utils.*;
import static controller.constants.EntityConstants.*;
import static model.imagetools.ToolBox.getBufferedImage;

public class TrigorathModel extends GeoShapeModel implements Movable, Collidable, Impactable {
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

//    @Override
//    public void setAnchor(Point2D anchor) {
//
//    }

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
//        impactCoefficient *= 2;
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
//         impactCoefficient = getImpactCoefficient(collisionRelativeVector);
//        impactCoefficient *= 2;
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

    public void bulletImpact(BulletModel bulletModel, Point2D collisionPoint){
        double distanceByEpsilon = getAnchor().distance(EpsilonModel.getINSTANCE().getAnchor());
        if (distanceByEpsilon < TRIGORATH_MAX_VEL_RADIUS) {
            Point2D impactVector = bulletModel.getDirection().getNormalizedDirectionVector();
            impactMaxVelocity = 2 * BULLET_IMPACT_COEFFICIENT / 5;
            setImpactInProgress(true);
            this.setDirection(new Direction(impactVector));
        }
        else {
            Point2D impactVector = bulletModel.getDirection().getTrigorathNormalizedDirectionVector();
            impactMaxVelocity = 2 * BULLET_IMPACT_COEFFICIENT / 5;
            setImpactInProgress(true);
            this.setDirection(new Direction(impactVector));
//            getDirection().adjustTrigorathDirectionMagnitude();
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
    public void move(Direction direction) {
        double distanceByEpsilon = getAnchor().distance(EpsilonModel.getINSTANCE().getAnchor());
        Point2D movement = multiplyVector(direction.getDirectionVector(), direction.getMagnitude());
        double magnitude = getDirection().getMagnitude();
//        this.anchor = addVectors(anchor, movement);


//        for (int i = 0; i < 3; i++) {
//            vertices[i] = addVectors(vertices[i], movement);
//        }
        // TODO !isImpactInProgress???

        movePolygon(movement);
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


    @Override
    public void friction(){
//        System.out.println(getDirection().getMagnitude());

        direction.setMagnitude(direction.getMagnitude() * FRICTION);
        double distanceByEpsilon = getAnchor().distance(EpsilonModel.getINSTANCE().getAnchor());
        if (direction.getMagnitude() < 1) {
//            System.out.println("ddsdsd");
            setDirection(
                    new Direction(relativeLocation(EpsilonModel.getINSTANCE().getAnchor(), getAnchor())));
            getDirection().adjustDirectionMagnitude();
            setImpactInProgress(false);

        }

        if (distanceByEpsilon > TRIGORATH_MAX_VEL_RADIUS && direction.getMagnitude() < 1.5){
//            System.out.println("ddsdsd");
            setDirection(
                    new Direction(relativeLocation(EpsilonModel.getINSTANCE().getAnchor(), getAnchor())));
             getDirection().adjustTrigorathDirectionMagnitude();
        }
        else {
//            System.out.println("saaaaaaaaaaaa");
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
//        System.out.println(angularVelocity);
//        angularVelocity *= 0.99;
//        if (Math.abs(angularVelocity) < 0.001) {
//            angularVelocity = 0;
//        }
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

//        vertices[0] = new Point2D.Double(anchor.getX()-radius*Math.sin(angle), anchor.getY()-radius*Math.cos(angle));
//        vertices[1] = new Point2D.Double(anchor.getX()+radius*Math.cos(Math.PI/6-angle), anchor.getY()+radius*Math.sin(Math.PI/6-angle));
//        vertices[2] = new Point2D.Double(anchor.getX()-radius*Math.cos(Math.PI/6+angle), anchor.getY()+radius*Math.sin(Math.PI/6+angle));

        myPolygon = rotateMyPolygon(myPolygon, Math.toDegrees(-angularVelocity), anchor);

    }
    public Point2D reflect(Point2D normalVector){
        double dotProduct = dotVectors(getDirection().getDirectionVector(), normalVector);
        Point2D reflection = addVectors(
                getDirection().getDirectionVector(),
                multiplyVector(normalVector,-2*dotProduct
                ));
        return normalizeVector(reflection);
    }
    private double calculateTrigorathInertia(){
        double mass = 100;
        double length = TRIGORATH_RADIUS* (1.5);
//        System.out.println(mass*length*length/12);
        return 50000;
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

//        aliveEnemies--;

        CollectibleModel.dropCollectible(getAnchor(), TRIGORATH_NUM_OF_COLLECTIBLES.getValue(), TRIGORATH_COLLECTIBLES_XP.getValue());


    }

    public void dropCollectible() {
        Point2D direction = relativeLocation(getAnchor(), EpsilonModel.getINSTANCE().getAnchor());
        Random random = new Random();
        double theta = random.nextGaussian(Math.PI, 1);
        if (theta<PI/2) theta = PI/2;
        if (theta>3*PI/2) theta = 3*PI/2;
        new CollectibleModel(getAnchor(), rotateVector(direction, theta), 2); // TODO check xp

        theta = random.nextGaussian(Math.PI, 1);
        if (theta<PI/2) theta = PI/2;
        if (theta>3*PI/2) theta = 3*PI/2;
        new CollectibleModel(getAnchor(), rotateVector(direction, theta), 2);
    }



    @Override
    public void onCollision(Collidable other, Point2D intersection) {
        if (other instanceof EpsilonModel) impact(relativeLocation(intersection, anchor), intersection, other);
        if (other instanceof BulletModel) {
            impact(relativeLocation(intersection, anchor), intersection, other, 6200);
            createImpactWave(this, other, intersection);
        }
    }

    @Override
    public void onCollision(Collidable other, Point2D coll1, Point2D coll2) {
        ((Impactable) this).impact(coll1, coll2, other);


    }


}