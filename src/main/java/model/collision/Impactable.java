package model.collision;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public interface Impactable {
    static ArrayList<Impactable> impactables = new ArrayList<>();
    boolean isImpactInProgress();
    void setImpactInProgress(boolean impactInProgress);
    void impact(CollisionState collisionState);
    void impact(Point2D normalVector, Point2D collisionPoint, Collidable collidable);
    double getImpactCoefficient(Point2D collisionRelativeVector);
    void banish();
}
