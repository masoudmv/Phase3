package client.network.game.view.charactersView;


import shared.Model.MyPolygon;

import java.awt.*;

public class LaserView extends GeoShapeView {

    public LaserView(String id) {
        super(id);
        this.zOrder = 1;
    }



    public void eliminate(){
        super.eliminate();
    }


    @Override
    public void draw(Graphics g, String panelID) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);

        MyPolygon myPolygon = myPolygons.get(panelID);
        if (myPolygon == null) {
            System.out.println("IS nuL");
            return;
        }

        int[] xpoints = new int[myPolygon.npoints];
        int[] ypoints = new int[myPolygon.npoints];

        for (int i = 0; i < myPolygon.npoints; i++) {
            xpoints[i] = (int) myPolygon.xpoints[i];
            ypoints[i] = (int) myPolygon.ypoints[i];
        }

        g2d.drawPolygon(xpoints, ypoints, xpoints.length);
    }
}
