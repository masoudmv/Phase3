package shared.Model;

import java.awt.*;

public class TimedLocation {
    private MyPolygon myPolygon;
    private Polygon polygon;
    private double timestamp;

    public TimedLocation(MyPolygon myPolygon, double timestamp) {
        this.myPolygon = myPolygon;
        this.timestamp = timestamp;
    }

    public TimedLocation(Polygon polygon, double timestamp) {
        this.polygon = polygon;
        this.timestamp = timestamp;
    }

    public MyPolygon getMyPolygon() {
        return myPolygon;
    }

    public Polygon getPolygon() {
        return polygon;
    }


    //todo move to utils ...
    public static Polygon myPolToPolygon(MyPolygon myPolygon) {
        int nPoints = myPolygon.npoints;
        int[] xPoints = new int[nPoints];
        int[] yPoints = new int[nPoints];
        for (int i = 0; i < nPoints; i++) {
            xPoints[i] = (int) myPolygon.xpoints[i];
            yPoints[i] = (int) myPolygon.ypoints[i];
        }
        return new Polygon(xPoints, yPoints, nPoints);
    }

    public double getTimestamp() {
        return timestamp;
    }
}
