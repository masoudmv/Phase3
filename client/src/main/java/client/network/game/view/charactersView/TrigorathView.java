package client.network.game.view.charactersView;

import client.network.game.view.FinalPanelView;

import shared.model.MyPolygon;

import java.awt.*;

public class TrigorathView extends GeoShapeView {

    public TrigorathView(String id) {
        super(id);

    }

    public void eliminate(){
        super.eliminate();
    }


    @Override
    public void draw(Graphics g, String panelID){
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.yellow);
        // Set the stroke for thicker edges
        float thickness = 5.0f; // Thickness of the edges
        g2d.setStroke(new BasicStroke(thickness));

        for (FinalPanelView finalPanelView : FinalPanelView.finalPanelViews){
            if (finalPanelView.getId().equals(panelID)) {
                MyPolygon myPolygon = myPolygons.get(finalPanelView.getId());
                if (myPolygon != null) {
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
}