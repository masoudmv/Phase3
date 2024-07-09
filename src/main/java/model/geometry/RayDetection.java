package model.geometry;

import com.sun.source.tree.IfTree;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.ArrayList;

public class RayDetection {

    public static ArrayList<Point> getVertexModel(BufferedImage image, int axes, Point pivot){
        Polygon bound = new Polygon();
        bound.addPoint(1, 1);
        bound.addPoint(image.getWidth()-1, 1);
        bound.addPoint(image.getWidth()-1, image.getHeight()-1);
        bound.addPoint(image.getHeight()-1, 1);
        return getVertexModel(image, axes, pivot, bound);
    }


    public static ArrayList<Point> getVertexModel(BufferedImage image, int axes, Point pivot, Polygon bound){
        ArrayList<Double> angles = new ArrayList<>();
        double div = 180F/axes;
        double currentAngle = 0;

        for (int i = 0; i < axes; i++) {
            angles.add(currentAngle);
            currentAngle += div;
        }

        ArrayList<Point> points = new ArrayList<>();
        for (double angle : angles){
            Point[] temp = getRayHeads(image, angle, pivot);
            if (temp[0] != null) points.add(temp[0]);
            if (temp[1] != null) points.add(temp[1]);

        }

        double divHorizontal = (double) image.getWidth() / axes;
        double xOffset = 0;
        for (int i = 0; i < axes + 1; i++) {
            if (image.getWidth() - xOffset <= 0.5) xOffset--;
            Point newPivot = new Point((int) Math.round(xOffset), 0);
            Point[] temp = getRayHeads(image, 90, newPivot);
            if (temp[0] != null) points.add(temp[0]);
            if (temp[1] != null) points.add(temp[1]);

            xOffset += divHorizontal;
        }

        double divVertical = (double) image.getHeight() / axes;
        double yOffset = 0;
        for (int i = 0; i < axes + 1; i++) {
            if (image.getHeight() - yOffset <= 0.5) yOffset--;
            Point newPivot = new Point(0, (int) Math.round(yOffset));
            Point[] temp = getRayHeads(image, 0, newPivot);
            if (temp[0] != null) points.add(temp[0]);
            if (temp[1] != null) points.add(temp[1]);

            yOffset += divVertical;
        }

        return points;

    }



    public static Point[] getRayHeads(BufferedImage image, double angle, Point pivot){
        Point minPoint = null;
        Point maxPoint = null;

        if(angle!=90){
            double slope = Math.tan(Math.toRadians(angle));
            for (int x = 0; x < image.getWidth(); x++) {
                int modifiedX = x - pivot.x;
                int modifiedY = (int) Math.floor(modifiedX * slope);
                Point target = new Point(x, pivot.y - modifiedY);

                if (isValid(image, target) && !isTransparent(image, target) && !isSurrounded(image, target)){
                    maxPoint = target;
                    if (minPoint == null) minPoint = target;
                }

            }
        }

        else {
            for (int y = 0; y < image.getHeight(); y++) {
                Point target = new Point(pivot.x, y);
                if (isValid(image, target) && !isTransparent(image, target) && !isSurrounded(image, target)){
                    maxPoint = target;
                    if (minPoint == null) minPoint = target;
                }

            }
        }

        Point[] out = new Point[2];
        if (maxPoint != null) out[0] = maxPoint;
        if (minPoint != null) out[1] = minPoint;
        return out;
    }
    public static boolean isTransparent(BufferedImage bImage, Point point){
        return (bImage.getRGB(point.x, point.y) >> 24) == 0x00;
    }

    public static boolean isSurrounded(BufferedImage image, Point point){
        Point L = new Point(point.x-1, point.y);
        Point R = new Point(point.x+1, point.y);
        Point U = new Point(point.x, point.y-1);
        Point D = new Point(point.x, point.y+1);

        boolean valid = isValid(image, L) && isValid(image, R) && isValid(image, U) && isValid(image, D);
        if (!valid) return false;
        return !isTransparent(image, L) &&  !isTransparent(image, R) && !isTransparent(image, U) && !isTransparent(image, D);

    }

    public static boolean isInBound(Point point, Polygon bound){
        int count = 0;
        for (int i = 0; i < bound.npoints; i++) {
            int index1 = i % bound.npoints;
            int index2 = (i+1) % bound.npoints;

            boolean intersection = Line2D.linesIntersect(bound.xpoints[index1], bound.ypoints[index1],
                    bound.xpoints[index2], bound.ypoints[index2], point.x, point.y, 0, 0);

            if (intersection) count++;
        }
        return count % 2 == 1;
    }


    public static boolean isValid(BufferedImage image, Point point){
        return point.x >= 0 && point.x < image.getWidth() && point.y >= 0 && point.y < image.getHeight();
    }


}








