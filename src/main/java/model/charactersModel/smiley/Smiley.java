package model.charactersModel.smiley;

import controller.Game;
import controller.Utils;
import model.FinalPanelModel;
import model.MyPolygon;
import model.charactersModel.ArchmireModel;
import model.charactersModel.EpsilonModel;
import model.charactersModel.GeoShapeModel;
//import model.collision.Coll;
import model.charactersModel.SmileyBullet;
import model.collision.Collidable;
import model.movement.Direction;
import org.example.GraphicalObject;
import view.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static controller.Utils.*;
import static model.charactersModel.SmileyBullet.smileyBullets;
import static model.imagetools.ToolBox.getBufferedImage;

public class Smiley extends GeoShapeModel implements Collidable {


    static BufferedImage image;
    public static ArrayList<Smiley> smilies = new ArrayList<>();
    private FinalPanelModel finalPanelModel;
    protected static MyPolygon pol;
    private double last = 0;

//    private double angularSpeed = 1.5;
//    double angleToEpsilon;


    public Smiley(Point2D anchor) {
        super(anchor, image, pol);
//        hands.add(this);


//        angleToEpsilon = findAngleToEpsilon();
//        rotate(90);


        Point2D loc = new Point2D.Double(getAnchor().getX() - 110, getAnchor().getY() - 110);
        Dimension size = new Dimension(1000, 1000);
        finalPanelModel = new FinalPanelModel(loc, size);

        collidables.add(this);
        smilies.add(this);

    }



//    private double findAngleToEpsilon(){
////        Point2D right = new Point2D.Double(1, 0);
////        Point2D left = new Point2D.Double(-1, 0);
////        Point2D center = EpsilonModel.getINSTANCE().getAnchor();
////        Point2D vecToEpsilon = relativeLocation(getAnchor(), center);
////        double res = 0;
////        if (vecToEpsilon.getY() < 0) res = findAngleBetweenTwoVectors(right, vecToEpsilon);
////        if (vecToEpsilon.getY() == 0) res = Math.PI;
////        if (vecToEpsilon.getY() > 0) res = findAngleBetweenTwoVectors(left, vecToEpsilon) + Math.PI;
////        return Math.toDegrees(res);
//    }



    public void move() {

        double now = Game.ELAPSED_TIME;
        if (now - last > 5) {
            last = now;
            rapidFire(10, 180);
        }





        // Update the angle for circular motion
//        angleToEpsilon += angularSpeed;
//        if (angleToEpsilon >= 360) angleToEpsilon -= 360; // Keep angle within 0-359 degrees
//        Point2D center = EpsilonModel.getINSTANCE().getAnchor();
//
//        // Calculate the current radius
//        double radius = findDistance(getAnchor(), center);
//
//        // Calculate new position based on the updated angle
//        double radians = Math.toRadians(angleToEpsilon);
//        double newX = center.getX() + radius * Math.cos(radians);
//        double newY = center.getY() + radius * Math.sin(radians);
//
//        // Set the new position
//        Point2D newAnchor = new Point2D.Double(newX, newY);
//
//        // Update the polygon's position
//        Point2D movement = new Point2D.Double(newX - getAnchor().getX(), newY - getAnchor().getY());
//        finalPanelModel.moveLocation(movement);
//
//        setAnchor(newAnchor);
//
//        // Rotate the polygon to face the center
//        angle = getAngleTowardsCenter(center, getAnchor()) + 270;
//        rotate(angularSpeed);

    }


    public void rapidFire(int numBullets, double arcAngle) {

        double startAngle = 0;
        double angleStep = arcAngle / (numBullets - 1);

        for (int i = 0; i < numBullets; i++) {
            double angle = startAngle + i * angleStep;
            double radians = Math.toRadians(angle);
            Point2D direction = new Point2D.Double(Math.cos(radians), Math.sin(radians));


            Point2D firingPoint = new Point2D.Double(anchor.getX(), anchor.getY()); //todo edit

            SmileyBullet b = new SmileyBullet(firingPoint);

            direction = adjustVectorMagnitude(direction, 5);
            b.setDirection(new Direction(direction));
        }
    }






    // Method to calculate the angle so that the same face of the object points towards the center
    public static double getAngleTowardsCenter(Point2D center, Point2D objectPosition) {
        // Calculate the vector from center to object
        double dx = objectPosition.getX() - center.getX();
        double dy = objectPosition.getY() - center.getY();

        // Calculate the angle (in radians) using atan2
        double angle = Math.atan2(dy, dx);

        return Math.toDegrees(angle);
    }



    public void rotate(double angle){
        setMyPolygon(Utils.rotateMyPolygon(myPolygon, angle, getAnchor()));
    }


    public static BufferedImage loadImage() {
        Image img = new ImageIcon("./src/smiley.png").getImage();
//        Smiley.image = getBufferedImage(img);

        Smiley.image = getBufferedImage(img);

        GraphicalObject bowser = new GraphicalObject(image);
        pol = bowser.getMyBoundingPolygon();




        return Smiley.image;
    }


    @Override
    public void setMyPolygon(MyPolygon myPolygon) {
        super.myPolygon = myPolygon;
    }

    @Override
    public boolean isCircular() {
        return true;
    }

    @Override
    public Point2D[] getVertices() {
        return null;
    }

    @Override
    public ArrayList<Line2D> getEdges() {
        return null;
    }


    @Override
    public double getRadius(){
        return image.getHeight()/2;
    };

    @Override
    public void onCollision(Collidable other, Point2D intersection) {
        if (other instanceof EpsilonModel);

    }

    @Override
    public void onCollision(Collidable other) {

    }

    @Override
    public void eliminate() {

    }
}
