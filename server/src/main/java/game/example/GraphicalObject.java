package game.example;

import game.model.geometry.Triangulation;
import shared.model.MyPolygon;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

import static game.model.geometry.GeometricToolBox.clockwiseSort;
import static game.model.geometry.GeometricToolBox.pointsToPolygon;
import static game.model.geometry.RayDetection.getVertexModel;


public class GraphicalObject {
    public static final int PRECISION = 5;
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


        for (int i = 0; i < boundingPolygon.npoints; i++) {
            myBoundingPolygon = new MyPolygon();
            myBoundingPolygon.xpoints = Arrays.stream(boundingPolygon.xpoints).asDoubleStream().toArray();
            myBoundingPolygon.ypoints = Arrays.stream(boundingPolygon.ypoints).asDoubleStream().toArray();
            myBoundingPolygon.npoints = boundingPolygon.npoints;

        }
    }
}
