package view.charactersView;

import model.MyPolygon;
import view.FinalPanelView;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import static model.imagetools.ToolBox.rotateImage;
//import static controller.Game.squarantine;

public class SquarantineView extends GeoShapeView{


    public SquarantineView(String id) {
        super(id);
    }

    public void eliminate(){
        super.eliminate();
    }




//    public String getId() {
//        return id;
//    }


    public void draw(Graphics g, String panelID){
        Graphics2D g2d = (Graphics2D) g;


        g2d.setColor(Color.green);
        // Set the stroke for thicker edges
        float thickness = 5.0f; // Thickness of the edges
        g2d.setStroke(new BasicStroke(thickness));

        for (FinalPanelView finalPanelView : FinalPanelView.finalPanelViews){
            if (finalPanelView.getId().equals(panelID)) {
                MyPolygon myPolygon = myPolygons.get(finalPanelView.getId());
                int[] xpoints = new int[myPolygon.npoints];
                int[] ypoints = new int[myPolygon.npoints];

                for (int i = 0; i < myPolygon.npoints; i++) {
                    xpoints[i] = (int) myPolygon.xpoints[i];
                    ypoints[i] = (int) myPolygon.ypoints[i];
                }
                g2d.drawPolygon(xpoints, ypoints, myPolygon.npoints);


            }
        }
    }

}