package model.collision;

import controller.Game;
import model.FinalPanelModel;
import model.charactersModel.*;
//import view.MainPanel;
//import view.Panel;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import static controller.GameLoop.EPSILON_MELEE_DAMAGE;
import static controller.GameLoop.EPSILON_RANGED_DAMAGE;
import static controller.Utils.*;

public interface Collidable {
    ArrayList<Collidable> collidables = new ArrayList<>();

    boolean isCircular();
    double getRadius();
    Point2D getAnchor();
    Point2D[] getVertices();
    ArrayList<Line2D> getEdges();

    default void checkCollision(Collidable collidable) {
        if (isCircular() && !collidable.isCircular()) {
            handleCirclePolygonCollision(collidable);
        } else if (!isCircular() && collidable.isCircular()) {
            collidable.checkCollision(this);
        } else if (!isCircular() && !collidable.isCircular()) {
            handlePolygonPolygonCollision(collidable);
//            this.onCollision(collidable);
//            collidable.onCollision(this);
        } else if (isCircular() && collidable.isCircular()) {
            handleCircleCircleCollision(collidable);
        }
    }

    private void handleCircleCircleCollision(Collidable collidable) {
        double distance = findDistance(getAnchor(), collidable.getAnchor());
        if (distance < getRadius() + collidable.getRadius()) {

            Point2D intersection = findCircleCircleIntersection(this, collidable);

            this.onCollision(collidable, intersection);
            collidable.onCollision(this, intersection);
        }

    }


    private void handleCirclePolygonCollision(Collidable collidable) {
        if (collidable instanceof FinalPanelModel) {
            Point2D intersection = findClosestPointOnEdges(getAnchor(), collidable.getEdges());
            if (intersection != null) { // todo find a way to remove this nonsense if statement!
                if (intersection.distance(getAnchor()) <= getRadius()) { // todo change radius by an offset amount
                    this.onCollision(collidable, intersection);
                    collidable.onCollision(this, intersection);
                }
            }
        }

        else {
//            System.out.println("BARICAAAAAAAAAAAA");
            Point2D intersection = closestPointOnPolygon(getAnchor(), collidable.getVertices()); // todo this calculation most be done with edges when dealing with FinalPanel
            if (intersection.distance(getAnchor()) <= getRadius()) { // todo change radius by an offset amount

                this.onCollision(collidable, intersection);
                collidable.onCollision(this, intersection);
            }
        }
    }

    private static Point2D findCircleCircleIntersection(Collidable c1, Collidable c2) {
        double x1 = c1.getAnchor().getX();
        double y1 = c1.getAnchor().getY();
        double r1 = c1.getRadius();
        double x2 = c2.getAnchor().getX();
        double y2 = c2.getAnchor().getY();
        double r2 = c2.getRadius();

        double dx = x2 - x1;
        double dy = y2 - y1;
        double d = Math.sqrt(dx * dx + dy * dy);

        // Find the distance from the center of the first circle to the intersection points
        double a = (r1 * r1 - r2 * r2 + d * d) / (2 * d);
        double h = Math.sqrt(r1 * r1 - a * a);

        // Find the midpoint between the intersection points
        double x0 = x1 + a * (x2 - x1) / d;
        double y0 = y1 + a * (y2 - y1) / d;

        // Find the actual intersection points
        double rx = -h * (y2 - y1) / d;
        double ry = h * (x2 - x1) / d;

        Point2D intersection1 = new Point2D.Double(x0 + rx, y0 + ry);
        Point2D intersection2 = new Point2D.Double(x0 - rx, y0 - ry);

        return findMidPoint(intersection1, intersection2);
    }

    private void handleBulletImpact(Point2D intersection, Collidable collidable) {
        if (collidable instanceof SquarantineModel) {
            ((SquarantineModel) collidable).damage(EPSILON_RANGED_DAMAGE);
        } else if (collidable instanceof TrigorathModel) {
            ((TrigorathModel) collidable).damage(EPSILON_RANGED_DAMAGE);
        }
        ((BulletModel) this).bulletImpact((BulletModel) this, intersection, collidable);
    }

    private void handleCirclePolygonImpact(Point2D intersection, Collidable collidable) {
        double minDistance = Double.MAX_VALUE;
        for (Point2D vertex : collidable.getVertices()) {
            minDistance = Math.min(minDistance, intersection.distance(vertex));
        }

        if (minDistance == 0) {
            if (collidable instanceof SquarantineModel) {
                EpsilonModel.getINSTANCE().damage(6);
            } else if (collidable instanceof TrigorathModel) {
                EpsilonModel.getINSTANCE().damage(10);
            }
        }

        EpsilonModel epsilon = EpsilonModel.getINSTANCE();
        if (epsilon.vertices != null) {
            for (Point2D vertex : epsilon.vertices) {
                minDistance = Math.min(minDistance, intersection.distance(vertex));
            }
            if (minDistance < 5) {
                if (collidable instanceof SquarantineModel) {
                    ((SquarantineModel) collidable).damage(EPSILON_MELEE_DAMAGE);
                } else if (collidable instanceof TrigorathModel) {
                    ((TrigorathModel) collidable).damage(EPSILON_MELEE_DAMAGE);
                }
            }
        }




        for (Collidable coll : collidables) {
            if (!(coll instanceof CollectibleModel)) {
                if (coll == collidable) {
                    ((Impactable) collidable).impact(relativeLocation(intersection, getAnchor()), intersection, this);
                } else if (coll == this) {
                    ((Impactable) this).impact(relativeLocation(getAnchor(), intersection), intersection, collidable);
                } else {
//                    ((Impactable) coll).impact(new CollisionState(intersection)); // todo
                }
            }
        }

//        if (collidable instanceof MainPanel || collidable instanceof Panel) {
//            ((Impactable) this).impact(relativeLocation(getAnchor(), intersection), intersection, collidable);
//        }
    }

