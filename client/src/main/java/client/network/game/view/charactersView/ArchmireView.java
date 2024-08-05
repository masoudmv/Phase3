package client.network.game.view.charactersView;


import shared.Model.MyPolygon;
import shared.Model.TimedLocation;
import shared.Model.imagetools.ToolBox;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static shared.Model.imagetools.ToolBox.getBufferedImage;

public class ArchmireView extends GeoShapeView {
//    HashMap<String, List<TimedLocation>> history = new HashMap<>();
    List<Polygon> polygons = new ArrayList<Polygon>();
    private static Image img;

    public ArchmireView(String id) {
        super(id, img);
        zOrder = 2;
    }


    public ArchmireView(String id, Image image) {
        super(id, image);
        zOrder = 2;
    }



    public static void loadImage(){
        Image img = new ImageIcon("./client/src/archmire.png").getImage();
        ArchmireView.img = getBufferedImage(img);
    }

    public void setHistory(String panelID, List<Polygon> polygons){
        this.polygons = polygons;
    }



//    @Override
//    public void setHistory(String panelID, List<TimedLocation> history) {
//        this.history.put(panelID, history);
//    }

    // hey ...
    public synchronized void eliminate() {
        super.eliminate();
    }


    @Override
    public void draw(Graphics g, String panelID) {
        Graphics2D g2d = (Graphics2D) g;

        if (locations.get(panelID) == null || image == null) return;

        int x = (int) locations.get(panelID).getX();
        int y = (int) locations.get(panelID).getY();
        g2d.setColor(Color.BLUE);

        for (Polygon polygon : polygons){
            g2d.fillPolygon(polygon);
        }


        g2d.drawImage(ToolBox.rotateImage(image, Math.toDegrees(-angle)), (int) (x - imageWidth / 2), (int) (y - imageWidth / 2), null);
    }

}