package model.movement;

import model.charactersModel.BulletModel;

import java.awt.geom.Point2D;
import java.util.LinkedList;

public interface Movable {
    LinkedList<Movable> movables = new LinkedList<>();
    boolean isCircular();
    void setDirection(Direction direction);
    void bulletImpact(BulletModel bulletModel, Point2D collisionPoint);
    Direction getDirection();
    void move(Direction direction);
    void move();
    void friction();
    Point2D getAnchor();

    }