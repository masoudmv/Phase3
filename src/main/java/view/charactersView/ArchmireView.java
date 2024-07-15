package view.charactersView;

import model.TimedLocation;
import model.charactersModel.GeoShapeModel;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class ArchmireView extends GeoShapeView {
    LinkedList<TimedLocation> locationHistory = new LinkedList<>();
    public ArchmireView(String id, Image image) {
        super(id, image);
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
        g2d.fillPolygon(xpoints, ypoints, myPolygon.npoints);
//        } else {
//            g2d.drawImage(image, (int) currentLocation.getX(), (int) currentLocation.getY(), null);
//        }
    }
}
