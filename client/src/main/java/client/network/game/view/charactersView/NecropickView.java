package client.network.game.view.charactersView;


import shared.model.MyPolygon;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import static shared.model.imagetools.ToolBox.getBufferedImage;
import static shared.model.imagetools.ToolBox.rotateImage;

public class NecropickView extends GeoShapeView {
    public boolean showNextLocation = false;
    public static ArrayList<NecropickView> necropickViews = new ArrayList<>();
    private MyPolygon nextPolygon;
    private Point2D nextLocation;
    private static Image img;


    public NecropickView(String id) {
        super(id, img);
        necropickViews.add(this);
    }


    public static void loadImage(){
        Image img = new ImageIcon("./client/src/necropick.png").getImage();
        NecropickView.img = getBufferedImage(img);
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



    @Override
    public void draw(Graphics g, String panelID) {
        if (locations.get(panelID) == null || image == null) return;


        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);

        if (showNextLocation) {
            MyPolygon myPolygon = myPolygons.get(panelID);

            int[] xpoints = new int[myPolygon.npoints];
            int[] ypoints = new int[myPolygon.npoints];

            for (int i = 0; i < myPolygon.npoints; i++) {
                xpoints[i] = (int) myPolygon.xpoints[i];
                ypoints[i] = (int) myPolygon.ypoints[i];
            }
            g2d.drawPolygon(xpoints, ypoints, myPolygon.npoints);
        } else {
            int x = (int) locations.get(panelID).getX();
            int y = (int) locations.get(panelID).getY();
            g2d.drawImage(rotateImage(image, Math.toDegrees(-angle)), (int) (x - imageWidth/2), (int) (y - imageWidth/2), null);

        }
    }
}
