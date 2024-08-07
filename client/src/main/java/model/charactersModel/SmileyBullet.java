package model.charactersModel;

import model.FinalPanelModel;
import model.MyPolygon;
import model.charactersModel.smiley.Smiley;
import model.collision.Collidable;
import model.entities.AttackTypes;
import model.entities.Entity;
import model.movement.Direction;
import org.example.GraphicalObject;
import view.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.CopyOnWriteArrayList;

import static controller.Utils.adjustVectorMagnitude;
import static controller.Utils.multiplyVector;
import static model.imagetools.ToolBox.getBufferedImage;

public class SmileyBullet extends GeoShapeModel implements Collidable {


    static BufferedImage image;
    public static CopyOnWriteArrayList<SmileyBullet> smileyBullets = new CopyOnWriteArrayList<>();
    private FinalPanelModel finalPanelModel;
    protected static MyPolygon pol;

//    private double angularSpeed = 1.5;
//    double angleToEpsilon;


    public SmileyBullet(Point2D anchor) {
        super(anchor, image, pol);
        smileyBullets.add(this);
        collidables.add(this);
        damageSize.put(AttackTypes.MELEE, 5);
        this.health = Integer.MAX_VALUE;
    }

    void update(Direction direction) {
        Point2D movement = multiplyVector(direction.getNormalizedDirectionVector(), direction.getMagnitude());
        movePolygon(movement);
    }

    public void update() {
        if (dontUpdate()) return;
        if (direction == null) return;
        update(direction);
        if (isOutSide()) eliminate();
    }




    public static BufferedImage loadImage() {
        Image img = new ImageIcon("./client/src/bullet.png").getImage();
//        SmileyBullet.image = getBufferedImage(img);

        SmileyBullet.image = getBufferedImage(img);

        GraphicalObject bowser = new GraphicalObject(image);
        pol = bowser.getMyBoundingPolygon();


        return SmileyBullet.image;
    }

    private boolean isOutSide(){
        boolean top = getAnchor().getY() + getRadius() < 0;
        boolean bottom = getAnchor().getY() - getRadius() > MainFrame.getINSTANCE().getHeight();
        boolean right = getAnchor().getX() + getRadius() < 0;
        boolean left = getAnchor().getX() - getRadius() > MainFrame.getINSTANCE().getWidth();


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
        smileyBullets.remove(this);

    }

    @Override
    public boolean isCircular() {
        return true;
    }

    @Override
    public void onCollision(Collidable other, Point2D intersection) {
        if (other instanceof FinalPanelModel || other instanceof Smiley) return;
        if (other instanceof EpsilonModel) {
            this.damage((Entity) other, AttackTypes.MELEE);
            eliminate();
            return;
        }

//        eliminate();

    }

    @Override
    public void onCollision(Collidable other, Point2D coll1, Point2D coll2) {

    }
}
