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
    private double squeezeStartTime = 0;


    private Hand leftHand;
    private Hand rightHand;


    public Smiley(Point2D anchor, Hand leftHand, Hand rightHand) {
        super(anchor, image, pol);
        this.leftHand = leftHand;
        Point2D loc = new Point2D.Double(getAnchor().getX() - 150, getAnchor().getY() - 150);
        Dimension size = new Dimension(300, 300);
        finalPanelModel = new FinalPanelModel(loc, size);

        collidables.add(this);
        smilies.add(this);

        this.rightHand = rightHand;
        initiateSqueeze();
    }


    public Smiley(Point2D anchor) {
        super(anchor, image, pol);
        Point2D loc = new Point2D.Double(getAnchor().getX() - 150, getAnchor().getY() - 150);
        Dimension size = new Dimension(300, 300);
        finalPanelModel = new FinalPanelModel(loc, size);

        collidables.add(this);
        smilies.add(this);

    }





    private void initiateSqueeze(){
        if (!leftHand.isAlive() || !rightHand.isAlive()) return;
        FinalPanelModel leftPanel = leftHand.getFinalPanelModel();
        FinalPanelModel rightPanel = rightHand.getFinalPanelModel();
        FinalPanelModel epsilonPanel = EpsilonModel.getINSTANCE().localPanel;

        boolean left = leftPanel.getLocation().getX() + leftPanel.getSize().getWidth() < epsilonPanel.getLocation().getX();
        boolean right = rightPanel.getLocation().getX() > epsilonPanel.getLocation().getX() + epsilonPanel.getSize().getWidth();
        boolean top  = epsilonPanel.getLocation().getY() > finalPanelModel.getLocation().getY() + finalPanelModel.getSize().getHeight();
        System.out.println(left);
        System.out.println(right);
        System.out.println(top);


        if (right && left && top) {
            leftHand.squeeze();
            rightHand.squeeze();
        }
    }





    public void move() {

        double now = Game.ELAPSED_TIME;
        if (now - last > 5) {
            last = now;
            rapidFire(10, 180);
        }


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
