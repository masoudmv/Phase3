package controller;


import model.FinalPanelModel;
import model.MyPolygon;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import static java.lang.Math.abs;
import static java.lang.Math.min;
import static model.geometry.GeometricToolBox.clockwiseSort;
import static model.geometry.GeometricToolBox.pointsToPolygon;

public class Utils {
    public static Point2D relativeLocation(Point2D point,Point2D anchor){
        return new Point2D.Double(point.getX()-anchor.getX(),point.getY()-anchor.getY());
    }
    public static Point2D multiplyVector(Point2D point,double scalar){
        return new Point2D.Double(point.getX()*scalar,point.getY()*scalar);
    }

    public static Point2D addVectors(Point2D point1,Point2D point2){
        return new Point2D.Double(point1.getX()+point2.getX(),point1.getY()+point2.getY());
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
    public static Point2D perpendicularClockwise(Point2D vector)
    {
        return new Point2D.Double(vector.getY(), -vector.getX());
    }

    public static Point2D PerpendicularCounterClockwise(Point2D vector)
    {
        return new Point2D.Double(-vector.getY(), vector.getX());
    }


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


    public static Point2D findClosestPointOnEdges(Point2D point, ArrayList<Line2D> edges) {
        Point2D closestPoint = null;
        double minDistance = Double.MAX_VALUE;

        for (Line2D edge : edges) {
            Point2D closest = getClosestPointOnLine(edge, point);
            double distance = closest.distance(point);

            if (distance < minDistance) {
                minDistance = distance;
                closestPoint = closest;
            }
        }

        return closestPoint;
    }

    private static Point2D getClosestPointOnLine(Line2D line, Point2D point) {
        double x1 = line.getX1();
        double y1 = line.getY1();
        double x2 = line.getX2();
        double y2 = line.getY2();
        double px = point.getX();
        double py = point.getY();

        double dx = x2 - x1;
        double dy = y2 - y1;
        double t = ((px - x1) * dx + (py - y1) * dy) / (dx * dx + dy * dy);

        if (t < 0) {
            t = 0;
        } else if (t > 1) {
            t = 1;
        }

        double closestX = x1 + t * dx;
        double closestY = y1 + t * dy;

        return new Point2D.Double(closestX, closestY);
    }


    public static int findPanelEdgeIndex(ArrayList<Point2D> vertices, Point2D intersection){
        double minDistance = Double.MAX_VALUE;
        int index = -1;
        for (int i=0; i < vertices.size(); i++){
            Point2D temp = getClosestPointOnSegment(vertices.get(i),vertices.get((i+1)%vertices.size()),intersection);
            double distance = temp.distance(intersection);
            if (distance < minDistance) {
                minDistance = distance;
                index = i;
            }
        }
        return index;
    }


    public static HashMap<Integer, Point2D> closestPointOnEdges(Point2D point, ArrayList<Line2D> edges){
        double minDistance = Double.MAX_VALUE;
        Point2D closest = null;
        int edgeIndex = -1;
        for (int i=0; i<edges.size(); i++){
            Point2D temp = getClosestPointOnSegment(edges.get(i).getP1(), edges.get(i).getP2(), point);
            double distance = temp.distance(point);
//            System.out.println(distance);
            if (distance < minDistance) {
//                System.out.println("+++++========++++++++++");
                edgeIndex = i;
                minDistance = distance;
                closest = temp;
            }
        }

        HashMap<Integer, Point2D> result = new HashMap<>(1);
        result.put(edgeIndex, closest);
        return result;
    }


    public static Point2D getClosestPointOnSegment(Point2D head1, Point2D head2, Point2D point) {
        double u =((point.getX()-head1.getX())*(head2.getX()-head1.getX())+(point.getY()-head1.getY())*(head2.getY()-head1.getY()))/head2.distanceSq(head1);
        if (u > 1.0) return (Point2D) head2.clone();
        else if (u <= 0.0) return (Point2D) head1.clone();
        else return new Point2D.Double(head2.getX()* u + head1.getX() * (1.0 - u) + 0.5,head2.getY() * u + head1.getY() * (1.0 - u) + 0.5);
    }


    public static Point2D rotateVector(Point2D vector, double theta) {
        double x = vector.getX();
        double y = vector.getY();

        double cosTheta = Math.cos(theta);
        double sinTheta = Math.sin(theta);

        double rotatedX = x * cosTheta - y * sinTheta;
        double rotatedY = x * sinTheta + y * cosTheta;

        return new Point2D.Double(rotatedX, rotatedY);
    }


    // I think this one is the correct AABB check!
    public static boolean doAABBsIntersect(Rectangle2D rect1, Rectangle2D rect2) {
        // Check overlap in X-axis
        boolean overlapX = rect1.getX() < rect2.getX() + rect2.getWidth() && rect1.getX() + rect1.getWidth() > rect2.getX();

        // Check overlap in Y-axis
        boolean overlapY = rect1.getY() < rect2.getY() + rect2.getHeight() && rect1.getY() + rect1.getHeight() > rect2.getY();

        // Return true if there is overlap in X or Y dimensions
        return overlapX && overlapY;
    }

    public static boolean isPointInPolygon(Point2D point, Point2D[] polygon) {
        boolean result = false;
        int n = polygon.length;
        for (int i = 0, j = n - 1; i < n; j = i++) {
            if ((polygon[i].getY() > point.getY()) != (polygon[j].getY() > point.getY()) &&
                    (point.getX() < (polygon[j].getX() - polygon[i].getX()) * (point.getY() - polygon[i].getY()) / (polygon[j].getY() - polygon[i].getY()) + polygon[i].getX())) {
                result = !result;
            }
        }
        return result;
    }


    // created by ChatGPT:
    public static Point2D getIntersection(Line2D line1, Line2D line2) {
        double x1 = line1.getX1();
        double y1 = line1.getY1();
        double x2 = line1.getX2();
        double y2 = line1.getY2();

        double x3 = line2.getX1();
        double y3 = line2.getY1();
        double x4 = line2.getX2();
        double y4 = line2.getY2();

        double denominator = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
        if (denominator == 0) {
            return null; // Lines are parallel
        }

        double ua = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / denominator;
        double ub = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / denominator;

        if (ua >= 0 && ua <= 1 && ub >= 0 && ub <= 1) {
            double x = x1 + ua * (x2 - x1);
            double y = y1 + ua * (y2 - y1);
            return new Point2D.Double(x, y);
        }

        return null; // Lines do not intersect within the line segments
    }


    // following method is only usable for panels (AABBs)
    public static ArrayList<Point2D> findIntersections(FinalPanelModel p1, FinalPanelModel p2){
        ArrayList<Line2D> edges1 = p1.getUnTrimmedEdges();
        ArrayList<Line2D> edges2 = p2.getUnTrimmedEdges();

        ArrayList<Point2D> intersections = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Point2D intersection = getIntersection(edges1.get(i), edges2.get(j));
                if (intersection != null) intersections.add(intersection);
            }
        }
        if (intersections.size() != 2) return null;
        return intersections;
    }

    public static Line2D moveLine(Line2D line, Point2D movement){
        Point2D firstPoint = addVectors(line.getP1(), movement);
        Point2D secondPoint = addVectors(line.getP2(), movement);
        return new Line2D.Double(firstPoint, secondPoint);
    }

    public static Point2D adjustVectorMagnitude(Point2D vector, double magnitude){
        double currentMagnitude = Math.sqrt(vector.getX()*vector.getX() + vector.getY()*vector.getY());
        double xNormalized = vector.getX()/currentMagnitude;
        double yNormalized = vector.getY()/currentMagnitude;
        return new Point2D.Double(xNormalized*magnitude, yNormalized*magnitude);
    }

    public static MyPolygon rotateMyPolygon(MyPolygon polygon, double angle, Point2D center) {
        double[] xPoints = polygon.xpoints;
        double[] yPoints = polygon.ypoints;
        int nPoints = polygon.npoints;

        double radians = Math.toRadians(angle);
        double cosTheta = Math.cos(radians);
        double sinTheta = Math.sin(radians);

        double[] newXPoints = new double[nPoints];
        double[] newYPoints = new double[nPoints];

        for (int i = 0; i < nPoints; i++) {
            // Translate point to origin
            double x = xPoints[i] - center.getX();
            double y = yPoints[i] - center.getY();

            // Apply rotation
            double newX = x * cosTheta - y * sinTheta;
            double newY = x * sinTheta + y * cosTheta;

            // Translate point back
            newXPoints[i] = (newX + center.getX());
            newYPoints[i] = (newY + center.getY());
        }

        return new MyPolygon(newXPoints, newYPoints, nPoints);
    }


    public static ArrayList<Line2D> findEdges(MyPolygon myPolygon) {
        ArrayList<Line2D> edges = new ArrayList<>();
        int numVertices = myPolygon.npoints;

        for (int i = 0; i < numVertices; i++) {
            Point2D start = new Point2D.Double(myPolygon.xpoints[i], myPolygon.ypoints[i]);
            Point2D end = new Point2D.Double(myPolygon.xpoints[(i + 1) % numVertices], myPolygon.ypoints[(i + 1) % numVertices]);
            edges.add(new Line2D.Double(start, end));
        }

        return edges;
    }
}