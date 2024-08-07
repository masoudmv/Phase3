package model.charactersModel.smiley;

import controller.Game;
import controller.Utils;
import model.FinalPanelModel;
import model.MyPolygon;
import model.charactersModel.BulletModel;
import model.charactersModel.EpsilonModel;
import model.charactersModel.GeoShapeModel;
//import model.collision.Coll;
import model.charactersModel.SmileyBullet;
import model.collision.Collidable;
import model.entities.AttackTypes;
import model.movement.Direction;
import org.example.GraphicalObject;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import static controller.Utils.*;
import static controller.constants.Constants.FRAME_DIMENSION;
import static controller.constants.EntityConstants.*;
import static controller.constants.EntityConstants.SMILEY_SLAP_COOLDOWN;
import static model.imagetools.ToolBox.getBufferedImage;

public class Smiley extends GeoShapeModel implements Collidable {


    static BufferedImage image;
    public static ArrayList<Smiley> smilies = new ArrayList<>();
    private FinalPanelModel finalPanelModel;
    protected static MyPolygon pol;
    private double lastRapidFire = 0;
    private double lastVomit = 0;


    private Hand leftHand;
    private Hand rightHand;
    private Fist fist;



    public Smiley(Point2D anchor, Hand leftHand, Hand rightHand) {
        super(anchor, image, new MyPolygon());

        Point2D loc = new Point2D.Double(getAnchor().getX() - 150, getAnchor().getY() - 150);
        Dimension size = new Dimension(300, 300);
        finalPanelModel = new FinalPanelModel(loc, size);
        finalPanelModel.setIsometric(true);

        collidables.add(this);
        smilies.add(this);

        this.rightHand = rightHand;
        this.leftHand = leftHand;
        this.health = 300;
        this.vulnerable = true;

    }

    private void checkForProjectileCoolDown(){
        double now = Game.ELAPSED_TIME;

        if (!leftHand.isAlive() && !rightHand.isAlive()) return;

        if (!rightHand.projectileInProgress &&
                now - rightHand.lastProjectileTime > SMILEY_PROJECTILE_DURATION.getValue() + SMILEY_PROJECTILE_COOLDOWN.getValue()) {
            if (Hand.slapInProgress) return;
            if (leftHand.projectileInProgress) return;
            if (leftHand.squeezeInProgress) return;
            if (rightHand.squeezeInProgress) return;



            EpsilonModel epsilon = EpsilonModel.getINSTANCE();
            Point2D p = epsilon.getAnchor();
            double x = p.getX();
            double y = p.getY();
            double midX = FRAME_DIMENSION.getWidth() / 2;
            double midY = FRAME_DIMENSION.getHeight() / 2;
            double diffX = Math.abs(x - midX);
            double diffY = y - midY;
            if (diffY < 0 || diffY > 200 ||diffX > 400) return;

            rightHand.initializeProjectile();
            leftHand.initializeProjectile();
        }
    }


    private void checkForSqueezeCoolDown(){
        double now = Game.ELAPSED_TIME;
        if (!rightHand.squeezeInProgress &&
                now - rightHand.lastSqueezeTime > SMILEY_SQUEEZE_DURATION.getValue() + SMILEY_SQUEEZE_COOLDOWN.getValue()) {
            if (Hand.slapInProgress) return;

            if (leftHand.squeezeInProgress) return;
            if (leftHand.projectileInProgress) return;
            if (rightHand.projectileInProgress) return;


            initiateSqueeze();
        }
    }


    private void updateVulnerability(){
        double now = Game.ELAPSED_TIME;
        boolean isVomit = now - lastVomit > SMILEY_AOE_ACTIVATED_LIFETIME.getValue();
        boolean isRapidFire = now - lastRapidFire > 5;
        boolean isSqueeze = rightHand.squeezeInProgress && leftHand.squeezeInProgress;
        boolean isSlap = Hand.slapInProgress;
        if (!isSlap && !isSqueeze && !isVomit && !isRapidFire) vulnerable = false;
    }




    private void vomit(){
        SmileyAOE.createVomitAOEs();
    }


