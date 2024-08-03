package game.view.charactersView;

import game.controller.Game;
import game.controller.constants.Constants;
import shared.Model.MyPolygon;

import java.awt.*;
import java.util.ArrayList;

public class LaserView extends GeoShapeView {
    public boolean showNextLocation = false;
    public static ArrayList<LaserView> laserViews = new ArrayList<>();
    private boolean isAvalanche = false;
    private double avalancheInitiation = -1;

    public LaserView(String id) {
        super(id);
        this.zOrder = 1;
    }

    // TODO

//    @Override
//    public void update(Component component){
//        super.update(component);
//        GeoShapeModel geoShapeModel = findGeoShapeModel(id);
//        this.isAvalanche = ((Laser) geoShapeModel).isAvalanche();
//        this.avalancheInitiation = ((Laser) geoShapeModel).getAvalancheInitiation();
//    }

    public void eliminate(){
        super.eliminate();
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
            double now = Game.ELAPSED_TIME;
            double avalancheStart = avalancheInitiation;
            double avalancheEnd = avalancheStart + Constants.AVALANCHE_DURATION;
            if (now < avalancheStart) {
                g2d.fillPolygon(xpoints, ypoints, myPolygon.npoints);
            }
            else if (avalancheStart < now && now < avalancheEnd) {
//                    g2d.fillPolygon(xpoints, ypoints, myPolygon.npoints);
            }
            else {
                g2d.drawPolygon(xpoints, ypoints, myPolygon.npoints);
            }
        }
        else g2d.drawPolygon(xpoints, ypoints, myPolygon.npoints);

    }
}
