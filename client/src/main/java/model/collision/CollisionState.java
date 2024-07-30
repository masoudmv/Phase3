package model.collision;

import java.awt.geom.Point2D;

public class CollisionState {
    public Point2D collisionPoint;

    public CollisionState(Point2D collisionPoint) {
        this.collisionPoint = collisionPoint;
    }
}