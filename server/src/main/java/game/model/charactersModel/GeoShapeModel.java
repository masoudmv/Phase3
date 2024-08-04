package game.model.charactersModel;

import game.controller.Game;
import game.controller.UserInterfaceController;
import game.controller.Utils;
import game.model.FinalPanelModel;
import shared.Model.MyPolygon;
import game.model.entities.Entity;
import game.model.entities.Profile;
import game.model.movement.Direction;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import static game.controller.UserInterfaceController.eliminateGeoShapeView;

public abstract class GeoShapeModel extends Entity {

    protected String id;
    protected Point2D anchor;
    public MyPolygon myPolygon;
    protected Direction direction = new Direction(new Point2D.Double(0, 0));
    protected double radius;
    public FinalPanelModel localPanel;
    protected double angle;
    protected boolean isOnFall = false;
    protected boolean isHovering = false;
    public static List<GeoShapeModel> entities = new CopyOnWriteArrayList<>();

    public GeoShapeModel(Point2D anchor, BufferedImage image, MyPolygon myPolygon) {
        this.id = UUID.randomUUID().toString();
        this.anchor = new Point2D.Double(anchor.getX(), anchor.getY());
        this.myPolygon = myPolygon;
        radius = (double) image.getWidth() / 2;
        Point2D img = new Point2D.Double((double) -image.getWidth() / 2, (double) -image.getHeight() / 2);
        moveVertices(Utils.addVectors(anchor, img));
        entities.add(this);
//        UserInterfaceController.createGeoShapeView(id, image);
    }



    public GeoShapeModel(Point2D anchor, BufferedImage image, MyPolygon myPolygon, int zOrder) {
        this.id = UUID.randomUUID().toString();
        this.anchor = new Point2D.Double(anchor.getX(), anchor.getY());
        this.myPolygon = myPolygon;
        radius = (double) image.getWidth() / 2;
        Point2D img = new Point2D.Double((double) -image.getWidth() / 2, (double) -image.getHeight() / 2);
        moveVertices(Utils.addVectors(anchor, img));
        entities.add(this);
//        UserInterfaceController.createGeoShapeView(id, image, zOrder);
    }

    public GeoShapeModel(Point2D anchor, BufferedImage image, MyPolygon myPolygon, boolean necropick) {
        this.id = UUID.randomUUID().toString();
        this.anchor = new Point2D.Double(anchor.getX(), anchor.getY());
        this.myPolygon = myPolygon;
        radius = (double) image.getHeight() / 2;
        Point2D img = new Point2D.Double((double) -image.getWidth() / 2, (double) -image.getHeight() / 2);
        moveVertices(Utils.addVectors(anchor, img));
        entities.add(this);
    }

    public GeoShapeModel(BufferedImage image, MyPolygon myPolygon) {
        this.id = UUID.randomUUID().toString();
        this.myPolygon = myPolygon;
        entities.add(this);
//        UserInterfaceController.createGeoShapeView(id, image);
    }

    public GeoShapeModel(Point2D anchor, BufferedImage image) {
        setDummyPolygon();
        this.id = UUID.randomUUID().toString();
        this.anchor = new Point2D.Double(anchor.getX(), anchor.getY());
        radius = (double) image.getHeight() / 2;
        entities.add(this);
//        UserInterfaceController.createGeoShapeView(id, image);
    }

    public GeoShapeModel(Point2D anchor) {
        this.anchor = anchor;
        this.id = UUID.randomUUID().toString();
        entities.add(this);
//        UserInterfaceController.createGeoShapeView(id);
    }

    public GeoShapeModel() {
        this.id = UUID.randomUUID().toString();
        entities.add(this);
        setDummyPolygon();
    }

