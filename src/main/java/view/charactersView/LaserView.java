package view.charactersView;

import model.MyPolygon;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class LaserView extends GeoShapeView {
    public boolean showNextLocation = false;
    public static ArrayList<LaserView> laserViews = new ArrayList<>();
//    private MyPolygon nextPolygon;
//    private Point2D nextLocation;

    public LaserView(String id) {
        super(id);
//        geoShapeViews.add(this);
//        laserViews.add(this);
//        geoShapeViews.add(this);
    }

//    private void drawNextLocation(Graphics2D g2d) {
//        int[] xpoints = new int[nextPolygon.npoints];
//        int[] ypoints = new int[nextPolygon.npoints];
//
//        for (int i = 0; i < nextPolygon.npoints; i++) {
//            xpoints[i] = (int) nextPolygon.xpoints[i];
//            ypoints[i] = (int) nextPolygon.ypoints[i];
//        }
//        g2d.drawPolygon(xpoints, ypoints, nextPolygon.npoints);
//    }
//
//    public void setNextPolygon(MyPolygon nextPolygon) {
//        this.nextPolygon = nextPolygon;
//    }
//
//    public void setNextLocation(Point2D nextLocation) {
//        this.nextLocation = nextLocation;
//    }

//    public Point2D getNextLocation() {
//        return nextLocation;
//    }
    public void eliminate(){
        super.eliminate();
//        geoShapeViews.remove(this);
//        laserViews.remove(this);
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
//
//        if (showNextLocation) {
            int[] xpoints = new int[myPolygon.npoints];
            int[] ypoints = new int[myPolygon.npoints];

            for (int i = 0; i < myPolygon.npoints; i++) {
                xpoints[i] = (int) myPolygon.xpoints[i];
                ypoints[i] = (int) myPolygon.ypoints[i];
            }
            g2d.drawPolygon(xpoints, ypoints, myPolygon.npoints);
//        } else {
//            g2d.drawImage(image, (int) currentLocation.getX(), (int) currentLocation.getY(), null);
//        }
    }
}
