package view.charactersView;

import model.MyPolygon;
import model.TimedLocation;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;

import static model.imagetools.ToolBox.getBufferedImage;
import static model.imagetools.ToolBox.rotateImage;

public class GeoShapeView {
    String id;
    Point2D currentLocation;
    BufferedImage image;
    double imageWidth;
    double imageHeight;
    protected MyPolygon myPolygon;
    public static ArrayList<GeoShapeView> geoShapeViews = new ArrayList<>();
    private Point2D slapTarget;
    protected double angle;


    public GeoShapeView(String id, Image image) {
        this.id = id;
        this.image = getBufferedImage(image);
        this.imageWidth = this.image.getWidth();
        this.imageHeight = this.image.getHeight();
        geoShapeViews.add(this);
    }

    public GeoShapeView(String id) {
        this.id = id;
        geoShapeViews.add(this);
    }

    public void setCurrentLocation(Point2D currentLocation) {
        this.currentLocation =
                new Point2D.Double(currentLocation.getX() - imageWidth/2, currentLocation.getY() - imageHeight/2);
    }

    public Point2D getCurrentLocation() {
        return currentLocation;
    }

    public String getId() {
        return id;
    }

    public void setMyPolygon(MyPolygon myPolygon) {
        this.myPolygon = myPolygon;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }
    public void eliminate(){
        geoShapeViews.remove(this);
    }
    public void setLocationHistory(LinkedList<TimedLocation> timedLocations){

    }

    public void setImage(BufferedImage image) {
        this.image = image;
        this.imageWidth = this.image.getWidth();
        this.imageHeight = this.image.getHeight();
    }

    //    public static void drawAll(Graphics ){
//        for (int i = 0; i < GeoShapeModel.entities.size(); i++) {
//            if (GeoShapeModel.entities.get(i).isLaser){
//                Polygon polygon = Controller.calculateEntityView(this, GeoShapeModel.entities.get(i).myPolygon);
//                g.fillPolygon(polygon);
//            }
//        }
//    }




    protected void drawPolygon(){

    }



    public void draw(Graphics g){
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);


        g2d.drawImage(rotateImage(image, angle), (int) currentLocation.getX(), (int) currentLocation.getY(), null);

        int[] xpoints = new int[myPolygon.npoints];
        int[] ypoints = new int[myPolygon.npoints];

        for (int i = 0; i < myPolygon.npoints; i++) {
            xpoints[i] = (int) myPolygon.xpoints[i];
            ypoints[i] = (int) myPolygon.ypoints[i];
        }

//        double radius = imageHeight/2;
//        g2d.drawOval((int) getCurrentLocation().getX(), (int) getCurrentLocation().getY(), (int) (2*radius), (int) (2*radius));
//        g2d.drawOval((int) getCurrentLocation().getX(), (int) getCurrentLocation().getY(), (int) (2), (int) (2));
        g2d.setColor(Color.red);

        g2d.drawPolygon(xpoints, ypoints, myPolygon.npoints);
    }

}
