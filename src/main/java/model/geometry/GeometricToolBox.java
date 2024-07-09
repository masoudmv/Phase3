package model.geometry;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class GeometricToolBox {

    public static ArrayList<Point> clockwiseSort(ArrayList<Point> points, Point pivot){
        ArrayList<Point> out = new ArrayList<>(points);
        out.sort(clockwiseComparator(pivot));
        return out;
    }

    public static Comparator<Point> clockwiseComparator(Point pivot){
        return (o1, o2) -> {
            if (o1.x - pivot.x >= 0 && o2.x - pivot.x < 0) return 1;
            if (o1.x - pivot.x < 0  && o2.x - pivot.x >= 0) return -1;
            if (o1.x - pivot.x == 0 && o2.x - pivot.x == 0) Integer.compare(o1.y, o2.y);
            return crossProduct(pivot, o1, o2);
        };
    }

    public static int crossProduct(Point O, Point A, Point B){
        return (A.x - O.x) * (B.y - O.y) -  (A.y - O.y) * (B.x - O.x);
    }

    public static Polygon pointsToPolygon(ArrayList<Point> points){
        Polygon polygon = new Polygon();
        for (Point point : points) polygon.addPoint(point.x, point.y);
        return polygon;
    }


}
