
package game.model.charactersModel;

import game.controller.GameType;
import game.controller.UserInterfaceController;
import game.controller.Utils;
import game.model.entities.AttackTypes;
import game.model.reflection.Enemy;
import shared.constants.Constants;
import shared.constants.EntityConstants;
import shared.model.MyPolygon;
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

import static shared.model.imagetools.ToolBox.getBufferedImage;

public class TrigorathModel extends GeoShapeModel implements Movable, Collidable, Impactable, Enemy {
    public double impactMaxVelocity;

    private boolean impactInProgress = false;
    private double angularVelocity;
    private double angularAcceleration;

    static BufferedImage image;
    private static double edgeLength;

    public TrigorathModel(Point2D anchor, String gameID) {
        super(anchor, image, new MyPolygon(new double[3], new double[3], 3), gameID);
        setTarget();
        this.gameID = gameID;
        initVertices();
        findGame(gameID).trigorathModels.add(this);
        collidables.add(this);
        movables.add(this);
        impactables.add(this);

        this.health = 15;
        damageSize.put(AttackTypes.MELEE, 10);

        UserInterfaceController.createTrigorathView(id, gameID);
    }

    public TrigorathModel() {
        super();
    }

    private void initVertices(){
        double radius = edgeLength / Math.sqrt(3);
        Point2D.Double point1 = new Point2D.Double(anchor.getX(), anchor.getY()-radius);
        Point2D.Double point2 = new Point2D.Double(anchor.getX()+ radius*Math.cos(Math.PI/6), anchor.getY()+radius/2);
        Point2D.Double point3 = new Point2D.Double(anchor.getX()-radius*Math.cos(Math.PI/6), anchor.getY()+radius/2);
        Point2D.Double[] vertices = new Point2D.Double[]{point1, point2, point3};
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
        double distanceByEpsilon = getAnchor().distance(target.getAnchor());
        if (distanceByEpsilon< Constants.TRIGORATH_MAX_VEL_RADIUS) {
            Point2D collisionPoint = collisionState.collisionPoint;
            Point2D collisionRelativeVector = Utils.relativeLocation(this.getAnchor(), collisionPoint);
            double impactCoefficient = getImpactCoefficient(collisionRelativeVector);
//        impactCoefficient *= 2;
            Point2D impactVector = Utils.normalizeVector(Utils.relativeLocation(this.getAnchor(), collisionState.collisionPoint));
            impactVector = Utils.multiplyVector(impactVector, impactCoefficient);
            Point2D r2 = Utils.addVectors(this.getDirection().getNormalizedDirectionVector(), impactVector);

            Direction direction = new Direction(Utils.normalizeVector(r2));
            this.setDirection(direction);
        }
        else {
            Point2D collisionPoint = collisionState.collisionPoint;
            Point2D collisionRelativeVector = Utils.relativeLocation(this.getAnchor(), collisionPoint);
            double impactCoefficient = getImpactCoefficient(collisionRelativeVector);
//        impactCoefficient *= 2;
            Point2D impactVector = Utils.normalizeTrigorathVector(Utils.relativeLocation(this.getAnchor(), collisionState.collisionPoint));
            impactVector = Utils.multiplyVector(impactVector, impactCoefficient);
            Point2D r2 = Utils.addVectors(this.getDirection().getTrigorathNormalizedDirectionVector(), impactVector);

            Direction direction = new Direction(Utils.normalizeTrigorathVector(r2));
            this.setDirection(direction);
        }


    }


    public void impact(Point2D normalVector, Point2D collisionPoint, Collidable polygon) {
        double distanceByEpsilon = getAnchor().distance(target.getAnchor());
        if (distanceByEpsilon < Constants.TRIGORATH_MAX_VEL_RADIUS) {
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
        setAngularMotion(collisionPoint, polygon, 34000);
        createImpactWave(this, polygon, collisionPoint);

    }



    //todo duplicated version. needs to be eiminated ...
    public void impact(Point2D normalVector, Point2D collisionPoint, Collidable polygon, double inertia) {
        double distanceByEpsilon = getAnchor().distance(target.getAnchor());
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

    @Override
    public double getImpactCoefficient(Point2D collisionRelativeVector) {
        double impactCoefficient;
        double distanceByEpsilon = getAnchor().distance(target.getAnchor());
        if (distanceByEpsilon< Constants.TRIGORATH_MAX_VEL_RADIUS) {
            double distance = Math.hypot(collisionRelativeVector.getX(), collisionRelativeVector.getY());
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
                impactMaxVelocity = 2 * coefficient * impactCoefficient / 5;
            }
        } else {
            double distance = Math.hypot(collisionRelativeVector.getX(), collisionRelativeVector.getY());
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
                impactMaxVelocity = 2 * coefficient * impactCoefficient / 5;
            }
        }


        return impactCoefficient;
    }

