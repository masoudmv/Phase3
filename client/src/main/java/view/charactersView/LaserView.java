package view.charactersView;

import controller.Game;
import model.MyPolygon;
import java.awt.*;

import static controller.constants.Constants.AVALANCHE_DURATION;

public class LaserView extends GeoShapeView {
    private boolean isAvalanche = false;
    private double avalancheInitiation = -1;

    public LaserView(String id) {
        super(id);
        this.zOrder = 1;
    }


    public void eliminate(){
        super.eliminate();
    }


    public void setAvalanche(boolean avalanche) {
        isAvalanche = avalanche;
    }

    public void setAvalancheInitiation(double avalancheInitiation) {
        this.avalancheInitiation = avalancheInitiation;
    }

    @Override
    public void draw(Graphics g, String panelID) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);


        MyPolygon myPolygon = myPolygons.get(panelID);
        if (myPolygon == null) return;

        int[] xpoints = new int[myPolygon.npoints];
        int[] ypoints = new int[myPolygon.npoints];

        for (int i = 0; i < myPolygon.npoints; i++) {
            xpoints[i] = (int) myPolygon.xpoints[i];
            ypoints[i] = (int) myPolygon.ypoints[i];
        }


        if (isAvalanche) {
            double now = Game.elapsedTime;
            double avalancheStart = avalancheInitiation;
            double avalancheEnd = avalancheStart + AVALANCHE_DURATION;
            if (now < avalancheStart) {
                g2d.setColor(Color.red);
                g2d.drawPolygon(xpoints, ypoints, myPolygon.npoints);
            }
            else if (avalancheStart < now && now < avalancheEnd) {
            }
            else {
                g2d.drawPolygon(xpoints, ypoints, myPolygon.npoints);
            }
        }
        else g2d.drawPolygon(xpoints, ypoints, myPolygon.npoints);

    }
}
