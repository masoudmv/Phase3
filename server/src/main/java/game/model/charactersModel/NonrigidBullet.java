package game.model.charactersModel;

import game.controller.Utils;
import game.example.GraphicalObject;
import game.model.FinalPanelModel;
import shared.Model.MyPolygon;
import game.model.entities.AttackTypes;
import game.model.entities.Entity;
import game.model.collision.Collidable;
import game.model.movement.Direction;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.CopyOnWriteArrayList;

import static game.controller.UserInterfaceController.createNonrigidBulletView;
import static shared.Model.imagetools.ToolBox.getBufferedImage;
import static shared.constants.Constants.FRAME_DIMENSION;

public class NonrigidBullet extends GeoShapeModel implements Collidable {


    static BufferedImage image;
    private FinalPanelModel finalPanelModel;
    protected static MyPolygon pol;

//    private double angularSpeed = 1.5;
//    double angleToEpsilon;


    public NonrigidBullet(Point2D anchor, String gameID) {
        super(anchor, image, pol, gameID);
        collidables.add(this);
        damageSize.put(AttackTypes.MELEE, 5);
        this.health = Integer.MAX_VALUE;
        createNonrigidBulletView(id, gameID);
    }

    void update(Direction direction) {
        Point2D movement = Utils.multiplyVector(direction.getNormalizedDirectionVector(), direction.getMagnitude());
        movePolygon(movement);
    }

    public void update() {
        if (dontUpdate()) return;
        if (direction == null) return;
        update(direction);
        if (isOutSide()) eliminate();
    }



    public void rapidFire(int numBullets, double arcAngle) {

        double startAngle = 0;
        double angleStep = arcAngle / (numBullets - 1);

        for (int i = 0; i < numBullets; i++) {
            double angle = startAngle + i * angleStep;
            double radians = Math.toRadians(angle);
            Point2D direction = new Point2D.Double(Math.cos(radians), Math.sin(radians));


            Point2D firingPoint = new Point2D.Double(130, 130); //todo edit

            NonrigidBullet b = new NonrigidBullet(firingPoint, gameID);

            direction = Utils.adjustVectorMagnitude(direction, 5);
            b.setDirection(new Direction(direction));
        }
    }

    public static BufferedImage loadImage() {
        Image img = new ImageIcon("./client/src/bullet.png").getImage();
//        SmileyBullet.image = getBufferedImage(img);

        NonrigidBullet.image = getBufferedImage(img);

        GraphicalObject bowser = new GraphicalObject(image);
        pol = bowser.getMyBoundingPolygon();


        return NonrigidBullet.image;
    }

    private boolean isOutSide(){
        boolean top = getAnchor().getY() + getRadius() < 0;
        boolean bottom = getAnchor().getY() - getRadius() > FRAME_DIMENSION.getHeight();
        boolean right = getAnchor().getX() + getRadius() < 0;
        boolean left = getAnchor().getX() - getRadius() > FRAME_DIMENSION.getWidth();


        if (top || bottom || right || left) return true;
        return false;
    }


    @Override
    public void setMyPolygon(MyPolygon myPolygon) {
        super.myPolygon = myPolygon;
    }

    @Override
    public void eliminate() {
        super.eliminate();
        collidables.remove(this);

    }

    @Override
    public boolean isCircular() {
        return true;
    }

    @Override
    public void onCollision(Collidable other, Point2D intersection) {
        if (other instanceof FinalPanelModel) return;
        if (other instanceof EpsilonModel) {
            this.damage((Entity) other, AttackTypes.MELEE);
            eliminate();
        }

    }

    @Override
    public void onCollision(Collidable other, Point2D coll1, Point2D coll2) {

    }
}
