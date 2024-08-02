package model.charactersModel;

import model.MyPolygon;
import model.collision.Collidable;
import model.movement.Direction;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static controller.UserInterfaceController.createArchmireView;
import static controller.Utils.relativeLocation;
import static controller.constants.EntityConstants.*;

public class Wyrm extends GeoShapeModel implements Collidable {
    static BufferedImage image;
    protected static MyPolygon pol;
    public static List<Wyrm> wyrms = new ArrayList<>();
    public Polygon polygon;


    public Wyrm(Point2D anchor) {
        super(anchor, image, pol, true);
        wyrms.add(this);
        updateDirection();
        createArchmireView(id, ArchmireModel.image);
        collidables.add(this);
        this.health = WYRM_HEALTH.getValue();
    }

    private void updateDirection(){
        Point2D destination = EpsilonModel.getINSTANCE().getAnchor();
        Point2D newDirection = relativeLocation(destination, getAnchor());
        this.direction = new Direction(newDirection);
        this.direction.setMagnitude(ARCHMIRE_SPEED);
    }



    @Override
    public void setMyPolygon(MyPolygon myPolygon) {

    }

    public void update() {
        if (dontUpdate()) return;

        // some other logic

    }



    @Override
    public boolean isCircular() {
        return false;
    }

    @Override
    public void onCollision(Collidable other, Point2D intersection) {

    }

    @Override
    public void onCollision(Collidable other, Point2D coll1, Point2D coll2) {

    }
}
