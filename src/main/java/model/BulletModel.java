package model;
//import controller.SoundHandler;
import model.collision.Collidable;
import model.collision.CollisionState;
import model.collision.Impactable;
import model.movement.Direction;
import model.movement.Movable;

import javax.sound.sampled.AudioSystem;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.LinkedList;
import java.util.UUID;

import static controller.Constants.BULLET_RADIUS;
import static controller.Constants.BULLET_VELOCITY;
import static controller.Controller.creatBulletView;
import static controller.Controller.findBulletView;
import static controller.Game.clip;
import static controller.Utils.*;

public class BulletModel implements Movable, Collidable, Impactable {
    String id;
    double radius;
    private Point2D anchor;
    Direction direction;
    public static LinkedList<BulletModel> bulletModels = new LinkedList<>();

    public BulletModel(Point2D anchor, Direction direction) {
        this.radius = BULLET_RADIUS;
        this.id= UUID.randomUUID().toString();
        this.anchor = anchor;
        this.direction = direction;
        bulletModels.add(this);
        movables.add(this);
        collidables.add(this);
        creatBulletView(id);
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean isCircular() {
        return true;
    }

    @Override
    public double getRadius() {
        return this.radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public void setDirection(Direction direction) {}

    @Override
    public void bulletImpact(BulletModel bulletModel, Point2D collisionPoint) {
        this.remove();
    }



    public void bulletImpact(BulletModel bulletModel, Point2D collisionPoint, Collidable collidable) {

        ((Movable) collidable).bulletImpact(bulletModel, collisionPoint);
//        SoundHandler.playClip();

//        try
//        {
//            clip = AudioSystem.getClip();
//            clip.open(AudioSystem.getAudioInputStream(new File("C:\\Users\\masoo\\Desktop\\Projects\\windowkill_AP\\src\\main\\resources\\burst2.wav")));
//            clip.start();
//        }
//        catch (Exception exc)
//        {
//            exc.printStackTrace(System.out);
//       }
//        new Thread(() -> {
//            if (clip != null) {
//                clip.stop();          // Stop the clip before rewinding it
//                clip.setFramePosition(0);  // Rewind to the beginning
//                clip.start();         // Start playing
//            }
//        }).start();



        for (Movable movable : movables){
            if (movable != this && movable != bulletModel && movable!= collidable){
                ((Impactable)movable).impact(new CollisionState(collisionPoint));
            }
        } this.remove();
    }


    @Override
    public Direction getDirection() {
        return this.direction;
    }

    @Override
    public void move(Direction direction) {
        Point2D movement = multiplyVector(direction.getNormalizedDirectionVector(), BULLET_VELOCITY);
        this.anchor = addVectors(anchor, movement);
    }

    @Override
    public void move() {
        move(direction);
    }

    @Override
    public void friction() {}

    @Override
    public Point2D getAnchor() {
        return this.anchor;
    }




    @Override
    public Point2D[] getVertices() {
        return null;
    }

    @Override
    public boolean isImpactInProgress() {
        return false;
    }

    @Override
    public void setImpactInProgress(boolean impactInProgress) {

    }

    @Override
    public void impact(CollisionState collisionState) {}

    @Override
    public void impact(Point2D normalVector, Point2D collisionPoint, Collidable polygon) {}


    @Override
    public double getImpactCoefficient(Point2D collisionRelativeVector) {
        return 0;
    }

    @Override
    public void banish() {}

    public void remove(){
        collidables.remove(this);
        bulletModels.remove(this);
        movables.remove(this);
        findBulletView((this).getId()).remove();
    }
}
