package model.charactersModel;

import controller.Game;
import model.MyPolygon;
import model.TimedLocation;
import model.collision.Collidable;
import model.entities.AttackTypes;
import model.interfaces.Enemy;
import model.movement.Direction;
import org.example.GraphicalObject;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import static controller.UserInterfaceController.createArchmireView;
import static controller.Utils.*;
import static controller.constants.EntityConstants.*;
import static model.charactersModel.EpsilonModel.epsilons;
import static model.imagetools.ToolBox.getBufferedImage;

public class ArchmireModel extends GeoShapeModel implements Collidable, Enemy {
    static BufferedImage image;
    protected static MyPolygon pol;
    public static ArrayList<ArchmireModel> archmireModels = new ArrayList<>();
    private LinkedList<TimedLocation> locationHistory = new LinkedList<>();
    private double lastUpdatedLocation = 0;
    public Polygon polygon;

    public ArchmireModel(Point2D anchor) {
        super(anchor, image, pol, true);
        this.health = ARCHMIRE_HEALTH.getValue();
        this.isHovering = true;
        updateDirection();
        createArchmireView(id, ArchmireModel.image);
        archmireModels.add(this);
        collidables.add(this);
        damageSize.put(AttackTypes.DROWN, 10);
        damageSize.put(AttackTypes.AOE, 2);
    }

    public ArchmireModel() {
    }

    // BabyArchmire:
    public ArchmireModel(Point2D anchor, MyPolygon myPolygon) {
        super(anchor, BabyArchmire.image, myPolygon, true);
        updateDirection();
        createArchmireView(id, BabyArchmire.image);

        archmireModels.add(this);
        collidables.add(this);
        damageSize.put(AttackTypes.DROWN, 5);
        damageSize.put(AttackTypes.AOE, 1);
    }



    private void updateDirection(){
        Point2D destination = EpsilonModel.getINSTANCE().getAnchor();
        Point2D newDirection = relativeLocation(destination, getAnchor());
        this.direction = new Direction(newDirection);
        this.direction.setMagnitude(ARCHMIRE_SPEED);
    }

    public static BufferedImage loadImage() {
        Image img = new ImageIcon("./client/src/archmire.png").getImage();
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

        Game.getINSTANCE().incrementDeadEnemies();
        CollectibleModel.dropCollectible(getAnchor(), ARCHMIRE_NUM_OF_COLLECTIBLES.getValue(), ARCHMIRE_COLLECTIBLES_XP.getValue());

        new BabyArchmire(new Point2D.Double(anchor.getX(), anchor.getY() + 40));
        new BabyArchmire(new Point2D.Double(anchor.getX(), anchor.getY() - 40));
        // TODO: REAL ELIMINATE



    }



    public void eli() {
        super.eliminate();
        collidables.remove(this);
        archmireModels.remove(this);
    }


    public void updateLocation() {
        double now = Game.elapsedTime;
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


    void update(Direction direction) {
        Point2D movement = multiplyVector(direction.getNormalizedDirectionVector(), direction.getMagnitude());
        movePolygon(movement);
        updateLocation();
        applyAOE();
    }


    private void applyAOE(){
        for (EpsilonModel model : epsilons){
            boolean isInside = model.isInside(myPolygon.getVertices());
            if (isInside) {
                this.damage(model, AttackTypes.DROWN);
                return;
            }


            for (TimedLocation location : locationHistory){
                isInside = model.isInside(location.getMyPolygon().getVertices());
                if (isInside) {
                    this.damage(model, AttackTypes.AOE);
                }
            }
        }
    }

    public void update() {
        if (dontUpdate()) return;
        update(direction);
    }

    @Override
    public boolean isCircular() {
        return false;
    }

    @Override
    public void onCollision(Collidable other, Point2D intersection) {
//        if (other instanceof BulletModel) eliminate();
    }

    @Override
    public void onCollision(Collidable other, Point2D coll1, Point2D coll2) {

    }

    @Override
    public void create() {
        Point2D anchor = findRandomPoint();
        new ArchmireModel(anchor);
    }

    @Override
    public int getMinSpawnWave() {
        return 2;
    }
}

