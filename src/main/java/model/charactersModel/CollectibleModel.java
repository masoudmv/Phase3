package model.charactersModel;
import model.collision.Collidable;
import model.movement.Direction;
import model.movement.Movable;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.UUID;

import static controller.constants.Constants.BULLET_RADIUS;
import static controller.UserInterfaceController.*;
import static controller.Game.ELAPSED_TIME;
import static controller.Utils.*;
import static controller.Utils.addVectors;

public class CollectibleModel implements Collidable, Movable {
    String id;
    double radius;
    private Point2D anchor;
    public static LinkedList<CollectibleModel> collectibleModels = new LinkedList<>();
    public Direction direction;
    public boolean impactInProgress;
    public double impactMaxVel;
    public double birthTime;


    public CollectibleModel(Point2D anchor, Point2D direction) {
        birthTime = ELAPSED_TIME;
        this.radius = BULLET_RADIUS;
        this.id= UUID.randomUUID().toString();
        this.anchor = anchor;
        this.direction = new Direction(direction);
        this.direction.adjustDirectionMagnitude();
        impactInProgress = true;
        impactMaxVel = 1.75;
        collectibleModels.add(this);
        collidables.add(this);
        createCollectibleView(id);
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean isCircular() {
        return true;
    }

    @Override
    public void setDirection(Direction direction) {

    }

    @Override
    public void bulletImpact(BulletModel bulletModel, Point2D collisionPoint) {

    }

    @Override
    public Direction getDirection() {
        return this.direction;
    }

    @Override
    public void move(Direction direction) {
        Point2D movement = multiplyVector(direction.getDirectionVector(), direction.getMagnitude());
        this.anchor = addVectors(anchor, movement);

    }

    @Override
    public void move() {
        move(direction);
    }

    @Override
    public void friction() {
        direction.setMagnitude(direction.getMagnitude() * 0.97);
        if (direction.getMagnitude() < 1){
            direction.setMagnitude(0);
            impactInProgress = false;

        }
    }

    @Override
    public double getRadius() {
        return this.radius;
    }

    @Override
    public Point2D getAnchor() {
        return this.anchor;
    }

    @Override
    public Point2D[] getVertices() {
        return getVertices();
    }

    @Override
    public ArrayList<Line2D> getEdges() {
        return null;
    }

    @Override
    public void onCollision(Collidable other, Point2D intersection) {

    }

    @Override
    public void onCollision(Collidable other) {

    }

    public void setRadius(double radius) {
        this.radius = radius;
    }



    public void remove(){
        collidables.remove(this);movables.remove(this);
        collectibleModels.remove(this);
        findCollectibleView((this).getId()).remove();
    }


}
