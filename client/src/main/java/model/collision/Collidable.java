package model.collision;

import model.FinalPanelModel;
import model.charactersModel.*;
//import view.MainPanel;
//import view.Panel;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

//import static controller.GameLoop.EPSILON_MELEE_DAMAGE;
//import static controller.GameLoop.EPSILON_RANGED_DAMAGE;
import static controller.Utils.*;
import static model.collision.Impactable.impactables;

public interface Collidable {
    CopyOnWriteArrayList<Collidable> collidables = new CopyOnWriteArrayList<>();

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
            if (!(collidable instanceof FinalPanelModel) && !(this instanceof FinalPanelModel)) findSingleIntersectionPoint(collidable);
//            handlePolygonPolygonCollision(collidable);
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

//    private void handleBulletImpact(Point2D intersection, Collidable collidable) {
//        // todo edit
//        if (collidable instanceof SquarantineModel) {
//            ((SquarantineModel) collidable).damage(Profile.getCurrent().EPSILON_RANGED_DAMAGE);
//        } else if (collidable instanceof TrigorathModel) {
//            ((TrigorathModel) collidable).damage(Profile.getCurrent().EPSILON_RANGED_DAMAGE);
//        }
//        ((BulletModel) this).bulletImpact((BulletModel) this, intersection, collidable);
//    }

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
                    // todo yum ...
//                    ((SquarantineModel) collidable).damage(Profile.getCurrent().EPSILON_MELEE_DAMAGE);
                } else if (collidable instanceof TrigorathModel) {
//                    ((TrigorathModel) collidable).damage(Profile.getCurrent().EPSILON_MELEE_DAMAGE);
                }
            }
        }




        for (Impactable coll : impactables) {
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
        if (!(collidable instanceof FinalPanelModel) && !(this instanceof FinalPanelModel)) {
            findSingleIntersectionPoint(collidable);
        }
    }

    // Method to find all intersection points between two polygons
    private static Point2D getIntersectionPoint(Line2D line1, Line2D line2) {
        if (!line1.intersectsLine(line2)) return null;

        double px = line1.getX1(),
                py = line1.getY1(),
                rx = line1.getX2() - px,
                ry = line1.getY2() - py;
        double qx = line2.getX1(),
                qy = line2.getY1(),
                sx = line2.getX2() - qx,
                sy = line2.getY2() - qy;

        double det = rx * sy - ry * sx;
        if (det == 0) {
            return null;  // Lines are parallel
        } else {
            double t = ((qx - px) * sy - (qy - py) * sx) / det;
            return new Point2D.Double(px + t * rx, py + t * ry);
        }
    }


    default Point2D closestPointOnPolygon(Point2D point, Point2D[] vertices){
        double minDistance = Double.MAX_VALUE;
        Point2D closest = null;
        for (int i=0;i<vertices.length;i++){

            Point2D temp = getClosestPointOnSegment(vertices[i],vertices[(i+1)%vertices.length],point);
            double distance = temp.distance(point);
            if (distance < minDistance) {
                minDistance = distance;
                closest = temp;
            }
        }
        return closest;
    }

    default void findSingleIntersectionPoint(Collidable poly2) {
        List<Point2D> intersectionPoints = new ArrayList<>();
        List<Intersection> intersectionsOfPoly1 = new ArrayList<>();
        List<Intersection> intersectionsOfPoly2 = new ArrayList<>();

        Point2D collisionPointOfPoly1 ;
        Point2D collisionNormalVectorOfPoly1 ;

        Point2D collisionPointOfPoly2;
        Point2D collisionNormalVectorOfPoly2;

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
        if  (intersectionPoints.size() == 2) {
            Intersection point1 = intersectionsOfPoly1.get(0);
            Intersection point2 = intersectionsOfPoly1.get(1);
            if (point1.edge == point2.edge) {
                collisionPointOfPoly1 =
                        findMidPoint(point1.collision, point2.collision);
                collisionNormalVectorOfPoly1 = normalizeVector(perpendicularClockwise(edges1[point1.edge]));
            } else {
                int vertex;
                if ((point1.edge + 1) % vertices1.length == point2.edge) {
                    vertex = point1.edge;
                } else {
                    vertex = point2.edge;
                }
                double distance1 = findDistance(point1.collision, vertices1[vertex]);
                double distance2 = findDistance(point2.collision, vertices1[vertex]);

                if (distance1 < distance2) {
//                    collisionPointOfPoly1 = point2.collision;
                    collisionPointOfPoly1 = vertices1[vertex];

                    collisionNormalVectorOfPoly1 = normalizeVector(perpendicularClockwise(vertices1[vertex]));
                } else {
                    collisionPointOfPoly1 = vertices1[vertex];
//                    collisionPointOfPoly1 = point1.collision;
                    collisionNormalVectorOfPoly1 = normalizeVector(perpendicularClockwise(vertices1[vertex]));
                }
            }
            Intersection intersection1 = intersectionsOfPoly2.get(0);
            Intersection intersection2 = intersectionsOfPoly2.get(1);
            if (intersection1.edge == intersection2.edge) {
                collisionPointOfPoly2 = findMidPoint(intersection1.collision, intersection2.collision);
                int edgeNumber = intersection1.edge;
                Point2D intersectingEdge = relativeLocation(vertices2[(edgeNumber + 1) % vertices2.length], vertices2[edgeNumber]);
                collisionNormalVectorOfPoly2 = normalizeVector(perpendicularClockwise(intersectingEdge));
            } else {
                int firstEdgeNumber = intersection1.edge;
                int secondEdgeNumber = intersection2.edge;
                int vertex;
                if ((firstEdgeNumber + 1) % vertices2.length == secondEdgeNumber) {
                    vertex = secondEdgeNumber;
                } else {
                    vertex = firstEdgeNumber;
                }
                double distance1 = findDistance(intersection1.collision, vertices2[vertex]);
                double distance2 = findDistance(intersection2.collision, vertices2[vertex]);
                if (distance1 < distance2) {
//                    collisionPointOfPoly2 = intersection2.collision;
                    collisionPointOfPoly2 = vertices2[vertex];
                    int edgeNumber = intersection2.edge;
                    Point2D intersectingEdge = relativeLocation(vertices2[(edgeNumber + 1) % vertices2.length], vertices2[edgeNumber]);
                    collisionNormalVectorOfPoly2 = normalizeVector(perpendicularClockwise(intersectingEdge));
                } else {
                    collisionPointOfPoly2 = vertices2[vertex];
//                    collisionPointOfPoly2 = intersection1.collision;
                    int edgeNumber = intersection1.edge;
                    Point2D intersectingEdge = relativeLocation(vertices2[(edgeNumber + 1) % vertices2.length], vertices2[edgeNumber]);
                    collisionNormalVectorOfPoly2 = normalizeVector(perpendicularClockwise(intersectingEdge));
                }
            }

            poly2.onCollision(this, collisionNormalVectorOfPoly2, collisionPointOfPoly2);
            this.onCollision(poly2, collisionNormalVectorOfPoly1, collisionPointOfPoly1);



        }
    }


    default void createImpactWave(Collidable coll1, Collidable poly2, Point2D collisionPointOfPoly1){
        for (Impactable coll : impactables) {
                if (!(coll == poly2) && !(coll == this) ) {
                        coll.impact(new CollisionState(collisionPointOfPoly1));
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
    void onCollision(Collidable other, Point2D coll1, Point2D coll2);
}
