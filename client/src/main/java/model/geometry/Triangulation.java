package model.geometry;

import earcut4j.Earcut;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.security.cert.PolicyNode;
import java.util.ArrayList;

public class Triangulation {
    BufferedImage image;
    Polygon polygon;
    public ArrayList<BoundarySensitivePolygon> triangles = new ArrayList<>();

    public Triangulation(BufferedImage image, Polygon polygon) {
        this.image = image;
        this.polygon = polygon;
    }

    public void triangulate(){
        double[] coordinates = new double[2 * polygon.npoints];
        for (int i = 0; i < polygon.npoints; i++) {
            coordinates[2*i] = polygon.xpoints[i];
            coordinates[2*i+1] = polygon.ypoints[i];
        }

        ArrayList<Integer> triangleIndices = (ArrayList<Integer>) Earcut.earcut(coordinates);
        for (int i = 0; i < triangleIndices.size() / 3; i++) {
            BoundarySensitivePolygon triangle = new BoundarySensitivePolygon(image, polygon);
            triangle.addPoint(polygon.xpoints[triangleIndices.get(3*i)], polygon.ypoints[triangleIndices.get(3*i)]);
            triangle.addPoint(polygon.xpoints[triangleIndices.get(3*i+1)], polygon.ypoints[triangleIndices.get(3*i+1)]);
            triangle.addPoint(polygon.xpoints[triangleIndices.get(3*i+2)], polygon.ypoints[triangleIndices.get(3*i+2)]);

            triangles.add(triangle);
        }
    }
}
