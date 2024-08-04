package shared.Model.dummies;

import shared.Model.MyPolygon;
import java.awt.*;


public class DummyModel {
    private String id;
    private Point anchor;
    private double angle;
    private MyPolygon myPolygon;

    public DummyModel(String id, Point anchor, double angle, MyPolygon myPolygon) {
        this.id = id;
        this.anchor = anchor;
        this.angle = angle;
        this.myPolygon = myPolygon;
    }

    public DummyModel() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Point getAnchor() {
        return anchor;
    }

    public void setAnchor(Point anchor) {
        this.anchor = anchor;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public MyPolygon getMyPolygon() {
        return myPolygon;
    }

    public void setMyPolygon(MyPolygon myPolygon) {
        this.myPolygon = myPolygon;
    }
}
