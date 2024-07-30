package view.charactersView;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import static controller.constants.Constants.BULLET_RADIUS;

public class BulletView extends GeoShapeView{
//    Point2D currentLocation=new Point2D.Double(0,0);
    public static CopyOnWriteArrayList<BulletView> bulletViews = new CopyOnWriteArrayList<>();

    public BulletView(String id) {
        super(id);
        bulletViews.add(this);
    }

//    public void setCurrentLocation(Point2D currentLocation) {
//        this.currentLocation = currentLocation;
//    }

//    public Point2D getCurrentLocation() {
//        return currentLocation;
//    }

    public String getId() {
        return id;
    }



    @Override
    public void eliminate(){
        super.eliminate();
    }

    @Override
    public void draw(Graphics g, String panelID) {
        g.setColor(Color.white);

//        if (locations.get(panelID) == null) return;
        if (locations.get(panelID) == null) return;

        int x = (int) (locations.get(panelID).getX() - BULLET_RADIUS);
        int y = (int) (locations.get(panelID).getY() - BULLET_RADIUS);


        g.fillOval(x, y, (int) (2 * BULLET_RADIUS), (int) (2 * BULLET_RADIUS));
    }
}
