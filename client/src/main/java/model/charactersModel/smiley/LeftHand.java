package model.charactersModel.smiley;

import model.MyPolygon;
import model.charactersModel.BabyArchmire;
import org.example.GraphicalObject;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import static model.imagetools.ToolBox.getBufferedImage;




public class LeftHand extends Hand{




    static BufferedImage image;
    static MyPolygon polygon;

    public LeftHand(Point2D anchor) {
        super(anchor, polygon);
    }


    public static BufferedImage loadImage() {
        Image img = new ImageIcon("./src/hand2.png").getImage();
        LeftHand.image = getBufferedImage(img);
        GraphicalObject bowser = new GraphicalObject(image);
        bowser.refine();

        polygon = bowser.getMyBoundingPolygon();


        return LeftHand.image;
    }

    @Override
    protected boolean isRightHand(){
        return false;
    }
}
