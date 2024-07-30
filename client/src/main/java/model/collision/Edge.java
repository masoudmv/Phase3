package model.collision;

import java.awt.geom.Point2D;

public class Edge {

    public Point2D head1;
    public Point2D head2;


    public Edge(Point2D head1, Point2D head2) {
        this.head1 = head1;
        this.head2 = head2;
    }
}
