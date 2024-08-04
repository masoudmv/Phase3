package game.model.charactersModel;

import game.controller.Game;
import game.controller.UserInterfaceController;
import shared.constants.EntityConstants;
import game.example.GraphicalObject;
import shared.Model.MyPolygon;
//import view.MainPanel;
import game.model.collision.Collidable;
import game.model.movement.Direction;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import static shared.Model.imagetools.ToolBox.getBufferedImage;

public class NecropickModel extends GeoShapeModel implements Collidable {
    static BufferedImage image;
    protected static MyPolygon pol;
    public static ArrayList<NecropickModel> necropickModels = new ArrayList<>();
//    public Polygon polygon;
    private boolean isHovering; // equals isUnderGround!
    private double stateChangeTime = 0; // the last Time necropick changed its state
    private Point2D nextAnchor; // precomputed next location
    private boolean isNextLocationCalculated = false; // flag to check if next location is calculated
    private static final Random random = new Random();

    public NecropickModel() {
        super(new Point2D.Double(-100, -100), image, pol, true);
        necropickModels.add(this);
        stateChangeTime = Game.ELAPSED_TIME; // Initialize state change time
        isHovering = true; // Start in hovering state
        collidables.add(this);
//        UserInterfaceController.createNecropickView(id, image);
        this.health = EntityConstants.NECROPICK_HEALTH.getValue();
    }

    public static BufferedImage loadImage() {
        Image img = new ImageIcon("./client/src/necropick.png").getImage();
        NecropickModel.image = getBufferedImage(img);

        GraphicalObject bowser = new GraphicalObject(image);
        pol = bowser.getMyBoundingPolygon();

        return NecropickModel.image;
    }

    public boolean isHovering() {
        return isHovering;
    }

    private void hideUnderGround(){
        isNextLocationCalculated = false;
        setAnchor(new Point2D.Double(-100, -100)); //:))))
        isHovering = true;
    }

    private void returnToGroundSurface(){
        if (checkIntersectionExistence()) return;
        shootBullets();
        isHovering = false;
    }



    public static Point2D getRandomPoint(Point2D pivot, double minRadius, double maxRadius) {
        double angle = 2 * Math.PI * random.nextDouble(); // Random angle between 0 and 2π
        double radius = minRadius + (maxRadius - minRadius) * random.nextDouble(); // Random radius between minRadius and maxRadius

        // Convert polar coordinates to Cartesian coordinates
        double x = pivot.getX() + radius * Math.cos(angle);
        double y = pivot.getY() + radius * Math.sin(angle);

        return new Point2D.Double(x, y);
    }

    public void update() {
        if (dontUpdate()) return;
        double elapsedTime = Game.ELAPSED_TIME;

        if (!isHovering && (elapsedTime - stateChangeTime) >= EntityConstants.HOVER_DURATION) {
            hideUnderGround();
            stateChangeTime = elapsedTime;
        }

        if (isHovering && (elapsedTime - stateChangeTime) >= (double) EntityConstants.NON_HOVER_DURATION /2 && !isNextLocationCalculated) {
            updateView();
        }

        if (isHovering && (elapsedTime - stateChangeTime) >= EntityConstants.NON_HOVER_DURATION) {
            returnToGroundSurface();
            stateChangeTime = elapsedTime;
            updateView();
        }
    }



    @Override
    public void setMyPolygon(MyPolygon myPolygon) {

    }

    @Override
    public void eliminate() {
        super.eliminate();
        collidables.remove(this);
        necropickModels.remove(this);

        CollectibleModel.dropCollectible(
                getAnchor(), EntityConstants.NECROPICK_NUM_OF_COLLECTIBLES.getValue(), EntityConstants.NECROPICK_COLLECTIBLES_XP.getValue()
        );
    }



    private void updateView() {
        if (isHovering) {
            boolean doesIntersect = findNextPos();
            while (doesIntersect) {
                doesIntersect = findNextPos();
            }
        }

//        UserInterfaceController.updateNecropick(id);
    }

    private boolean findNextPos() {
        isNextLocationCalculated = true;
        Point2D dest = EpsilonModel.getINSTANCE().getAnchor();
        nextAnchor = getRandomPoint(dest, EntityConstants.NECROPICK_MIN_RADIUS, EntityConstants.NECROPICK_MAX_RADIUS);
        setAnchor(nextAnchor);

        return checkIntersectionExistence();
    }


    private void shootBullets(){
        new BulletModel(getAnchor(), new Direction(new Point2D.Double(0, -1)), false);
        new BulletModel(getAnchor(), new Direction(new Point2D.Double(0, +1)), false);
        new BulletModel(getAnchor(), new Direction(new Point2D.Double(+1, 0)), false);
        new BulletModel(getAnchor(), new Direction(new Point2D.Double(-1, 0)), false);
        new BulletModel(getAnchor(), new Direction(new Point2D.Double(+1, -1)), false);
        new BulletModel(getAnchor(), new Direction(new Point2D.Double(-1, -1)), false);
        new BulletModel(getAnchor(), new Direction(new Point2D.Double(+1, +1)), false);
        new BulletModel(getAnchor(), new Direction(new Point2D.Double(-1, +1)), false);

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
