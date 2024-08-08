package shared.model.dummies;

import java.awt.*;


public class DummyPanel {
    private String id;
    private Point location;
    private Dimension dimension;

    public DummyPanel(String id, Point location, Dimension dimension) {
        this.id = id;
        this.location = location;
        this.dimension = dimension;
    }

    public DummyPanel() {
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
