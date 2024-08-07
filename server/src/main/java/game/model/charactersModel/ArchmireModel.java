package game.model.charactersModel;

import game.controller.Game;
import game.controller.GameType;
import game.controller.UserInterfaceController;
import game.controller.Utils;
import game.model.reflection.Enemy;
import javafx.scene.shape.Arc;
import shared.constants.EntityConstants;
import game.example.GraphicalObject;
import shared.Model.MyPolygon;
import shared.Model.TimedLocation;
import game.model.entities.AttackTypes;
import game.model.collision.Collidable;
import game.model.movement.Direction;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;


import static game.controller.UserInterfaceController.createArchmireView;
import static game.controller.UserInterfaceController.createBabyArchmireView;
import static shared.Model.imagetools.ToolBox.getBufferedImage;

public class ArchmireModel extends GeoShapeModel implements Collidable, Enemy {
    static BufferedImage image;
    protected static MyPolygon pol;
    private LinkedList<TimedLocation> locationHistory = new LinkedList<>();
    private double lastUpdatedLocation = 0;
    public Polygon polygon;
    private double lastAOE = -Double.MAX_VALUE;

    public ArchmireModel(Point2D anchor, String gameID) {
        super(anchor, image, pol, gameID);
        setTarget();
        this.health = EntityConstants.ARCHMIRE_HEALTH.getValue();
        this.isHovering = true;
        updateDirection();
        createArchmireView(id, gameID);
        collidables.add(this);
        damageSize.put(AttackTypes.DROWN, 10);
        damageSize.put(AttackTypes.AOE, 2);
    }

    // BabyArchmire:
    public ArchmireModel(Point2D anchor, MyPolygon myPolygon, String gameID) {
        super(anchor, BabyArchmire.image, myPolygon, gameID);
        setTarget();
        updateDirection();
        createBabyArchmireView(id, gameID);

        collidables.add(this);
        damageSize.put(AttackTypes.DROWN, 5);
        damageSize.put(AttackTypes.AOE, 1);
    }

    public ArchmireModel() {
    }

    private void updateDirection(){
        Point2D destination = target.getAnchor();
        Point2D newDirection = Utils.relativeLocation(destination, getAnchor());
        this.direction = new Direction(newDirection);
        this.direction.setMagnitude(EntityConstants.ARCHMIRE_SPEED);
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

        findGame(gameID).enemyEliminated();

        CollectibleModel.dropCollectible(getAnchor(),
                EntityConstants.ARCHMIRE_NUM_OF_COLLECTIBLES.getValue(),
                EntityConstants.ARCHMIRE_COLLECTIBLES_XP.getValue(),
                gameID
        );

        new BabyArchmire(new Point2D.Double(anchor.getX(), anchor.getY() + 40), gameID);
        new BabyArchmire(new Point2D.Double(anchor.getX(), anchor.getY() - 40), gameID);
        // TODO: REAL ELIMINATE

    }



    public void eli() {
        super.eliminate();
        collidables.remove(this);
    }


    public void updateLocation() {
        double now = findGame(gameID).ELAPSED_TIME;
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
        Point2D movement = Utils.multiplyVector(direction.getNormalizedDirectionVector(), direction.getMagnitude());
        movePolygon(movement);
        updateLocation();
        applyAOE();
    }


    private void applyAOE(){
        for (EpsilonModel model : findGame(gameID).epsilons){
            boolean isInside = model.isInside(myPolygon.getVertices());
            if (isInside) {
                double now = findGame(gameID).ELAPSED_TIME;
                if (now - lastAOE > 1) {
                    this.damage(model, AttackTypes.DROWN);
                    lastAOE = now;
                    return;
                }
            }


            for (TimedLocation location : locationHistory){
                isInside = model.isInside(location.getMyPolygon().getVertices());
                if (isInside) {
                    double now = findGame(gameID).ELAPSED_TIME;
                    if (now - lastAOE > 1) {
                        this.damage(model, AttackTypes.AOE);
                        lastAOE = now;
                        return;
                    }
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
    public void create(String gameID) {
        Point2D anchor = findRandomPoint();
        GameType type = findGame(gameID).getGameType();
        switch (type) {
            case monomachia: {
                Point2D pivot = getSymmetricPoint(anchor);
                new ArchmireModel(pivot, gameID);
                new ArchmireModel(anchor, gameID);
                break;
            }
            case colosseum:{
                new ArchmireModel(anchor, gameID);
                break;
            }
        }

    }

    @Override
    public int getMinSpawnWave() {
        return 2;
    }
}

