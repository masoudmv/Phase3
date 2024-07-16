package model.charactersModel;

import javafx.scene.shape.Arc;
import model.MyPolygon;
import org.example.GraphicalObject;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import static model.imagetools.ToolBox.getBufferedImage;

public class BabyArchmire extends ArchmireModel {

    static BufferedImage image;

    public BabyArchmire(Point2D anchor) {
        super(anchor, pol);
    }

    public static BufferedImage loadImage() {
        Image img = new ImageIcon("./src/babyArchmire.png").getImage();
        BabyArchmire.image = getBufferedImage(img);

        GraphicalObject bowser = new GraphicalObject(image);
        pol = bowser.getMyBoundingPolygon();


        return BabyArchmire.image;
    }
}