    private void setDummyPolygon() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        myPolygon = new MyPolygon(x, y, 3);
    }

    public String getId() {
        return id;
    }

    public Point2D getAnchor() {
        return anchor;
    }

    public double getAngle() {
        return angle;
    }

    public void setAnchor(Point2D anchor) {
        Point2D movement = Utils.relativeLocation(anchor, this.anchor);
        movePolygon(movement);
    }

    public abstract void setMyPolygon(MyPolygon myPolygon);

    public void moveVertices(Point2D movement) {
        double[] xpoints = new double[myPolygon.npoints];
        double[] ypoints = new double[myPolygon.npoints];
        for (int i = 0; i < myPolygon.npoints; i++) {
            xpoints[i] = this.myPolygon.xpoints[i] + movement.getX();
            ypoints[i] = this.myPolygon.ypoints[i] + movement.getY();
        }
        myPolygon = new MyPolygon(xpoints, ypoints, myPolygon.npoints);
    }

    public Point2D[] getVertices() {
        Point2D[] vertices = new Point2D[myPolygon.npoints];
        for (int i = 0; i < myPolygon.npoints; i++) {
            double x = myPolygon.xpoints[i];
            double y = myPolygon.ypoints[i];
            vertices[i] = new Point2D.Double(x, y);
        }
        return vertices;
    }

    public void movePolygon(Point2D movement) {
        anchor = Utils.addVectors(anchor, movement);
        moveVertices(movement);
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public synchronized void eliminate() {
        entities.remove(this);
//        eliminateGeoShapeView(id);

        eliminateGeoShapeView(id);

    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public ArrayList<Line2D> getEdges() {
        return Utils.findEdges(myPolygon);
    }

    protected boolean checkIntersectionExistence(){
        for (GeoShapeModel geoShapeModel : entities) {
            if (!geoShapeModel.isHovering && this.intersects(geoShapeModel) && !geoShapeModel.equals(this)) {
                return true; // Intersection found, next position is not valid
            }
        }
        return false; // No intersection found, next position is valid
    }


    public boolean isInside(Point2D[] polygon) {
        ArrayList<Point2D> bound = this.getBoundingPoints();
        boolean isInside = true;
        for (Point2D point : bound) {
            if (!Utils.isPointInPolygon(point, polygon)) {
                isInside = false;
                break;
            }
        }
        return isInside;
    }

    public void addHealth(int units) {
        this.health = Math.min(fullHealth, health + units);
    }

    public ArrayList<Point2D> getBoundingPoints() {
        ArrayList<Point2D> bound = new ArrayList<>();
        for (Integer i : myPolygon.getBoundingPointIndexes()) {
            bound.add(new Point2D.Double(myPolygon.xpoints[i], myPolygon.ypoints[i]));
        }
        return bound;
    }

    public void initiateFall() {
        isOnFall = true;
        this.direction = new Direction(new Point2D.Double(0, 1));
    }

    public void updateVelocityOnFall() {
        double magnitude = direction.getMagnitude();
        magnitude += 9.81 / 3;
        direction.setMagnitude(magnitude);
    }

    public boolean isCircular() {
        return false; // Default implementation; can be overridden by subclasses
    }

    // Method to check if two shapes intersect
    public boolean intersects(GeoShapeModel other) {
        if (this.isCircular() && other.isCircular()) {
            return circlesIntersect(this, other);
        } else if (this.isCircular()) {
            return circlePolygonIntersect(this, other);
        } else if (other.isCircular()) {
            return circlePolygonIntersect(other, this);
        } else {
            return polygonsIntersect(this.myPolygon, other.myPolygon);
        }
    }

    // Circle-Circle collision detection
    private boolean circlesIntersect(GeoShapeModel circle1, GeoShapeModel circle2) {
        double distance = circle1.anchor.distance(circle2.anchor);
        return distance < (circle1.radius + circle2.radius);
    }

    // Circle-Polygon collision detection
    private boolean circlePolygonIntersect(GeoShapeModel circle, GeoShapeModel polygon) {
        MyPolygon poly = polygon.myPolygon;
        Point2D center = circle.anchor;
        double radius = circle.radius;

        List<Point2D.Double> axes = new ArrayList<>();
        addPolygonAxes(poly, axes);

        // Add the closest edge normal to the circle's center
        Point2D.Double closestEdgeNormal = getClosestEdgeNormal(poly, center);
        axes.add(closestEdgeNormal);

        // Test all axes
        for (Point2D.Double axis : axes) {
            if (!circlePolygonProjectionsOverlap(circle, poly, axis)) {
                return false; // Found a separating axis, so shapes do not intersect
            }
        }
        return true; // No separating axis found, so shapes intersect
    }

    // Get the normal of the closest edge to the circle's center
    private Point2D.Double getClosestEdgeNormal(MyPolygon poly, Point2D center) {
        Point2D.Double closestEdgeNormal = null;
        double minDistance = Double.MAX_VALUE;

        for (int i = 0; i < poly.npoints; i++) {
            int nextIndex = (i + 1) % poly.npoints;
            Point2D.Double p1 = new Point2D.Double(poly.xpoints[i], poly.ypoints[i]);
            Point2D.Double p2 = new Point2D.Double(poly.xpoints[nextIndex], poly.ypoints[nextIndex]);
            Line2D.Double edge = new Line2D.Double(p1, p2);

            Point2D.Double edgeNormal = new Point2D.Double(-(p2.y - p1.y), p2.x - p1.x); // Perpendicular to the edge
            normalize(edgeNormal);

            double distance = edge.ptSegDist(center);
            if (distance < minDistance) {
                minDistance = distance;
                closestEdgeNormal = edgeNormal;
            }
        }

        return closestEdgeNormal;
    }

    private boolean circlePolygonProjectionsOverlap(GeoShapeModel circle, MyPolygon poly, Point2D.Double axis) {
        double[] polyProjection = projectPolygon(poly, axis);
        double[] circleProjection = projectCircle(circle, axis);

        // Check if projections overlap
        return !(circleProjection[1] < polyProjection[0] || polyProjection[1] < circleProjection[0]);
    }

    private double[] projectCircle(GeoShapeModel circle, Point2D.Double axis) {
        double centerProjection = circle.anchor.getX() * axis.x + circle.anchor.getY() * axis.y;
        double radiusProjection = circle.radius * Math.sqrt(axis.x * axis.x + axis.y * axis.y);
        return new double[]{centerProjection - radiusProjection, centerProjection + radiusProjection};
    }

    private boolean polygonsIntersect(MyPolygon poly1, MyPolygon poly2) {
        List<Point2D.Double> axes = new ArrayList<>();
        addPolygonAxes(poly1, axes);
        addPolygonAxes(poly2, axes);

        for (Point2D.Double axis : axes) {
            if (!projectionsOverlap(poly1, poly2, axis)) {
                return false;
            }
        }
        return true;
    }

    private void addPolygonAxes(MyPolygon poly, List<Point2D.Double> axes) {
        for (int i = 0; i < poly.npoints; i++) {
            int nextIndex = (i + 1) % poly.npoints;
            double dx = poly.xpoints[nextIndex] - poly.xpoints[i];
            double dy = poly.ypoints[nextIndex] - poly.ypoints[i];
            Point2D.Double axis = new Point2D.Double(-dy, dx); // Perpendicular axis
            normalize(axis);
            axes.add(axis);
        }
    }

    private void normalize(Point2D.Double vector) {
        double length = Math.sqrt(vector.x * vector.x + vector.y * vector.y);
        vector.x /= length;
        vector.y /= length;
    }

    private boolean projectionsOverlap(MyPolygon poly1, MyPolygon poly2, Point2D.Double axis) {
        double[] projection1 = projectPolygon(poly1, axis);
        double[] projection2 = projectPolygon(poly2, axis);

        return !(projection1[1] < projection2[0] || projection2[1] < projection1[0]);
    }

    private double[] projectPolygon(MyPolygon poly, Point2D.Double axis) {
        double min = Double.MAX_VALUE;
        double max = -Double.MAX_VALUE;

        for (int i = 0; i < poly.npoints; i++) {
            double projection = (poly.xpoints[i] * axis.x + poly.ypoints[i] * axis.y);
            if (projection < min) {
                min = projection;
            }
            if (projection > max) {
                max = projection;
            }
        }
        return new double[]{min, max};
    }

    public boolean dontUpdate(){
        double now = Game.ELAPSED_TIME;
        double slumberInitiation = Profile.getCurrent().slumberInitiationTime;
        return now - slumberInitiation < 10;
    }


    public void update(){



    }
}
