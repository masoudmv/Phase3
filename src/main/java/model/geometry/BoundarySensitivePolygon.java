package model.geometry;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static model.geometry.RayDetection.getVertexModel;

//import static geometry.RayDetection.getVertexModel;

public class BoundarySensitivePolygon extends Polygon {
    public static final int PRECISION = 3;

    ArrayList<Point> boundaryPoints = new ArrayList<>();
    BufferedImage image;
    Point pivot;
    Polygon superPolygon;

    public BoundarySensitivePolygon(BufferedImage image, Polygon superPolygon) {
        this.image = image;
        this.superPolygon = superPolygon;
    }

    public void updateBoundary(){
        setPivot();
        getVertexModel(image, PRECISION, pivot, this);
    }

    public void setPivot(){
        int xAvg = 0, yAvg = 0;
        for (int i = 0; i < npoints; i++) {
            xAvg += xpoints[i];
            yAvg += ypoints[i];
        }

        xAvg /= npoints;
        yAvg /= npoints;

        pivot = new Point(xAvg, yAvg);
    }
}
