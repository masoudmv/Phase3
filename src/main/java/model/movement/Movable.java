package model.movement;

import model.BulletModel;
import model.collision.Collidable;
import model.collision.CollisionState;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;

import static controller.Constants.*;
import static controller.Utils.*;
import static controller.Utils.addVectors;
import static model.collision.Collidable.collidables;

public interface Movable {
    boolean isCircular();
    LinkedList<Movable> movables = new LinkedList<>();
    void setDirection(Direction direction);
    void bulletImpact(BulletModel bulletModel, Point2D collisionPoint);

    Direction getDirection();
    void move(Direction direction);
    void move();
    void friction();
    Point2D getAnchor();
    }