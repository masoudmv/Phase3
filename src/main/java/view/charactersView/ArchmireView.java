package view.charactersView;

import controller.Game;
import model.TimedLocation;
import model.charactersModel.GeoShapeModel;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static model.imagetools.ToolBox.rotateImage;

public class ArchmireView extends GeoShapeView {
    LinkedList<TimedLocation> locationHistory = new LinkedList<>();
    public ArchmireView(String id, Image image) {
        super(id, image);
    }

    @Override
    public void setLocationHistory(LinkedList<TimedLocation> locationHistory){
        this.locationHistory = locationHistory;
//        System.out.println("IsUpdating");
    }

//
//    @Override
//    public void draw(Graphics g) {
//        Graphics2D g2d = (Graphics2D) g;
//        Color baseColor = Color.BLUE;
//        int baseAlpha = 100;
//
//        // Calculate the maximum and minimum alpha values
//        int maxAlpha = baseAlpha;
//        int minAlpha = 0;
//
//        // Cache alpha values
//        Map<Integer, Color> alphaColorCache = new HashMap<>();
//
//        for (TimedLocation location : locationHistory) {
//            Polygon pol = location.getPolygon();
//            double time = Game.ELAPSED_TIME - location.getTimestamp();
//            int alpha = Math.max(minAlpha, Math.min(maxAlpha, baseAlpha - (int) (time * baseAlpha / 5)));
//
//            // Get or create the dimmer color with the cached alpha value
//            Color dimmerColor = alphaColorCache.computeIfAbsent(alpha, a ->
//                    new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), a)
//            );
//
//            g2d.setColor(dimmerColor);
//            g2d.fillPolygon(pol);
//        }
//
//        g2d.drawImage(rotateImage(image, angle), (int) currentLocation.getX(), (int) currentLocation.getY(), null);
//    }

}
