package model.collision;

import java.awt.geom.Point2D;

public class Intersection {
    Point2D collision;
    int edge;
    Collidable polygon;

    public Intersection(Point2D collision, int edge, Collidable polygon) {
        this.collision = collision;
        this.edge = edge;
        this.polygon = polygon;
    }
}
