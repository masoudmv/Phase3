package view.charactersView;

import model.MyPolygon;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

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
    private double angle;

    public GeoShapeView(String id, Image image) {
        this.id = id;
        this.image = getBufferedImage(image);
        this.imageWidth = this.image.getWidth();
        this.imageHeight = this.image.getHeight();
        geoShapeViews.add(this);
    }


    public GeoShapeView(String id) {
        this.id = id;
    }

    public void setCurrentLocation(Point2D currentLocation) {
        this.currentLocation = new Point2D.Double(
                currentLocation.getX() - imageWidth / 2,
                currentLocation.getY() - imageHeight / 2
        );
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

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        g2d.drawImage(rotateImage(image, angle), (int) currentLocation.getX(), (int) currentLocation.getY(), null);
    }
}
