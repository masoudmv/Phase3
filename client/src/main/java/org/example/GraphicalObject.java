package org.example;

import model.MyPolygon;
import model.geometry.BoundarySensitivePolygon;
import model.geometry.Triangulation;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static model.geometry.GeometricToolBox.clockwiseSort;
import static model.geometry.GeometricToolBox.pointsToPolygon;
import static model.geometry.RayDetection.getVertexModel;

public class GraphicalObject {
    public static final int PRECISION = 10;
    BufferedImage image;
    Polygon boundingPolygon;
    MyPolygon myBoundingPolygon;
    Triangulation triangulation;
    Point center;

    public MyPolygon getMyBoundingPolygon() {
        return myBoundingPolygon;
    }

    public GraphicalObject(BufferedImage image) {
        this.image = image;
        center = new Point(image.getWidth() / 2, image.getHeight() / 2);
        ArrayList<Point> points = clockwiseSort(getVertexModel(image, PRECISION, center), center);
        boundingPolygon = pointsToPolygon(points);
        triangulation = new Triangulation(image, boundingPolygon);
        triangulation.triangulate();


//        for (int i = 0; i < boundingPolygon.npoints; i++) {
        myBoundingPolygon = new MyPolygon();
        myBoundingPolygon.xpoints = Arrays.stream(boundingPolygon.xpoints).asDoubleStream().toArray();
        myBoundingPolygon.ypoints = Arrays.stream(boundingPolygon.ypoints).asDoubleStream().toArray();
        myBoundingPolygon.npoints = boundingPolygon.npoints;

//        }
    }

    public void refine() {
        for (BoundarySensitivePolygon triangle : triangulation.triangles) triangle.updateBoundary();
        ArrayList<Point> pivots = new ArrayList<>();
        HashMap<Point, BoundarySensitivePolygon> pivotSave = new HashMap<>();
        for (BoundarySensitivePolygon triangle : triangulation.triangles) {
            if (!triangle.boundaryPoints.isEmpty()) {
                pivots.add(triangle.pivot);
                pivotSave.put(triangle.pivot, triangle);

            }
        }
        ArrayList<Point> sortedPivots = clockwiseSort(pivots, center);
        ArrayList<Point> refinedBoundary = new ArrayList<>();
        for (Point pivot : sortedPivots) {
            refinedBoundary.addAll(pivotSave.get(pivot).boundaryPoints);
        }
        boundingPolygon = pointsToPolygon(refinedBoundary);
        triangulation = new Triangulation(image, boundingPolygon);
        triangulation.triangulate();
    }
}
