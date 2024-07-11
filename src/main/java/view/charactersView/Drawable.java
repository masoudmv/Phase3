package view.charactersView;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public interface Drawable {
    ArrayList<Drawable> drawables = new ArrayList<>();
    void draw(Graphics g);
    public String getId();
    void setCurrentLocation(Point2D currentLocation);

}
