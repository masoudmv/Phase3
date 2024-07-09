package model.collision;
import controller.Game;
import model.BulletModel;
import model.CollectibleModel;
import model.charactersModel.EpsilonModel;
import model.charactersModel.SquarantineModel;
import model.charactersModel.TrigorathModel;
import view.MainPanel;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

//import static controller.SoundHandler.doPlay;
//import static controller.SoundHandler.playSound;
import static controller.Sound.playBubble;
import static controller.Update.epsilonMeleeDamage;
import static controller.Update.epsilonRangedDamage;
import static controller.Utils.*;

public interface Collidable {
    ArrayList<Collidable> collidables = new ArrayList<>();
    boolean isCircular();
    double getRadius();
//    double getRadius();
    Point2D getAnchor();
    Point2D[] getVertices();


    default void collides(Collidable collidable){
        if (isCircular() && !collidable.isCircular() && !(this instanceof CollectibleModel )){
            Point2D intersection = closestPointOnPolygon(getAnchor(), collidable.getVertices());

            if (intersection.distance(getAnchor()) <= getRadius()){
                if (this instanceof BulletModel) {

                    if (!(collidable instanceof MainPanel)){
                    }
                    if (collidable instanceof  SquarantineModel) ((SquarantineModel)collidable).damage(epsilonRangedDamage);
                    if (collidable instanceof  TrigorathModel) ((TrigorathModel)collidable).damage(epsilonRangedDamage);
                    ((BulletModel) this).bulletImpact((BulletModel) this, intersection, collidable);
                    

                }
                else if (!(collidable instanceof MainPanel) && !(this instanceof MainPanel)) {

                    double minDistance = Double.MAX_VALUE;
                    for (Point2D vertex: collidable.getVertices()){
                        if (intersection.distance(vertex)<minDistance) minDistance=intersection.distance(vertex);
                    } if (minDistance==0 && collidable instanceof SquarantineModel) EpsilonModel.getINSTANCE().damage(6);
                    if (minDistance==0 && collidable instanceof TrigorathModel) EpsilonModel.getINSTANCE().damage(10);

                    minDistance = Double.MAX_VALUE;
                    EpsilonModel epsilon = EpsilonModel.getINSTANCE();
                    if (epsilon.vertices != null) {
                        for (Point2D vertex: epsilon.vertices){
                            if (intersection.distance(vertex)<minDistance) minDistance=intersection.distance(vertex);
                        }
                        if (minDistance<5 && collidable instanceof SquarantineModel) ((SquarantineModel)collidable).damage(epsilonMeleeDamage);
                        if (minDistance<5 && collidable instanceof TrigorathModel) ((TrigorathModel)collidable).damage(epsilonMeleeDamage);
                    }


                    for (Collidable coll : collidables){
                        if (!(coll instanceof CollectibleModel)) {
                            if (coll == collidable){
                                ((Impactable) collidable).impact(relativeLocation(intersection, getAnchor()), intersection, this);
                            } else if (coll == this){
                                ((Impactable) this).impact(relativeLocation(getAnchor(), intersection), intersection, collidable);
                            } else {
                                ((Impactable) coll).impact(new CollisionState(intersection));

                            }
                        }
                    }
                } else if (collidable instanceof MainPanel) {

                    ((Impactable) this).impact(relativeLocation(getAnchor(), intersection), intersection, collidable);
                }
            }
        } else if (!isCircular() && collidable.isCircular()){
            collidable.collides(this);
        } else if (!isCircular() && !collidable.isCircular()){
            if (!(collidable instanceof MainPanel) && !(this instanceof MainPanel)) findSingleIntersectionPoint(collidable);
        }


        if (isCircular() && collidable.isCircular() && (!(collidable instanceof BulletModel)) && (!(this instanceof BulletModel))){
            if (this instanceof CollectibleModel && collidable instanceof CollectibleModel){}
            else {
                Point2D dist = relativeLocation(getAnchor(), collidable.getAnchor());
                double distance = Math.hypot(dist.getX(), dist.getY());
                if (distance < getRadius()+collidable.getRadius()){
                    Game.getINSTANCE().sumInGameXpWith(5);
                    ((CollectibleModel)collidable).remove();

                }
            }
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

    default Point2D getClosestPointOnSegment(Point2D head1, Point2D head2, Point2D point) {
        double u =((point.getX()-head1.getX())*(head2.getX()-head1.getX())+(point.getY()-head1.getY())*(head2.getY()-head1.getY()))/head2.distanceSq(head1);
        if (u > 1.0) return (Point2D) head2.clone();
        else if (u <= 0.0) return (Point2D) head1.clone();
        else return new Point2D.Double(head2.getX()* u + head1.getX() * (1.0 - u) + 0.5,head2.getY() * u + head1.getY() * (1.0 - u) + 0.5);
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
            if (point1.edge == point2.edge){
                collisionPointOfPoly1 =
                findMidPoint(point1.collision, point2.collision);
                collisionNormalVectorOfPoly1 = normalizeVector(PerpendicularClockwise(edges1[point1.edge]));
            } else {
                int vertex;
                if ((point1.edge + 1) % vertices1.length == point2.edge){
                    vertex = point1.edge;
                } else {
                    vertex = point2.edge;
                }
                double distance1 = findDistance(point1.collision, vertices1[vertex]);
                double distance2 = findDistance(point2.collision, vertices1[vertex]);

                if (distance1<distance2){
//                    collisionPointOfPoly1 = point2.collision;
                    collisionPointOfPoly1 = vertices1[vertex];

                    collisionNormalVectorOfPoly1 = normalizeVector(PerpendicularClockwise(vertices1[vertex]));
                } else {
                    collisionPointOfPoly1 = vertices1[vertex];
//                    collisionPointOfPoly1 = point1.collision;
                    collisionNormalVectorOfPoly1 = normalizeVector(PerpendicularClockwise(vertices1[vertex]));
                }
            }
            Intersection intersection1 = intersectionsOfPoly2.get(0);
            Intersection intersection2 = intersectionsOfPoly2.get(1);
            if (intersection1.edge == intersection2.edge){
                collisionPointOfPoly2 = findMidPoint(intersection1.collision, intersection2.collision);
                int edgeNumber = intersection1.edge;
                Point2D intersectingEdge = relativeLocation(vertices2[(edgeNumber+1)%vertices2.length], vertices2[edgeNumber]);
                collisionNormalVectorOfPoly2= normalizeVector(PerpendicularClockwise(intersectingEdge));
            } else {
                int firstEdgeNumber = intersection1.edge;
                int secondEdgeNumber = intersection2.edge;
                int vertex;
                if ((firstEdgeNumber+1)%vertices2.length == secondEdgeNumber){
                    vertex = secondEdgeNumber;
                } else {
                    vertex = firstEdgeNumber;
                }
                double distance1 = findDistance(intersection1.collision, vertices2[vertex]);
                double distance2 = findDistance(intersection2.collision, vertices2[vertex]);
                if (distance1<distance2){
//                    collisionPointOfPoly2 = intersection2.collision;
                    collisionPointOfPoly2 = vertices2[vertex];
                    int edgeNumber = intersection2.edge;
                    Point2D intersectingEdge = relativeLocation( vertices2[(edgeNumber+1)%vertices2.length], vertices2[edgeNumber]);
                    collisionNormalVectorOfPoly2 = normalizeVector(PerpendicularClockwise(intersectingEdge));
                } else {
                    collisionPointOfPoly2 = vertices2[vertex];
//                    collisionPointOfPoly2 = intersection1.collision;
                    int edgeNumber = intersection1.edge;
                    Point2D intersectingEdge = relativeLocation( vertices2[(edgeNumber+1)%vertices2.length], vertices2[edgeNumber]);
                    collisionNormalVectorOfPoly2 = normalizeVector(PerpendicularClockwise(intersectingEdge));
                }
            }
            for (Collidable coll : collidables){
                if (!(coll instanceof CollectibleModel)) {
                    if (coll == poly2){
                        ((Impactable) poly2).impact(collisionNormalVectorOfPoly2, collisionPointOfPoly2, this);
                    } else if (coll == this){
                        ((Impactable) this).impact(collisionNormalVectorOfPoly1, collisionPointOfPoly1, poly2);
                    } else {

                        ((Impactable) coll).impact(new CollisionState(collisionPointOfPoly1));


                    }
                }
            }

        }
    }
}