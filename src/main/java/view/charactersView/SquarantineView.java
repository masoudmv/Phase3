package view.charactersView;

import view.MainPanel;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import static controller.Constants.SQUARANTINE_EDGE;
import static controller.Game.bufferedImageResult;
//import static controller.Game.squarantine;

public class SquarantineView implements Drawable{
    String id;
    Point2D currentLocation=new Point2D.Double(0,0);
    public static ArrayList<SquarantineView> squarantineViews=new ArrayList<>();
    Point2D point1= new Point2D.Double(0,0);
    Point2D point2= new Point2D.Double(0,0);
    Point2D point3= new Point2D.Double(0,0);
    Point2D point4= new Point2D.Double(0,0);
    private Point2D[] vertices = new Point2D[]{point1,point2,point3,point4};
    public SquarantineView(String id) {
        this.id = id;
        squarantineViews.add(this);
        drawables.add(this);
    }

    public Point2D getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Point2D currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getId() {
        return id;
    }
    @Override
    public void draw(Graphics g){
        int[] xPoly = new int[]{(int) vertices[0].getX(), (int) vertices[1].getX(), (int) vertices[2].getX(), (int) vertices[3].getX()};
        int[] yPoly = new int[]{(int) vertices[0].getY(), (int) vertices[1].getY(), (int) vertices[2].getY(), (int) vertices[3].getY()};
        g.setColor(Color.white);
        Polygon poly = new Polygon(xPoly, yPoly, xPoly.length);
        g.setColor(Color.green);
        g.drawPolygon(poly);
//        g.fillPolygon(poly);



//        g.drawImage(bufferedImageResult, xPoly[0]-(int) SQUARANTINE_EDGE/2, yPoly[0]-(int) SQUARANTINE_EDGE/2, MainPanel.getINSTANCE()); // see javadoc for more info on the parameters

    }

    public void setVertices(Point2D[] vertices) {
        this.vertices = vertices;
    }

    public void remove() {
        drawables.remove(this);
        squarantineViews.remove(this);
    }
}