    private void initiateSqueeze(){
//        lastSqueezeTime = Game.ELAPSED_TIME;
        if (!leftHand.isAlive() || !rightHand.isAlive()) return;
//        if (leftHand.projectileInProgress || rightHand.projectileInProgress) return;
        FinalPanelModel leftPanel = leftHand.getFinalPanelModel();
        FinalPanelModel rightPanel = rightHand.getFinalPanelModel();
        FinalPanelModel epsilonPanel = EpsilonModel.getINSTANCE().getLocalPanel();
        if (epsilonPanel == null) return;

        boolean left = leftPanel.getLocation().getX() + leftPanel.getSize().getWidth() < epsilonPanel.getLocation().getX();
        boolean right = rightPanel.getLocation().getX() > epsilonPanel.getLocation().getX() + epsilonPanel.getSize().getWidth();
        boolean top  = epsilonPanel.getLocation().getY() > finalPanelModel.getLocation().getY() + finalPanelModel.getSize().getHeight();

        if (rightHand.beforeSlapPosition != null) return;
        if (leftHand.beforeSlapPosition != null) return;


        if (right && left && top) {
            leftHand.initializeSqueeze();
            rightHand.initializeSqueeze();
        }
    }


    @Override
    public void update() {
        checkForProjectileCoolDown();
        checkForSqueezeCoolDown();


        double now = Game.ELAPSED_TIME;


        if (now - lastRapidFire > 20) {
            lastRapidFire = now;
            rapidFire(7, 90);
        }


        SmileyAOE.updateAll();
        if (now - lastVomit > 25) {
            lastVomit = now;
            vomit();
        }



        updateVulnerability();


    }


    public void rapidFire(int numBullets, double arcAngle) {

        new Thread(new Runnable() {

            @Override
            public void run() {
                int index = 0;
                while (index < 6){
                    EpsilonModel epsilon = EpsilonModel.getINSTANCE();
                    Point2D des = epsilon.getAnchor();
                    Point2D start = anchor;

                    // Calculate the relative position from start to des
                    Point2D rel = relativeLocation(des, start);
                    double t = rel.getY() / rel.getX();

                    // Calculate the starting angle of the arc
                    double startAngle = Math.toDegrees(Math.atan2(rel.getY(), rel.getX())) - arcAngle / 2;

                    // Calculate the angle step between each bullet
                    double angleStep = arcAngle / (numBullets - 1);

                    // Loop through each bullet to set its direction and fire it
                    for (int i = 0; i < numBullets; i++) {
                        // Calculate the angle for the current bullet
                        double angle = startAngle + i * angleStep;
                        double radians = Math.toRadians(angle);

                        // Calculate the direction vector based on the angle
                        Point2D direction = new Point2D.Double(Math.cos(radians), Math.sin(radians));

                        // Set the firing point (anchor point in this case)
                        Point2D firingPoint = new Point2D.Double(anchor.getX(), anchor.getY());

                        // Create a new bullet at the firing point
                        SmileyBullet b = new SmileyBullet(firingPoint);

                        // Adjust the direction vector's magnitude and set it on the bullet
                        direction = adjustVectorMagnitude(direction, 5);
                        b.setDirection(new Direction(direction));
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    index ++;
                }
            }
        }).start();
    }

    private Point2D relativeLocation(Point2D des, Point2D start) {
        return new Point2D.Double(des.getX() - start.getX(), des.getY() - start.getY());
    }

    private Point2D adjustVectorMagnitude(Point2D vector, double magnitude) {
        double currentMagnitude = Math.sqrt(vector.getX() * vector.getX() + vector.getY() * vector.getY());
        double scale = magnitude / currentMagnitude;
        return new Point2D.Double(vector.getX() * scale, vector.getY() * scale);
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
        Image img = new ImageIcon("C:\\Users\\masoud\\Desktop\\New folder\\NetworkClass\\client\\src\\smiley.png").getImage();
//        Smiley.image = getBufferedImage(img);

        Smiley.image = getBufferedImage(img);

//        GraphicalObject bowser = new GraphicalObject(image);
//        pol = bowser.getMyBoundingPolygon();




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
//        if (other instanceof EpsilonModel);

        if (other instanceof BulletModel) {
            ((BulletModel) other).damage(this, AttackTypes.MELEE);
            eliminate();
        }




    }

    @Override
    public void onCollision(Collidable other, Point2D coll1, Point2D coll2) {

    }


    @Override
    public void eliminate() {
        super.eliminate();
        collidables.remove(this);
        finalPanelModel.eliminate();

        leftHand.eliminate();
        rightHand.eliminate();
    }
}
