package game.model.collision;

import java.awt.geom.Point2D;
import java.util.concurrent.CopyOnWriteArrayList;

public interface Impactable {
    CopyOnWriteArrayList<Impactable> impactables = new CopyOnWriteArrayList<>();
    boolean isImpactInProgress();
    void setImpactInProgress(boolean impactInProgress);
    void impact(CollisionState collisionState);
    void impact(Point2D normalVector, Point2D collisionPoint, Collidable collidable);
    double getImpactCoefficient(Point2D collisionRelativeVector);
    void banish();
}
