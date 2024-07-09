package controller;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Vector;

public class Utils {
    public static Point2D relativeLocation(Point2D point,Point2D anchor){
        return new Point2D.Double(point.getX()-anchor.getX(),point.getY()-anchor.getY());
    }
    public static Point2D multiplyVector(Point2D point,double scalar){
        return new Point2D.Double(point.getX()*scalar,point.getY()*scalar);
    }
    public static Point2D multiplyVectorXYComponent(Point2D point,double scalarX, double scalarY){
        return new Point2D.Double(point.getX()*scalarX, point.getY() * scalarY);
    }
    public static Point2D addVectors(Point2D point1,Point2D point2){
        return new Point2D.Double(point1.getX()+point2.getX(),point1.getY()+point2.getY());
    }
    public static Point2D weightedAddVectors(Point2D point1,Point2D point2,double weight1,double weight2){
        return multiplyVector(addVectors(multiplyVector(point1,weight1),multiplyVector(point2,weight2)),1/(weight1+weight2));
    }
    public static double dotVectors(Point2D point1, Point2D point2){
        return point1.getX() * point2.getX() + point1.getY() * point2.getY();
    }
    public static Point2D normalizeVector(Point2D point){
        double size = Math.hypot(point.getX(), point.getY());
        double x = point.getX() / size;
        double y = point.getY() / size;
        point.setLocation(x, y);
        return point;
    }

    public static Point2D normalizeTrigorathVector(Point2D point){
        double size = Math.hypot(point.getX(), point.getY());
        double x = point.getX() / size;
        double y = point.getY() / size;
        point.setLocation(x, y);
        return multiplyVector(point, 1.5);
    }
    public static Point2D PerpendicularClockwise(Point2D vector)
    {
        return new Point2D.Double(vector.getY(), -vector.getX());
    }

    public static Point2D PerpendicularCounterClockwise(Point2D vector)
    {
        return new Point2D.Double(-vector.getY(), vector.getX());
    }

//    public static Point2D PerpendicularVector(Line2D line2D)
//    {
//        return new Point2D.Double(-vector.getY(), vector.getX());
//    }

//    public static Point2D getIntersectionPoint(Line2D line1, Line2D line2) {
//        if (!line1.intersectsLine(line2)) return null;
//
//        double px = line1.getX1(),
//                py = line1.getY1(),
//                rx = line1.getX2() - px,
//                ry = line1.getY2() - py;
//        double qx = line2.getX1(),
//                qy = line2.getY1(),
//                sx = line2.getX2() - qx,
//                sy = line2.getY2() - qy;
//
//        double det = rx * sy - ry * sx;
//        if (det == 0) {
//            return null;  // Lines are parallel
//        } else {
//            double t = ((qx - px) * sy - (qy - py) * sx) / det;
//            return new Point2D.Double(px + t * rx, py + t * ry);
//        }
//    }
    public static Point2D findMidPoint(Point2D point1, Point2D point2){
        return new Point2D.Double((point1.getX()+point2.getX())/2, (point1.getY()+point2.getY())/2);
    }
    public static double findDistance(Point2D point1, Point2D point2){
        double dx = point1.getX() - point2.getX();
        double dy = point1.getY() - point2.getY();
        double distance = dx*dx + dy*dy;
        return Math.sqrt(distance);
    }

    public static double calculateVectorMagnitude(Point2D point){
        double dx = point.getX();
        double dy = point.getY();
        double distance = dx*dx + dy*dy;
        return Math.sqrt(distance);
    }

    public static double findAngleBetweenTwoVectors(Point2D u, Point2D v){
        double dotValue = dotVectors(u, v);
        double angle =  dotValue/(calculateVectorMagnitude(u)*calculateVectorMagnitude(v));
        return Math.acos(angle);
    }

    public static Point2D closestPointOnPolygon(Point2D point, Point2D[] vertices){
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

    public static Point2D getClosestPointOnSegment(Point2D head1, Point2D head2, Point2D point) {
        double u =((point.getX()-head1.getX())*(head2.getX()-head1.getX())+(point.getY()-head1.getY())*(head2.getY()-head1.getY()))/head2.distanceSq(head1);
        if (u > 1.0) return (Point2D) head2.clone();
        else if (u <= 0.0) return (Point2D) head1.clone();
        else return new Point2D.Double(head2.getX()* u + head1.getX() * (1.0 - u) + 0.5,head2.getY() * u + head1.getY() * (1.0 - u) + 0.5);
    }


    public static Point2D rotateVector(Point2D vector, double theta){
        double x = vector.getX();
        double y = vector.getY();
        double length = Math.sqrt(x*x + y*y);
        double alpha = Math.atan(y/x);
        double alphaPrime = alpha-theta;
        return new Point2D.Double(length*Math.cos(alphaPrime), length*Math.sin(alphaPrime));
    }



}