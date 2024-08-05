package game.model.charactersModel;
import game.controller.Game;
import game.controller.UserInterfaceController;
import game.controller.Utils;
import shared.constants.Constants;
import shared.constants.EntityConstants;
import shared.Model.MyPolygon;
import game.model.collision.Collidable;
import game.model.movement.Direction;
import game.model.movement.Movable;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class CollectibleModel extends GeoShapeModel implements Collidable, Movable {

//    double radius;
//    private Point2D anchor;
    public static CopyOnWriteArrayList<CollectibleModel> collectibleModels = new CopyOnWriteArrayList<>();
//    public Direction direction;
    public boolean impactInProgress;
    public double impactMaxVel;
    public double birthTime;
    private static final Random random = new Random();
    private int collectibleXP;


    public CollectibleModel(Point2D anchor, Point2D direction, int collectibleXP, String gameID) {
        super(gameID);
        birthTime = findGame(gameID).ELAPSED_TIME;
        this.radius = EntityConstants.COLLECTABLE_RADIUS.getValue();
//        this.id= UUID.randomUUID().toString();
        this.anchor = anchor;
        this.direction = new Direction(direction);
        this.direction.adjustDirectionMagnitude();
        this.collectibleXP = collectibleXP;
        this.health = Integer.MAX_VALUE;
        impactInProgress = true;
        impactMaxVel = 1.75;
        setDummyPolygon();
        collectibleModels.add(this);
        collidables.add(this);
        UserInterfaceController.createCollectibleView(id, gameID);

    }

    public int getCollectibleXP() {
        return collectibleXP;
    }

    public void setCollectibleXP(int collectibleXP) {
        this.collectibleXP = collectibleXP;
    }




    public static void dropCollectible(Point2D anchor, int numOfCollectibles, int xp, String gameID) {
        Point2D direction = new Point2D.Double(1, 0);
        for (int i = 0; i < numOfCollectibles; i++) {
            double theta = random.nextDouble(0, 2 * Constants.PI);
            new CollectibleModel(anchor, Utils.rotateVector(direction, theta), xp, gameID);
        }
    }


    public String getId() {
        return id;
    }

    @Override
    public boolean isCircular() {
        return true;
    }

    @Override
    public void setDirection(Direction direction) {

    }

    @Override
    public Direction getDirection() {
        return this.direction;
    }

    @Override
    public void update(Direction direction) {
        Point2D movement = Utils.multiplyVector(direction.getDirectionVector(), direction.getMagnitude());
        this.anchor = Utils.addVectors(anchor, movement);
        friction();
    }

    @Override
    public void update() {
        super.update();
        update(direction);
    }

    @Override
    public void friction() {
        direction.setMagnitude(direction.getMagnitude() * 0.97);
        if (direction.getMagnitude() < 1){
            direction.setMagnitude(0);
            impactInProgress = false;

        }
    }

    @Override
    public double getRadius() {
        return this.radius;
    }

    @Override
    public Point2D getAnchor() {
        return this.anchor;
    }

    @Override
    public void setMyPolygon(MyPolygon myPolygon) {

    }

    @Override
    public Point2D[] getVertices() {
        return getVertices();
    }

    @Override
    public ArrayList<Line2D> getEdges() {
        return null;
    }

    @Override
    public void onCollision(Collidable other, Point2D intersection) {
        if (other instanceof EpsilonModel) eliminate();
    }

    @Override
    public void onCollision(Collidable other, Point2D coll1, Point2D coll2) {

    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public ArrayList<Point2D> getBoundingPoints(){
        ArrayList<Point2D> bound = new ArrayList<>();
        bound.add(new Point2D.Double(getAnchor().getX() - getRadius(), getAnchor().getY()));
        bound.add(new Point2D.Double(getAnchor().getX() + getRadius(), getAnchor().getY()));
        bound.add(new Point2D.Double(getAnchor().getX(), getAnchor().getY() + getRadius()));
        bound.add(new Point2D.Double(getAnchor().getX(), getAnchor().getY() - getRadius()));
        return bound;
    }



    public void eliminate(){
        super.eliminate();
        collidables.remove(this);
        movables.remove(this);
        collectibleModels.remove(this);
        Game.inGameXP += collectibleXP;
//        findCollectibleView((this).getId()).remove();
    }


}
