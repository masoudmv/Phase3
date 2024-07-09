package view.charactersView;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayDeque;
import java.util.ArrayList;

import static controller.Constants.RADIUS;

public class EpsilonView implements Drawable{
    String id;
    Point2D currentLocation=new Point2D.Double(0,0);
    public static ArrayList<EpsilonView> epsilonViews=new ArrayList<>();
    private  ArrayList<Point2D> vertices = new ArrayList<>();
    public EpsilonView(String id) {
        this.id = id;
        epsilonViews.add(this);
        drawables.add(this);
    }

    public Point2D getCurrentLocation() {
        return currentLocation;
    }
    public void setVertices(ArrayList<Point2D> vertices){
        this.vertices = vertices;
    }

    public void setCurrentLocation(Point2D currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getId() {
        return id;
    }
    @Override
    public void draw (Graphics g){
        g.setColor(Color.white);
        Point2D location = this.getCurrentLocation();
        g.drawOval((int) (location.getX()-RADIUS), (int) (location.getY()-RADIUS), (int) (2 *RADIUS), (int) (2*RADIUS));
//        g.fillOval((int) (location.getX()-RADIUS), (int) (location.getY()-RADIUS), (int) (2 *RADIUS), (int) (2*RADIUS));
        for (int i = 0; i < vertices.size(); i++) {
            g.setColor(Color.red);
            g.fillOval((int) vertices.get(i).getX()-2, (int) vertices.get(i).getY()-2, 4, 4);
//            g.setColor(Color.red);
//            g.setClip(1,2,3,4);
//            g.drawLine((int) vertices.get(i).getX(), (int) vertices.get(i).getY(),
//                    (int) vertices.get(i).getX(), (int) vertices.get(i).getY());
        }
    }

}