package game.model.charactersModel;

import shared.constants.EntityConstants;
import game.example.GraphicalObject;
import shared.Model.MyPolygon;
import game.model.collision.Collidable;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import static shared.Model.imagetools.ToolBox.getBufferedImage;

public class BabyArchmire extends ArchmireModel {

    static BufferedImage image;
    protected static MyPolygon poly;

    public BabyArchmire(Point2D anchor, String gameID) {
        super(anchor, poly, gameID);
        this.health = EntityConstants.BABY_ARCHMIRE_HEALTH.getValue();

    }



    @Override
    public void eliminate(){
        super.eli();
        collidables.remove(this);

        CollectibleModel.dropCollectible(
                getAnchor(), EntityConstants.BABY_ARCHMIRE_NUM_OF_COLLECTIBLES.getValue(), EntityConstants.BABY_ARCHMIRE_COLLECTIBLES_XP.getValue(), gameID
        );
    }

    public BabyArchmire() {
    }

    public static BufferedImage loadImage() {
        Image img = new ImageIcon("./client/src/babyArchmire.png").getImage();
        BabyArchmire.image = getBufferedImage(img);

        GraphicalObject bowser = new GraphicalObject(image);
        poly = bowser.getMyBoundingPolygon();


        return BabyArchmire.image;
    }

    @Override
    public void onCollision(Collidable other, Point2D intersection) {
//        if (other instanceof BulletModel) eliminate();
    }

}
