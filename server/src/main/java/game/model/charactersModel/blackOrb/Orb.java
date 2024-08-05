package game.model.charactersModel.blackOrb;

import game.controller.Game;
import shared.constants.EntityConstants;
import game.model.FinalPanelModel;
import shared.Model.MyPolygon;
import javafx.scene.shape.Circle;
import game.model.charactersModel.CollectibleModel;
import game.model.charactersModel.EpsilonModel;
import game.model.charactersModel.GeoShapeModel;
import game.model.collision.Collidable;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;


import static game.model.charactersModel.blackOrb.BlackOrb.lasers;
import static shared.Model.imagetools.ToolBox.getBufferedImage;

public class Orb extends GeoShapeModel implements Collidable {
    static BufferedImage image; // transient to avoid serialization
    private final Circle circle;
    private FinalPanelModel panel;

    public Orb(Point2D anchor, String gameID) {
        super(anchor, image);
        this.circle = new Circle(anchor.getX(), anchor.getY(), (double) image.getHeight() / 2);
        collidables.add(this);
        this.health = EntityConstants.ORB_HEALTH.getValue();
        this.gameID = gameID;


        /**
         checking for intersection with epsilon
         if it touches the orb in the creation process, YOU DIE!
         */


        Game game = findGame(gameID);

        for (EpsilonModel epsilon : game.epsilons){
            for (GeoShapeModel model : findGame(gameID).entities){
                if (model.intersects(this) && !model.equals(this)) model.eliminate();

            }

            if (epsilon.intersects(this)) System.out.println("YOU ARE DEAD!");
            // todo show death panel ...
        }

    }

    public static BufferedImage loadImage() {
        Image img = new ImageIcon("./client/src/orb.png").getImage();
        Orb.image = getBufferedImage(img);
        return Orb.image;
    }


    public Circle getCircle() {
        return circle;
    }

    @Override
    public boolean isCircular() {
        return true;
    }

    @Override
    public double getRadius() {
        return radius;
    }

    @Override
    public Point2D getAnchor() {
        return super.getAnchor();
    }

    @Override
    public void setMyPolygon(MyPolygon myPolygon) {}

    @Override
    public Point2D[] getVertices() {
        return null;
    }

    @Override
    public ArrayList<Line2D> getEdges() {
        return null;
    }

    @Override
    public void onCollision(Collidable other, Point2D intersection) {
//        if (other instanceof BulletModel) {
//            this.damage((Entity) other, AttackTypes.MELEE);
//        }
    }

    @Override
    public void onCollision(Collidable other, Point2D coll1, Point2D coll2) {}

    public FinalPanelModel getPanel() {
        return panel;
    }

    public void setPanel(FinalPanelModel panel) {
        this.panel = panel;
    }

    @Override
    public void eliminate() {
        super.eliminate();

        CollectibleModel.dropCollectible(getAnchor(),
                EntityConstants.ORB_NUM_OF_COLLECTIBLES.getValue(),
                EntityConstants.ORB_COLLECTIBLES_XP.getValue(),
                gameID
        );

        collidables.remove(this);
        // List to collect lasers to be removed
        CopyOnWriteArrayList<Laser> lasersToRemove = new CopyOnWriteArrayList<>();

        // Iterate and collect lasers to be removed
        for (Laser laser : lasers) {
            if (laser.getOrbsOfALaser()[0] == this || laser.getOrbsOfALaser()[1] == this) {
                laser.eliminate();
                lasersToRemove.add(laser);
            }
        }

        // Remove collected lasers from the list
        lasers.removeAll(lasersToRemove);


        panel.eliminate();
//        collidables.remove(this);
    }
}
