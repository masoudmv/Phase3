package model.collision;

import java.awt.geom.Point2D;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Segment {

       Point2D head1;
       Point2D head2;
       Collidable collidable;

    public Segment(Point2D head1, Point2D head2) {
        this.head1 = head1;
        this.head2 = head2;
    }

    static boolean closeToZero(double v) {
        // Check if double is close to zero, considering precision issues.
        return Math.abs(v) <= 0.0000000000001;
    }


    public static Point2D findMidpoint(Segment segment){
        Point2D head1 = segment.head1;
        Point2D head2 = segment.head2;
        return new Point2D.Double((head1.getX()+head2.getX())/2, (head1.getY()+head2.getY())/2);
    }

    public static Point2D findIntersection(Segment segment1, Segment segment2){

        double x1 = segment1.head1.getX();
        double y1 = segment1.head1.getY();

        double x2 = segment1.head2.getX();
        double y2 = segment1.head2.getY();

        double x3 = segment2.head1.getX();
        double y3 = segment2.head1.getY();

        double x4 = segment2.head2.getX();
        double y4 = segment2.head2.getY();


        if (x1 == x2 && x3 == x4) {
            // Both segments are vertical
            if (x1 != x3) return null;
            if (min(y1,y2) < min(y3,y4)) {
                if (max(y1,y2) >= min(y3,y4)) return new Point2D.Double(x1, (max(y1,y2)+min(y3,y4))/2);
                else return null;
            } else {
                if (max(y3,y4) >= min(y1,y2)) return new Point2D.Double(x1, (max(y3,y4)+min(y1,y2))/2);
            }
        }
        if (x1 == x2) {
            // Only segment1 is vertical. Does segment2 cross it? y = a*x + b
            double slopeSegment2 = (y4-y3)/(x4-x3);
            double interceptSegment2 = y3 - slopeSegment2 * x3;
            double y = slopeSegment2 * x1 + interceptSegment2;
            if (y >= min(y1,y2) && y <= max(y1,y2) && x1 >= min(x3,x4) && x1 <= max(x3,x4)) return new Point2D.Double(x1, y);
            else return null;
        }
        if (x3 == x4) {
            // Only segment2 is vertical. Does segment1 cross it? y = a*x + b
            double slopeSegment1 = (y2-y1)/(x2-x1);
            double interceptSegment1 = y1 - slopeSegment1 * x1;
            double y = slopeSegment1 * x3 + interceptSegment1;
            if (y >= min(y3,y4) && y <= max(y3,y4) && x3 >= min(x1,x2) && x3 <= max(x1,x2)) return new Point2D.Double(x3, y);
        }

        double slopeSegment1 = (y2-y1)/(x2-x1);
        double interceptSegment1 = y1 - slopeSegment1 * x1;
        double slopeSegment2 = (y4-y3)/(x4-x3);
        double interceptSegment2 = y3 - slopeSegment2 * x3;
        if (closeToZero(slopeSegment1 - slopeSegment2)) {
            // Parallel lines
            return null;
            // TODO find intersection points.
//            if (!closeToZero(interceptSegment1 - interceptSegment2)) return null;
//            else {
//
//            }
        }
        // Nonparallel nonVertical lines intersect at x. Is x part of both segments?
        double x = -(interceptSegment1-interceptSegment2)/(slopeSegment1-slopeSegment2);
        double y = slopeSegment1 * x + interceptSegment1;
        if (x >= min(x1,x2) && x <= max(x1,x2) && x >= min(x3,x4) && x <= max(x3,x4)) return new Point2D.Double(x, y);
        else return null;


    }
}
