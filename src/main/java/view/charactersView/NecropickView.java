package view.charactersView;

import model.MyPolygon;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class NecropickView extends GeoShapeView{
    public boolean showNextLocation = false;
    public static ArrayList<NecropickView> necropickViews = new ArrayList<>();
    private MyPolygon nextPolygon;
    private Point2D nextLocation;
    public NecropickView(String id, Image image) {
        super(id, image);
        necropickViews.add(this);
    }

    private void drawNextLocation(Graphics2D g2d){
        int[] xpoints = new int[nextPolygon.npoints];
        int[] ypoints = new int[nextPolygon.npoints];

        for (int i = 0; i < nextPolygon.npoints; i++) {
            xpoints[i] = (int) nextPolygon.xpoints[i];
            ypoints[i] = (int) nextPolygon.ypoints[i];

        }
        g2d.drawPolygon(xpoints, ypoints, nextPolygon.npoints);
    }

    public void setNextPolygon(MyPolygon nextPolygon) {
        this.nextPolygon = nextPolygon;
    }

    public void setNextLocation(Point2D nextLocation) {
        this.nextLocation = nextLocation;
    }

    public Point2D getNextLocation() {
        return nextLocation;
    }

    public void draw(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        if (showNextLocation) {
            drawNextLocation(g2d);
//            g.fillOval((int) nextLocation.getX(), (int) nextLocation.getY(), 10, 10);
        }
    }
}
