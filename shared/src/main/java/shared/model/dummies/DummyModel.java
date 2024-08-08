package shared.model.dummies;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DummyModel implements Serializable {
    private static final long serialVersionUID = 1L; // Add a unique identifier for serialization
    private String id;
    private Point anchor;
    private double angle;
    private int[] xPoints;
    private int[] yPoints;
    private int nPoints;


    private List<Polygon> polygons = new ArrayList<>();
    private boolean showNextLoc = false;


    public DummyModel(String id, Point anchor, double angle, int[] xPoints, int[] yPoints, int nPoints) {
        this.id = id;
        this.anchor = anchor;
        this.angle = angle;
        this.xPoints = xPoints;
        this.yPoints = yPoints;
        this.nPoints = nPoints;
    }

    public DummyModel(String id, Point anchor, double angle) {
        this.id = id;
        this.anchor = anchor;
        this.angle = angle;
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

    public int[] getxPoints() {
        return xPoints;
    }

    public int[] getyPoints() {
        return yPoints;
    }

    public int getnPoints() {
        return nPoints;
    }

    public List<Polygon> getPolygons() {
        return polygons;
    }

    public void setPolygons(List<Polygon> polygons) {
        this.polygons = polygons;
    }

    public boolean isShowNextLoc() {
        return showNextLoc;
    }

    public void setShowNextLoc(boolean showNextLoc) {
        this.showNextLoc = showNextLoc;
    }


}
