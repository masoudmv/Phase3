package model.charactersModel;

import javafx.scene.shape.Arc;
import model.MyPolygon;
import org.example.GraphicalObject;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import static controller.constants.EntityConstants.*;
import static model.imagetools.ToolBox.getBufferedImage;

public class BabyArchmire extends ArchmireModel {

    static BufferedImage image;

    public BabyArchmire(Point2D anchor) {
        super(anchor, pol);
    }



    @Override
    public void eliminate(){
        super.eliminate();
        collidables.remove(this);
        archmireModels.remove(this);

        CollectibleModel.dropCollectible(
                getAnchor(), BABY_ARCHMIRE_NUM_OF_COLLECTIBLES.getValue(), BABY_ARCHMIRE_COLLECTIBLES_XP.getValue()
        );
    }


    public static BufferedImage loadImage() {
        Image img = new ImageIcon("./src/babyArchmire.png").getImage();
        BabyArchmire.image = getBufferedImage(img);

        GraphicalObject bowser = new GraphicalObject(image);
        pol = bowser.getMyBoundingPolygon();


        return BabyArchmire.image;
    }
}
