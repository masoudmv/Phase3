package model.charactersModel;

import model.MyPolygon;
import model.collision.Collidable;
import org.example.GraphicalObject;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import static controller.constants.EntityConstants.*;
import static model.imagetools.ToolBox.getBufferedImage;

public class BabyArchmire extends ArchmireModel {

    static BufferedImage image;
    protected static MyPolygon poly;

    public BabyArchmire(Point2D anchor) {
        super(anchor, poly);
        this.health = BABY_ARCHMIRE_HEALTH.getValue();

    }

    public BabyArchmire(){

    }



    @Override
    public void eliminate(){
        super.eli();
        collidables.remove(this);
        archmireModels.remove(this);

        CollectibleModel.dropCollectible(
                getAnchor(), BABY_ARCHMIRE_NUM_OF_COLLECTIBLES.getValue(), BABY_ARCHMIRE_COLLECTIBLES_XP.getValue()
        );
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
