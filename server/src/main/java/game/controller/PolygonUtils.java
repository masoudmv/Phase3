package game.controller;

import shared.model.MyPolygon;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class PolygonUtils {

    public static class Result {
        public final Point2D.Double direction;
        public final double distance;

        public Result(Point2D.Double direction, double distance) {
            this.direction = direction;
            this.distance = distance;
        }
    }

    public static Result findSeparationVector(MyPolygon poly1, MyPolygon poly2) {
        List<Point2D.Double> axes = getAxes(poly1);
        axes.addAll(getAxes(poly2));

        Point2D.Double smallestAxis = null;
        double smallestOverlap = Double.MAX_VALUE;

        for (Point2D.Double axis : axes) {
            double[] projection1 = projectPolygon(axis, poly1);
            double[] projection2 = projectPolygon(axis, poly2);

            double overlap = getOverlap(projection1, projection2);

            if (overlap == 0) {
                return new Result(new Point2D.Double(0, 0), 0); // No overlap, polygons are already separated
            } else if (overlap < smallestOverlap) {
                smallestOverlap = overlap;
                smallestAxis = axis;
            }
        }

        Point2D.Double mtv = new Point2D.Double(smallestAxis.x * smallestOverlap, smallestAxis.y * smallestOverlap);
        return new Result(mtv, smallestOverlap);
    }

    private static List<Point2D.Double> getAxes(MyPolygon poly) {
        List<Point2D.Double> axes = new ArrayList<>();
        for (int i = 0; i < poly.npoints; i++) {
            int nextIndex = (i + 1) % poly.npoints;
            double dx = poly.xpoints[nextIndex] - poly.xpoints[i];
            double dy = poly.ypoints[nextIndex] - poly.ypoints[i];
            Point2D.Double axis = new Point2D.Double(-dy, dx); // Perpendicular axis
            normalize(axis);
            axes.add(axis);
        }
        return axes;
    }

    private static void normalize(Point2D.Double vector) {
        double length = Math.sqrt(vector.x * vector.x + vector.y * vector.y);
        vector.x /= length;
        vector.y /= length;
    }

    private static double[] projectPolygon(Point2D.Double axis, MyPolygon poly) {
        double min = Double.MAX_VALUE;
        double max = -Double.MAX_VALUE;

        for (int i = 0; i < poly.npoints; i++) {
            double projection = poly.xpoints[i] * axis.x + poly.ypoints[i] * axis.y;
            if (projection < min) {
                min = projection;
            }
            if (projection > max) {
                max = projection;
            }
        }
        return new double[]{min, max};
    }

    private static double getOverlap(double[] projection1, double[] projection2) {
        double start = Math.max(projection1[0], projection2[0]);
        double end = Math.min(projection1[1], projection2[1]);
        return end - start;
    }
}
