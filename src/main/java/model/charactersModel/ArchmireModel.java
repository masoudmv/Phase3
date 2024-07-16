package model.charactersModel;

import controller.Game;
import model.MyPolygon;
import model.TimedLocation;
import model.collision.Collidable;
import model.movement.Direction;
import org.example.GraphicalObject;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import static controller.UserInterfaceController.createArchmireView;
import static controller.constants.EntityConstants.ARCHMIRE_SPEED;
import static controller.Utils.*;
import static model.imagetools.ToolBox.getBufferedImage;

public class ArchmireModel extends GeoShapeModel implements Collidable {
    static BufferedImage image;
    public static ArrayList<ArchmireModel> archmireModels = new ArrayList<>();
    private LinkedList<TimedLocation> locationHistory = new LinkedList<>();
    private double lastUpdatedLocation = 0;
    public Polygon polygon;
    protected static MyPolygon pol;

    public ArchmireModel(Point2D anchor) {
        super(anchor, image, pol, true);
        archmireModels.add(this);
        updateDirection();
        createArchmireView(id, ArchmireModel.image);
        collidables.add(this);

    }

    // BabyArchmire:
    public ArchmireModel(Point2D anchor, MyPolygon myPolygon) {
        super(anchor, BabyArchmire.image, myPolygon, true);
        archmireModels.add(this);
        updateDirection();
        createArchmireView(id, BabyArchmire.image);

    }



    private void updateDirection(){
        Point2D destination = EpsilonModel.getINSTANCE().getAnchor();
        Point2D newDirection = relativeLocation(destination, getAnchor());
        this.direction = new Direction(newDirection);
        this.direction.setMagnitude(ARCHMIRE_SPEED);
    }

    public static BufferedImage loadImage() {
        Image img = new ImageIcon("./src/archmire.png").getImage();
        ArchmireModel.image = getBufferedImage(img);

        GraphicalObject bowser = new GraphicalObject(image);
        pol = bowser.getMyBoundingPolygon();


        return ArchmireModel.image;
    }

    @Override
    public void setMyPolygon(MyPolygon myPolygon) {
        // Implementation needed
    }

    @Override
    public void eliminate() {
        super.eliminate();
        collidables.remove(this);
        archmireModels.remove(this);

        // TODO: You can do better than this!
        new BabyArchmire(new Point2D.Double(anchor.getX(), anchor.getY()+40));
        new BabyArchmire(new Point2D.Double(anchor.getX(), anchor.getY()-40));

        // TODO: REAL ELIMINATE

    }

    public void updateLocation() {
        double now = Game.ELAPSED_TIME;
        updateDirection();
        if (now - lastUpdatedLocation > 0.5){
            locationHistory.addLast(new TimedLocation(myPolygon, now));
            lastUpdatedLocation = now;
        }
        removeOldLocations(now);
    }

    private void removeOldLocations(double currentTime) {
        while (!locationHistory.isEmpty() && (currentTime - locationHistory.getFirst().getTimestamp() > 5)) {
            locationHistory.removeFirst();
        }
    }

    public LinkedList<TimedLocation> getLocationHistory() {
        return locationHistory;
    }


    void move(Direction direction) {
        Point2D movement = multiplyVector(direction.getNormalizedDirectionVector(), direction.getMagnitude());
        movePolygon(movement);
    }

    public void move() {
        move(direction);
    }

    @Override
    public boolean isCircular() {
        return false;
    }

    @Override
    public void onCollision(Collidable other, Point2D intersection) {
        if (other instanceof BulletModel) eliminate();
    }

    @Override
    public void onCollision(Collidable other) {

    }
}

