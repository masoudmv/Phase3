package client.network.game.view.charactersView;

import client.network.game.controller.UserInterfaceController;
import client.network.game.controller.Utils;
import client.network.game.view.FinalPanelView;

import shared.Model.MyPolygon;
import shared.Model.TimedLocation;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static shared.Model.imagetools.ToolBox.getBufferedImage;
import static shared.Model.imagetools.ToolBox.rotateImage;

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

//        initiator();
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

            Point2D currentLocation = UserInterfaceController.calculateViewLocationPolygonalEnemy(finalPanelView, this.getId());
            Point corner = new Point(finalPanelView.getX(), finalPanelView.getY());

            Point2D p =  Utils.relativeLocation(anchor, corner);
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




    protected void drawPolygon(){

    }

    public int getZOrder() {
        return zOrder;
    }

    public void setZOrder(int zOrder) {this.zOrder = zOrder;}

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

        g2d.drawImage(rotateImage(image, Math.toDegrees(-angle)), (int) (x - imageWidth/2), (int) (y - imageWidth/2), null);

        g2d.drawPolygon(xpoints, ypoints, myPolygon.npoints);
    }
}
