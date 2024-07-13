package model.charactersModel.smiley;

import controller.Utils;
import model.FinalPanelModel;
import model.MyPolygon;
import model.charactersModel.GeoShapeModel;
import model.movement.Direction;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.CopyOnWriteArrayList;
import static controller.Utils.adjustVectorMagnitude;
import static controller.Utils.multiplyVector;
import static model.imagetools.ToolBox.getBufferedImage;

public class SmileyBullet extends GeoShapeModel {


    static BufferedImage image;
    public static CopyOnWriteArrayList<SmileyBullet> smileyBullets = new CopyOnWriteArrayList<>();
    private FinalPanelModel finalPanelModel;
//    private double angularSpeed = 1.5;
//    double angleToEpsilon;


    public SmileyBullet(Point2D anchor, MyPolygon myPolygon) {
        super(anchor, image, myPolygon);
        smileyBullets.add(this);







    }

    void move(Direction direction) {
        Point2D movement = multiplyVector(direction.getNormalizedDirectionVector(), direction.getMagnitude());
        movePolygon(movement);
    }

    public void move() {
        if (direction == null) return;
        move(direction);
    }



    public void rapidFire(int numBullets, double arcAngle) {

        double startAngle = 0;
        double angleStep = arcAngle / (numBullets - 1);

        for (int i = 0; i < numBullets; i++) {
            double angle = startAngle + i * angleStep;
            double radians = Math.toRadians(angle);
            Point2D direction = new Point2D.Double(Math.cos(radians), Math.sin(radians));


            Point2D firingPoint = new Point2D.Double(130, 130); //todo edit

            SmileyBullet b = new SmileyBullet(firingPoint, myPolygon);

            direction = adjustVectorMagnitude(direction, 5);
            b.setDirection(new Direction(direction));
            smileyBullets.add(b);
        }
    }

    public static BufferedImage loadImage() {
        Image img = new ImageIcon("./src/bullet.png").getImage();
        SmileyBullet.image = getBufferedImage(img);
        return SmileyBullet.image;
    }


    @Override
    public void setMyPolygon(MyPolygon myPolygon) {
        super.myPolygon = myPolygon;
    }

    @Override
    public void eliminate() {

    }
}
