package view.charactersView;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public interface Drawable {
    CopyOnWriteArrayList<Drawable> drawables = new CopyOnWriteArrayList<>();
    void draw(Graphics g);
    public String getId();
    void setCurrentLocation(Point2D currentLocation);

}