    private void handlePolygonPolygonCollision(Collidable collidable) {
//        if (!(collidable instanceof MainPanel) && !(this instanceof MainPanel)) {
//            findSingleIntersectionPoint(collidable);
//        }
    }

    private static Point2D getIntersectionPoint(Line2D line1, Line2D line2) {
        if (!line1.intersectsLine(line2)) return null;

        double px = line1.getX1();
        double py = line1.getY1();
        double rx = line1.getX2() - px;
        double ry = line1.getY2() - py;
        double qx = line2.getX1();
        double qy = line2.getY1();
        double sx = line2.getX2() - qx;
        double sy = line2.getY2() - qy;

        double det = rx * sy - ry * sx;
        if (det == 0) {
            return null;  // Lines are parallel
        } else {
            double t = ((qx - px) * sy - (qy - py) * sx) / det;
            return new Point2D.Double(px + t * rx, py + t * ry);
        }
    }

    private void findSingleIntersectionPoint(Collidable poly2) {
        List<Point2D> intersectionPoints = new ArrayList<>();
        List<Intersection> intersectionsOfPoly1 = new ArrayList<>();
        List<Intersection> intersectionsOfPoly2 = new ArrayList<>();

        Point2D[] vertices1 = this.getVertices();
        Point2D[] vertices2 = poly2.getVertices();
        Point2D[] edges1 = new Point2D[vertices1.length];
        Point2D[] edges2 = new Point2D[vertices2.length];

        for (int i = 0; i < vertices1.length; i++) {
            edges1[i] = relativeLocation(vertices1[(i + 1) % vertices1.length], vertices1[i]);
        }

        for (int i = 0; i < vertices2.length; i++) {
            edges2[i] = relativeLocation(vertices2[(i + 1) % vertices2.length], vertices2[i]);
        }

        for (int i = 0; i < vertices1.length; i++) {
            Line2D line1 = new Line2D.Double(vertices1[i], vertices1[(i + 1) % vertices1.length]);

            for (int j = 0; j < vertices2.length; j++) {
                Line2D line2 = new Line2D.Double(vertices2[j], vertices2[(j + 1) % vertices2.length]);
                Point2D intersection = getIntersectionPoint(line1, line2);
                if (intersection != null) {
                    intersectionPoints.add(intersection);
                    intersectionsOfPoly1.add(new Intersection(intersection, i, this));
                    intersectionsOfPoly2.add(new Intersection(intersection, j, poly2));
                }
            }
        }

        if (intersectionPoints.size() == 2) {
            handlePolygonIntersection(intersectionsOfPoly1, intersectionsOfPoly2, edges1, edges2, vertices1, vertices2, poly2);
        }
    }

    private void handlePolygonIntersection(
            List<Intersection> intersectionsOfPoly1,
            List<Intersection> intersectionsOfPoly2,
            Point2D[] edges1,
            Point2D[] edges2,
            Point2D[] vertices1,
            Point2D[] vertices2,
            Collidable poly2) {

        Point2D collisionPointOfPoly1;
        Point2D collisionNormalVectorOfPoly1;
        Point2D collisionPointOfPoly2;
        Point2D collisionNormalVectorOfPoly2;

        Intersection point1 = intersectionsOfPoly1.get(0);
        Intersection point2 = intersectionsOfPoly1.get(1);

        if (point1.edge == point2.edge) {
            collisionPointOfPoly1 = findMidPoint(point1.collision, point2.collision);
            collisionNormalVectorOfPoly1 = normalizeVector(perpendicularClockwise(edges1[point1.edge]));
        } else {
            collisionPointOfPoly1 = handleVertexCollision(vertices1, point1, point2);
            collisionNormalVectorOfPoly1 = normalizeVector(perpendicularClockwise(vertices1[point1.edge]));
        }

        Intersection intersection1 = intersectionsOfPoly2.get(0);
        Intersection intersection2 = intersectionsOfPoly2.get(1);

        if (intersection1.edge == intersection2.edge) {
            collisionPointOfPoly2 = findMidPoint(intersection1.collision, intersection2.collision);
            int edgeNumber = intersection1.edge;
            Point2D intersectingEdge = relativeLocation(vertices2[(edgeNumber + 1) % vertices2.length], vertices2[edgeNumber]);
            collisionNormalVectorOfPoly2 = normalizeVector(perpendicularClockwise(intersectingEdge));
        } else {
            collisionPointOfPoly2 = handleVertexCollision(vertices2, intersection1, intersection2);
            collisionNormalVectorOfPoly2 = normalizeVector(perpendicularClockwise(vertices2[intersection1.edge]));
        }

        for (Collidable coll : collidables) {
            if (coll == poly2) {
                ((Impactable) poly2).impact(collisionNormalVectorOfPoly1, collisionPointOfPoly1, this);
            } else if (coll == this) {
                ((Impactable) this).impact(collisionNormalVectorOfPoly2, collisionPointOfPoly2, poly2);
            } else {
                ((Impactable) coll).impact(new CollisionState(findMidPoint(collisionPointOfPoly1, collisionPointOfPoly2)));
            }
        }
    }

    private Point2D handleVertexCollision(Point2D[] vertices, Intersection point1, Intersection point2) {
        if ((point1.edge + 1) % vertices.length == point2.edge) {
            return findMidPoint(point1.collision, vertices[point2.edge]);
        } else {
            return findMidPoint(point2.collision, vertices[point1.edge]);
        }
    }

    record Intersection(Point2D collision, int edge, Collidable collidable) {}
    void onCollision(Collidable other, Point2D intersection);
    void onCollision(Collidable other);
}
