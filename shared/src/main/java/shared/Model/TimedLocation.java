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

    public double getTimestamp() {
        return timestamp;
    }
}
