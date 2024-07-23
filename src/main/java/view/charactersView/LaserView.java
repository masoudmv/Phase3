package view.charactersView;

import controller.Game;
import model.MyPolygon;
import model.charactersModel.GeoShapeModel;
import model.charactersModel.blackOrb.Laser;
import model.entities.Entity;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import static controller.UserInterfaceController.findGeoShapeModel;
import static controller.constants.Constants.AVALANCHE_DURATION;
import static model.entities.Entity.entities;

public class LaserView extends GeoShapeView {
    public boolean showNextLocation = false;
    public static ArrayList<LaserView> laserViews = new ArrayList<>();
    private boolean isAvalanche = false;
    private double avalancheInitiation = -1;

    public LaserView(String id) {
        super(id);
        this.zOrder = 1;
    }

    @Override
    public void update(Component component){
        super.update(component);
        GeoShapeModel geoShapeModel = findGeoShapeModel(id);
        this.isAvalanche = ((Laser) geoShapeModel).isAvalanche();
        this.avalancheInitiation = ((Laser) geoShapeModel).getAvalancheInitiation();
    }

    public void eliminate(){
        super.eliminate();
    }


    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);

            int[] xpoints = new int[myPolygon.npoints];
            int[] ypoints = new int[myPolygon.npoints];

            for (int i = 0; i < myPolygon.npoints; i++) {
                xpoints[i] = (int) myPolygon.xpoints[i];
                ypoints[i] = (int) myPolygon.ypoints[i];
            }

            if (isAvalanche) {
                double now = Game.ELAPSED_TIME;
                double avalancheStart = avalancheInitiation;
                double avalancheEnd = avalancheStart + AVALANCHE_DURATION;
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