    @Override
    public void banish() {
        Point2D collisionPoint = target.getAnchor();
        Point2D collisionRelativeVector = Utils.relativeLocation(this.getAnchor(), collisionPoint);
        double distance = Math.hypot(collisionRelativeVector.getX(), collisionRelativeVector.getY());
        double impactCoefficient;
        if (distance < 100) {
            setImpactInProgress(true);
            impactMaxVelocity = 3 * Constants.IMPACT_COEFFICIENT / 5;
            impactCoefficient = Constants.IMPACT_COEFFICIENT;
        } else if (distance > 400) {
            setImpactInProgress(false);
            impactCoefficient = 0;
        } else {
            setImpactInProgress(true);
            double coefficient = 1 - (distance- 100)/(500 - 100);
            impactCoefficient = coefficient * Constants.IMPACT_COEFFICIENT;
            impactMaxVelocity = 3* coefficient * impactCoefficient / 5;
        }
//         impactCoefficient = getImpactCoefficient(collisionRelativeVector);
//        impactCoefficient *= 2;
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
        return this.myPolygon.getVertices();
    }

    @Override
    public ArrayList<Line2D> getEdges() {
        return null;
    } // todo implement

    @Override
    public void update(Direction direction) {
        setTarget();
        double distanceByEpsilon = getAnchor().distance(target.getAnchor());
        Point2D movement = Utils.multiplyVector(direction.getDirectionVector(), direction.getMagnitude());
        double magnitude = getDirection().getMagnitude();
//        this.anchor = addVectors(anchor, movement);


//        for (int i = 0; i < 3; i++) {
//            vertices[i] = addVectors(vertices[i], movement);
//        }
        // TODO !isImpactInProgress???

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
//        System.out.println(getDirection().getMagnitude());

        direction.setMagnitude(direction.getMagnitude() * Constants.FRICTION);
        double distanceByEpsilon = getAnchor().distance(target.getAnchor());
        if (direction.getMagnitude() < 1) {
//            System.out.println("ddsdsd");
            setDirection(
                    new Direction(Utils.relativeLocation(target.getAnchor(), getAnchor())));
            getDirection().adjustDirectionMagnitude();
            setImpactInProgress(false);

        }

        if (distanceByEpsilon > Constants.TRIGORATH_MAX_VEL_RADIUS && direction.getMagnitude() < 1.5){
//            System.out.println("ddsdsd");
            setDirection(
                    new Direction(Utils.relativeLocation(target.getAnchor(), getAnchor())));
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
    private double calculateTrigorathInertia(){
        double mass = 100;
        double length = Constants.TRIGORATH_RADIUS* (1.5);
//        System.out.println(mass*length*length/12);
        return 50000;
    }

    @Override
    public ArrayList<Point2D> getBoundingPoints(){;
        ArrayList<Point2D> bound = new ArrayList<>();
        for (int i = 0; i < myPolygon.npoints; i++) {
            bound.add( new Point2D.Double(myPolygon.xpoints[i], myPolygon.ypoints[i]) );
            bound.add( new Point2D.Double(myPolygon.xpoints[i], myPolygon.ypoints[i]) );
        } return bound;
    }


    public void eliminate(){
        super.eliminate();

        collidables.remove(this);
        findGame(gameID).trigorathModels.remove(this);
        movables.remove(this);

        findGame(gameID).enemyEliminated();


        CollectibleModel.dropCollectible(getAnchor(),
                EntityConstants.TRIGORATH_NUM_OF_COLLECTIBLES.getValue(),
                EntityConstants.TRIGORATH_COLLECTIBLES_XP.getValue(),
                gameID
        );


    }

    public void create(String gameID) {
        Point2D anchor;
        boolean isValid;
        double MIN_DISTANCE = 100.0; // The minimum distance to avoid collision
        int maxAttempts = 100;
        int attempts = 0;

        do {
            isValid = true;
            anchor = findRandomPoint();
            attempts++;

            for (GeoShapeModel model : findGame(gameID).entities) {
                double distance = model.getAnchor().distance(anchor);
                if (distance < MIN_DISTANCE) {
                    isValid = false;
                    break;
                }
            }
        } while (!isValid && attempts < maxAttempts);

        if (isValid) {
            // Add the new enemy to the game's entities
            GameType type = findGame(gameID).getGameType();
            switch (type) {
                case monomachia: {
                    Point2D pivot = getSymmetricPoint(anchor);
                    new TrigorathModel(pivot, gameID);
                    new TrigorathModel(anchor, gameID);
                    break;
                }
                case colosseum:{
                    new TrigorathModel(anchor, gameID);
                    break;
                }
            }


        } else {
            System.out.println("Failed to create Trigorath without intersection after " + maxAttempts + " attempts.");
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
        if (other instanceof EpsilonModel) {
            handleDamageEpsilon((EpsilonModel) other);
            impact(Utils.relativeLocation(intersection, anchor), intersection, other);
        }
        if (other instanceof Orb) impact(Utils.relativeLocation(intersection, anchor), intersection, other);
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