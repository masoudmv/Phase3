package client.network.game.view.charactersView;


import shared.Model.MyPolygon;
import shared.Model.TimedLocation;
import shared.Model.imagetools.ToolBox;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static shared.Model.imagetools.ToolBox.getBufferedImage;

public class ArchmireView extends GeoShapeView {
    HashMap<String, List<TimedLocation>> history = new HashMap<>();
    private static Image img;

    public ArchmireView(String id) {
        super(id, img);
        zOrder = 2;
    }



    public static void loadImage(){
        Image img = new ImageIcon("./client/src/archmire.png").getImage();
        img = getBufferedImage(img);
    }


    @Override
    public void setHistory(String panelID, List<TimedLocation> history) {
        this.history.put(panelID, history);
    }

    // hey ...
    public synchronized void eliminate() {
        super.eliminate();
    }


    @Override
    public void draw(Graphics g, String panelID) {
        Graphics2D g2d = (Graphics2D) g;


        int x = (int) locations.get(panelID).getX();
        int y = (int) locations.get(panelID).getY();

        if (locations.get(panelID) == null || image == null) return;


        Color baseColor = Color.BLUE;
        int baseAlpha = 100;

        // Calculate the maximum and minimum alpha values
        int maxAlpha = baseAlpha;
        int minAlpha = 0;

        // Cache alpha values
        Map<Integer, Color> alphaColorCache = new HashMap<>();
        List<TimedLocation> locationHistory = history.get(panelID);

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



        g2d.drawImage(ToolBox.rotateImage(image, Math.toDegrees(-angle)), (int) (x - imageWidth / 2), (int) (y - imageWidth / 2), null);

        g2d.setColor(Color.white);




        MyPolygon myPolygon = myPolygons.get(panelID);

        int[] xpoints = new int[myPolygon.npoints];
        int[] ypoints = new int[myPolygon.npoints];
        for (int i = 0; i < myPolygon.npoints; i++) {
            xpoints[i] = (int) myPolygon.xpoints[i];
            ypoints[i] = (int) myPolygon.ypoints[i];
        }

        g2d.drawPolygon(xpoints, ypoints, myPolygon.npoints);
    }

}