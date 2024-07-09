package model.charactersModel;

import java.awt.*;
import java.util.Arrays;

public class MyPolygon extends Polygon {
    public int npoints;
    public double[] xpoints;
    public double[] ypoints;
    private static final int MIN_LENGTH = 4;

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
    }

}
