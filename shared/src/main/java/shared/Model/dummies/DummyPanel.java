package shared.Model.dummies;

import java.awt.*;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

public class DummyPanel {
    private String id;
    private Point2D location;
    private Dimension2D dimension;

    public DummyPanel(String id, Point2D location, Dimension2D dimension) {
        this.id = id;
        this.location = location;
        this.dimension = dimension;
    }

    public DummyPanel() {
    }

    public Point2D getLocation() {
        return location;
    }

    public void setLocation(Point2D location) {
        this.location = location;
    }

    public Dimension2D getDimension() {
        return dimension;
    }

    public void setDimension(Dimension2D dimension) {
        this.dimension = dimension;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
