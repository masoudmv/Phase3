package shared.Model.dummies;

import shared.Model.MyPolygon;

import java.awt.geom.Point2D;

public class DummyModel {
    private String id;
    private Point2D anchor;
    private double angle;
    private MyPolygon myPolygon;

    public DummyModel(String id, Point2D anchor, double angle, MyPolygon myPolygon) {
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

    public Point2D getAnchor() {
        return anchor;
    }

    public void setAnchor(Point2D anchor) {
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
