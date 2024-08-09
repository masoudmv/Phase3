package view.charactersView;

import model.MyPolygon;
import model.TimedLocation;
import view.FinalPanelView;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static controller.Utils.relativeLocation;
import static model.imagetools.ToolBox.getBufferedImage;
import static model.imagetools.ToolBox.rotateImage;

public class GeoShapeView {
    String id;
    BufferedImage image;
    double imageWidth;
    double imageHeight;
    public volatile static CopyOnWriteArrayList<GeoShapeView> geoShapeViews = new CopyOnWriteArrayList<>();
    protected double angle;
    protected int zOrder = 3;

    protected ConcurrentHashMap<String, Point2D> locations = new ConcurrentHashMap<>();
    protected ConcurrentHashMap<String, MyPolygon> myPolygons = new ConcurrentHashMap<>();


    public GeoShapeView(String id, Image image, Point2D anchor, MyPolygon myPolygon) {
        this.id = id;
        this.image = getBufferedImage(image);
        this.imageWidth = this.image.getWidth();
        this.imageHeight = this.image.getHeight();
        geoShapeViews.add(this);
        initiator(anchor, myPolygon);
    }


    public GeoShapeView(String id, Image image, int zOrder) {
        this.id = id;
        this.image = getBufferedImage(image);
        this.imageWidth = this.image.getWidth();
        this.imageHeight = this.image.getHeight();
        geoShapeViews.add(this);
        this.zOrder = zOrder;
    }


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

    public void setCurrentLocation(String panelID, Point2D currentLocation) {


        this.locations.put(panelID, currentLocation);
    }


    public String getId() {
        return id;
    }

    public void setMyPolygon(String panelID, MyPolygon myPolygon) {
        this.myPolygons.put(panelID, myPolygon);
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public synchronized void eliminate(){
        geoShapeViews.remove(this);
    }

    public void setHistory(String panelID, List<TimedLocation> history){}


    public void setImage(BufferedImage image) {
        this.image = image;
        this.imageWidth = this.image.getWidth();
        this.imageHeight = this.image.getHeight();
    }

    protected void initiator(Point2D anchor, MyPolygon myPolygon){


        for (FinalPanelView finalPanelView : FinalPanelView.finalPanelViews){ // finalPanelModel???
            String panelID = finalPanelView.getId();

            Point corner = new Point(finalPanelView.getX(), finalPanelView.getY());

            Point2D p =  relativeLocation(anchor, corner);
            this.setCurrentLocation(panelID, p);

            double[] xpoints = new double[myPolygon.npoints];
            double[] ypoints = new double[myPolygon.npoints];

            for (int i = 0; i < myPolygon.npoints; i++) {
                xpoints[i] = myPolygon.xpoints[i] - finalPanelView.getX();
                ypoints[i] = myPolygon.ypoints[i] - finalPanelView.getY();
            }


            MyPolygon myPol = new MyPolygon(xpoints, ypoints, myPolygon.npoints);
            this.setMyPolygon(panelID, myPol);

        }
    }



    public int getZOrder() {
        return zOrder;
    }

    public void draw(Graphics g, String panelID){
        Graphics2D g2d = (Graphics2D) g;
        if (locations.get(panelID) == null || image == null) return;

        g2d.setColor(Color.white);

        int x = (int) locations.get(panelID).getX();
        int y = (int) locations.get(panelID).getY();

        MyPolygon myPolygon = myPolygons.get(panelID);

        int[] xpoints = new int[myPolygon.npoints];
        int[] ypoints = new int[myPolygon.npoints];
        for (int i = 0; i < myPolygon.npoints; i++) {
            xpoints[i] = (int) myPolygon.xpoints[i];
            ypoints[i] = (int) myPolygon.ypoints[i];
        }

        // Rotate the image and get the new dimensions
        BufferedImage rotatedImage = rotateImage(image, angle);
        int rotatedWidth = rotatedImage.getWidth();
        int rotatedHeight = rotatedImage.getHeight();

        // Draw the rotated image centered at (x, y)
        g2d.drawImage(rotatedImage, (x - rotatedWidth / 2), (y - rotatedHeight / 2), null);

        g2d.drawPolygon(xpoints, ypoints, myPolygon.npoints);
    }
}
