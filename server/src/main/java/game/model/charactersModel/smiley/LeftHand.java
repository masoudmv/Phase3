package game.model.charactersModel.smiley;

import game.example.GraphicalObject;
import shared.Model.MyPolygon;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import static shared.Model.imagetools.ToolBox.getBufferedImage;




public class LeftHand extends Hand{

    static BufferedImage image;
    static MyPolygon polygon;

    public LeftHand(Point2D anchor, String gameID) {
        super(anchor, polygon, gameID);
    }


    public static BufferedImage loadImage() {
        Image img = new ImageIcon("./client/src/hand2.png").getImage();
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
