package model.charactersModel;
import model.MyPolygon;
import model.collision.Collidable;
import model.movement.Direction;
import model.movement.Movable;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import static controller.UserInterfaceController.*;
import static controller.Game.ELAPSED_TIME;
import static controller.Utils.*;
import static controller.Utils.addVectors;
import static controller.constants.Constants.PI;
import static controller.constants.EntityConstants.COLLECTABLE_RADIUS;

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


    public CollectibleModel(Point2D anchor, Point2D direction, int collectibleXP) {
        super();
        birthTime = ELAPSED_TIME;
        this.radius = COLLECTABLE_RADIUS.getValue();
//        this.id= UUID.randomUUID().toString();
        this.anchor = anchor;
        this.direction = new Direction(direction);
        this.direction.adjustDirectionMagnitude();
        this.collectibleXP = collectibleXP;
        impactInProgress = true;
        impactMaxVel = 1.75;
        setDummyPolygon();
        collectibleModels.add(this);
        collidables.add(this);
        createCollectibleView(id);
    }

    public int getCollectibleXP() {
        return collectibleXP;
    }

    public void setCollectibleXP(int collectibleXP) {
        this.collectibleXP = collectibleXP;
    }


    private void setDummyPolygon(){
        double[] x = {0, 0, 0};
        double[] y = {0, 0, 0};
        myPolygon = new MyPolygon(x, y, 3);
    }


    public static void dropCollectible(Point2D anchor, int numOfCollectibles, int xp) {
        Point2D direction = relativeLocation(anchor, EpsilonModel.getINSTANCE().getAnchor());
        for (int i = 0; i < numOfCollectibles; i++) {
            double theta = random.nextDouble(0, 2 * PI);
            new CollectibleModel(anchor, rotateVector(direction, theta), xp);
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
    public void bulletImpact(BulletModel bulletModel, Point2D collisionPoint) {

    }

    @Override
    public Direction getDirection() {
        return this.direction;
    }

    @Override
    public void move(Direction direction) {
        Point2D movement = multiplyVector(direction.getDirectionVector(), direction.getMagnitude());
        this.anchor = addVectors(anchor, movement);

    }

    @Override
    public void move() {
        move(direction);
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



    public void eliminate(){
        super.eliminate();
        collidables.remove(this);
        movables.remove(this);
        collectibleModels.remove(this);
//        findCollectibleView((this).getId()).remove();
    }


}
