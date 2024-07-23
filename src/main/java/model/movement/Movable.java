package model.movement;

import model.charactersModel.BulletModel;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;

public interface Movable {
    CopyOnWriteArrayList<Movable> movables = new CopyOnWriteArrayList<>();
    boolean isCircular();
    void setDirection(Direction direction);
    void bulletImpact(BulletModel bulletModel, Point2D collisionPoint);
    Direction getDirection();
    void move(Direction direction);
    void move();
    void friction();
    Point2D getAnchor();

    }