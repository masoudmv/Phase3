package model;

import java.awt.*;
import java.awt.geom.Point2D;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

public class MyPolygon extends Polygon { //todo remove inheritance
    public int npoints;
    public double[] xpoints;
    public double[] ypoints;
    private static final int MIN_LENGTH = 3;
    private CopyOnWriteArrayList<Integer> boundingPointIndexes = new CopyOnWriteArrayList<>();

    public MyPolygon() {
        xpoints = new double[MIN_LENGTH];
        ypoints = new double[MIN_LENGTH];
    }

    public MyPolygon(double[] xpoints, double[] ypoints, int npoints) {
        // Fix 4489009: should throw IndexOutOfBoundsException instead
        // of OutOfMemoryError if npoints is huge and > {x,y}points.length
        if (npoints > xpoints.length || npoints > ypoints.length) {
            throw new IndexOutOfBoundsException("npoints > xpoints.length || "+
                    "npoints > ypoints.length");
        }
        // Fix 6191114: should throw NegativeArraySizeException with
        // negative npoints
        if (npoints < 0) {
            throw new NegativeArraySizeException("npoints < 0");
        }
        // Fix 6343431: Applet compatibility problems if arrays are not
        // exactly npoints in length
        this.npoints = npoints;
        this.xpoints = Arrays.copyOf(xpoints, npoints);
        this.ypoints = Arrays.copyOf(ypoints, npoints);
        setBoundingPointIndexes();
    }

    private void setBoundingPointIndexes(){
        double xMin = Double.MAX_VALUE;
        double xMax = Double.MIN_VALUE;
        double yMin = Double.MAX_VALUE;
        double yMax = Double.MIN_VALUE;

        int xMinIndex = -1;
        int xMaxIndex = -1;
        int yMinIndex = -1;
        int yMaxIndex = -1;

        for (int i = 0; i < npoints; i++) {
            if (xpoints[i] < xMin) {
                xMin = xpoints[i];
                xMinIndex = i;
            }
            if (xpoints[i] > xMax) {
                xMax = xpoints[i];
                xMaxIndex = i;
            }

            if (ypoints[i] < yMin) {
                yMin = ypoints[i];
                yMinIndex = i;
            }
            if (ypoints[i] > yMax) {
                yMax = ypoints[i];
                yMaxIndex = i;
            }
        }

        // TODO remove redundant indexes!
        boundingPointIndexes.add(xMinIndex);
        boundingPointIndexes.add(xMaxIndex);
        boundingPointIndexes.add(yMinIndex);
        boundingPointIndexes.add(yMaxIndex);
    }

    public CopyOnWriteArrayList<Integer> getBoundingPointIndexes() {
        return boundingPointIndexes;
    }

    public Point2D[] getVertices(){
        Point2D[] point2DS = new Point2D[npoints];
        for (int i = 0; i < npoints; i++) {
            point2DS[i] = new Point2D.Double(xpoints[i], ypoints[i]);
        } return point2DS;
    }

    public void setVertices(Point2D[] points) {
        npoints = points.length;
        for (int i = 0; i < points.length; i++) {
            xpoints[i] = points[i].getX();
            ypoints[i] = points[i].getY();
        }
    }

}
