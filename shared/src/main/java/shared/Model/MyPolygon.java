package shared.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;


@JsonIgnoreProperties(ignoreUnknown = true)
public class MyPolygon {
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
        if (npoints > xpoints.length || npoints > ypoints.length) {
            throw new IndexOutOfBoundsException("npoints > xpoints.length || "+
                    "npoints > ypoints.length");
        }
        if (npoints < 0) {
            throw new NegativeArraySizeException("npoints < 0");
        }
        this.npoints = npoints;
        this.xpoints = Arrays.copyOf(xpoints, npoints);
        this.ypoints = Arrays.copyOf(ypoints, npoints);
        setBoundingPointIndexes();
    }

    private void setBoundingPointIndexes(){
        double xMin = java.lang.Double.MAX_VALUE;
        double xMax = java.lang.Double.MAX_VALUE;
        double yMin = java.lang.Double.MAX_VALUE;
        double yMax = java.lang.Double.MAX_VALUE;

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

        boundingPointIndexes.add(xMinIndex);
        boundingPointIndexes.add(xMaxIndex);
        boundingPointIndexes.add(yMinIndex);
        boundingPointIndexes.add(yMaxIndex);
    }

    public CopyOnWriteArrayList<Integer> getBoundingPointIndexes() {
        return boundingPointIndexes;
    }

    public Point2D.Double[] getVertices(){
        Point2D.Double[] point2DS = new Point2D.Double[npoints];
        for (int i = 0; i < npoints; i++) {
            point2DS[i] = new Point2D.Double(xpoints[i], ypoints[i]);
        }
        return point2DS;
    }

    public void setVertices(Point2D.Double[] points) {
        npoints = points.length;
        for (int i = 0; i < points.length; i++) {
            xpoints[i] = points[i].getX();
            ypoints[i] = points[i].getY();
        }
    }

    public Point2D.Double getCenter(){
        double x = 0;
        double y = 0;
        for (int i = 0; i < npoints; i++) {
            x += xpoints[i];
            y += ypoints[i];
        }
        return new Point2D.Double(x/npoints, y/npoints);
    }
}
