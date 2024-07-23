package view.charactersView;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import static controller.constants.Constants.BULLET_RADIUS;

public class BulletView implements Drawable {
    String id;
    Point2D currentLocation=new Point2D.Double(0,0);
    public static CopyOnWriteArrayList<BulletView> bulletViews = new CopyOnWriteArrayList<>();

    public BulletView(String id) {


        this.id = id;
        bulletViews.add(this);
//        this.currentLocation = currentLocation;
        drawables.add(this);
    }

    public void setCurrentLocation(Point2D currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Point2D getCurrentLocation() {
        return currentLocation;
    }

    public String getId() {
        return id;
    }

    public void remove(){
        drawables.remove(this);
        bulletViews.remove(this);
    }

    @Override
    public void draw(Graphics g) {




        g.setColor(Color.white);
        Point2D location = this.getCurrentLocation();
//        System.out.println(location);
        g.fillOval((int) (location.getX()-BULLET_RADIUS), (int) (location.getY()-BULLET_RADIUS), (int) (2 *BULLET_RADIUS), (int) (2*BULLET_RADIUS));

    }
}