//package geometry;
//
//import com.sun.source.tree.IfTree;
//
//import java.awt.*;
//import java.awt.geom.Line2D;
//import java.awt.image.BufferedImage;
//import java.nio.Buffer;
//import java.util.ArrayList;
//
//public class RayDetection {
//
//    public static ArrayList<Point> getVertexModel(BufferedImage image, int axes, Point pivot){
//        Polygon bound = new Polygon();
//        bound.addPoint(1, 1);
//        bound.addPoint(image.getWidth()-1, 1);
//        bound.addPoint(image.getWidth()-1, image.getHeight()-1);
//        bound.addPoint(image.getHeight()-1, 1);
//        return getVertexModel(image, axes, pivot, bound);
//    }
//
//
//    public static ArrayList<Point> getVertexModel(BufferedImage image, int axes, Point pivot, Polygon bound){
//        ArrayList<Double> angles = new ArrayList<>();
//        double div = 180F/axes;
//        double currentAngle = 0;
//
//        for (int i = 0; i < axes; i++) {
//            angles.add(currentAngle);
//            currentAngle += div;
//        }
//
//        ArrayList<Point> points = new ArrayList<>();
//        for (double angle : angles){
//            Point[] temp = getRayHeads(image, angle, pivot, bound);
//            if (temp[0] != null) points.add(temp[0]);
//            if (temp[1] != null) points.add(temp[1]);
//
//        }
//
//        double divHorizontal = (double) image.getWidth() / axes;
//        double xOffset = 0;
//        for (int i = 0; i < axes + 1; i++) {
//            if (image.getWidth() - xOffset <= 0.5) xOffset--;
//            Point newPivot = new Point((int) Math.round(xOffset), 0);
//            Point[] temp = getRayHeads(image, 90, newPivot, bound);
//            if (temp[0] != null) points.add(temp[0]);
//            if (temp[1] != null) points.add(temp[1]);
//
//            xOffset += divHorizontal;
//        }
//
//        double divVertical = (double) image.getHeight() / axes;
//        double yOffset = 0;
//        for (int i = 0; i < axes + 1; i++) {
//            if (image.getHeight() - yOffset <= 0.5) yOffset--;
//            Point newPivot = new Point(0, (int) Math.round(yOffset));
//            Point[] temp = getRayHeads(image, 0, newPivot, bound);
//            if (temp[0] != null) points.add(temp[0]);
//            if (temp[1] != null) points.add(temp[1]);
//
//            yOffset += divVertical;
//        }
//
//        return points;
//
//    }
//
//    public static boolean isInBound(Point point, Polygon bound){
//        int count = 0;
//        for (int i = 0; i < bound.npoints; i++) {
//            int index1 = i % bound.npoints;
//            int index2 = (i+1) % bound.npoints;
//
//            boolean intersection = Line2D.linesIntersect(bound.xpoints[index1], bound.ypoints[index1],
//                    bound.xpoints[index2], bound.ypoints[index2], point.x, point.y, 0, 0);
//
//            if (intersection) count++;
//        }
//        return count % 2 == 1;
//    }
//
//    public static Point[] getRayHeads(BufferedImage image, double angle, Point pivot, Polygon bound){
//        Point minPoint = null;
//        Point maxPoint = null;
//
//        if(angle!=90){
//            double slope = Math.tan(Math.toRadians(angle));
//            int xUpperBound = (int) Math.min(bound.getBounds().getMaxX(), image.getWidth()-1);
//            for (int x = (int) bound.getBounds().getMinX(); x < xUpperBound; x++) {
//                int modifiedX = x - pivot.x;
//                int modifiedY = (int) Math.floor(modifiedX * slope);
//                Point target = new Point(x, pivot.y - modifiedY);
//
//                if (isInBound(target, bound) && !isTransparent(image, target) && !isSurrounded(image, target, bound)){
//                    maxPoint = target;
//                    if (minPoint == null) minPoint = target;
//                }
//
//            }
//        }
//
//        else {
//            int yUpperBound = (int) Math.min(bound.getBounds().getMaxY(), image.getHeight()-1);
//            for (int y = (int) bound.getBounds().getMinY(); y < yUpperBound; y++) {
//                Point target = new Point(pivot.x, y);
//                if (isInBound(target, bound) && !isTransparent(image, target) && !isSurrounded(image, target, bound)){
//                    maxPoint = target;
//                    if (minPoint == null) minPoint = target;
//                }
//
//            }
//        }
//
//        Point[] out = new Point[2];
//        if (maxPoint != null) out[0] = maxPoint;
//        if (minPoint != null) out[1] = minPoint;
//        return out;
//    }
//    public static boolean isTransparent(BufferedImage bImage, Point point){
//        return (bImage.getRGB(point.x, point.y) >> 24) == 0x00;
//    }
//
//    public static boolean isSurrounded(BufferedImage image, Point point, Polygon bound){
//        Point L = new Point(point.x-1, point.y);
//        Point R = new Point(point.x+1, point.y);
//        Point U = new Point(point.x, point.y-1);
//        Point D = new Point(point.x, point.y+1);
//
//        boolean valid = isInBound(L, bound) && isInBound(R, bound) && isInBound(U, bound) && isInBound(D, bound);
//        if (!valid) return false;
//        return !isTransparent(image, L) &&  !isTransparent(image, R) && !isTransparent(image, D) && !isTransparent(image, U);
//
//    }
//
//
//    public static boolean isValid(BufferedImage image, Point point){
//        return point.x >= 0 && point.x <image.getWidth() && point.y >=0 && point.y< image.getHeight();
//    }
//
//
//}

