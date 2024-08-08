package game.model.charactersModel;

import game.controller.Game;
import game.example.GraphicalObject;
import game.model.FinalPanelModel;
import game.model.reflection.Enemy;
import shared.model.MyPolygon;
import game.model.collision.Collidable;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import static game.controller.UserInterfaceController.createBarricadosView;
//import static game.model.FinalPanelModel.finalPanelModels;
import static shared.model.imagetools.ToolBox.getBufferedImage;
import static shared.constants.Constants.FRAME_DIMENSION;

public class BarricadosModel extends GeoShapeModel implements Collidable, Enemy {
    static BufferedImage image;
    protected static MyPolygon pol;
    private final static Dimension panelSize = new Dimension(400, 400);
    FinalPanelModel f;

    public BarricadosModel(Point2D anchor, String gameID) {
        super(anchor, image, pol, gameID);
        Point2D location = new Point2D.Double(anchor.getX()-200, anchor.getY()-200);
        f = new FinalPanelModel(location, panelSize, gameID);
        f.setRigid(false);
        f.setIsometric(true);
        setMyPolygon();
        updateMyPolygon();
        this.health = Integer.MAX_VALUE;
        collidables.add(this);
        createBarricadosView(id, gameID);

        Game game = findGame(gameID);

        for (GeoShapeModel model : game.entities){
            if (model.intersects(this) && !model.equals(this)) model.eliminate();
        }

    }

    public BarricadosModel() {
        super();
    }

    private void updateMyPolygon(){
        double halfEdgeLength = 95;
        double x = anchor.getX();
        double y = anchor.getY();

        Point2D.Double[] vertices = new Point2D.Double[4];
        vertices[0] = new Point2D.Double(x - halfEdgeLength, y - halfEdgeLength); // Top-left
        vertices[1] = new Point2D.Double(x + halfEdgeLength, y - halfEdgeLength); // Top-right
        vertices[2] = new Point2D.Double(x + halfEdgeLength, y + halfEdgeLength); // Bottom-right
        vertices[3] = new Point2D.Double(x - halfEdgeLength, y + halfEdgeLength); // Bottom-left

        myPolygon.setVertices(vertices);
    }


    private void setMyPolygon(){
        double xLeft = getAnchor().getX() - (double) image.getWidth()/2;
        double xRight = getAnchor().getX() + (double) image.getWidth()/2;
        double yBottom = getAnchor().getY() + (double) image.getHeight()/2;
        double yTop = getAnchor().getX() - (double) image.getHeight()/2;

        double[] xPoints = {xLeft, xRight, xRight, xLeft};
        double[] yPoints = {yTop, yTop, yBottom, yBottom};

        setMyPolygon(new MyPolygon(xPoints, yPoints, 4));

    }

    @Override
    public void setMyPolygon(MyPolygon myPolygon) {

    }


    public static BufferedImage loadImage(){
        Image img = new ImageIcon("./client/src/barricados.png").getImage();
        BarricadosModel.image = getBufferedImage(img);

        GraphicalObject bowser = new GraphicalObject(image);
        pol = bowser.getMyBoundingPolygon();


        return BarricadosModel.image;
    }
//    @Override
//    public void setMyPolygon(MyPolygon myPolygon) {
//

//    }


    @Override
    public void create(String gameID) {
        Random random = new Random();
        double width = FRAME_DIMENSION.getWidth();
        double height = FRAME_DIMENSION.getHeight();
        Point2D pivot;
        Dimension2D panelSize = new Dimension(400, 400);
        boolean isValid;
        int attempts = 0;
        int maxAttempts = 100;

        do {
            double x = random.nextDouble(250, width - 250);
            double y = random.nextDouble(250, height - 250);
            pivot = new Point2D.Double(x, y);
            Point2D panelPivot = new Point2D.Double(x - 200, y - 200);

            isValid = !wouldIntersect(panelPivot, panelSize, gameID);
            attempts++;
        } while (!isValid && attempts < maxAttempts);

        if (isValid) {
            new BarricadosModel(pivot, gameID);
        } else {
            System.out.println("Failed to create BarricadosModel without intersection after " + maxAttempts + " attempts.");
        }
    }

    @Override
    public int getMinSpawnWave() {
        return 4;
    }


    public static boolean wouldIntersect(Point2D location, Dimension2D size, String gameID) {
        double newLeft = location.getX();
        double newRight = location.getX() + size.getWidth();
        double newTop = location.getY();
        double newBottom = location.getY() + size.getHeight();

        for (FinalPanelModel panel : findGame(gameID).finalPanelModels) {
            double existingLeft = panel.getLocation().getX();
            double existingRight = panel.getLocation().getX() + panel.getSize().getWidth();
            double existingTop = panel.getLocation().getY();
            double existingBottom = panel.getLocation().getY() + panel.getSize().getHeight();

            if (newRight > existingLeft && newLeft < existingRight && newBottom > existingTop && newTop < existingBottom) {
                return true;
            }
        }
        return false;
    }

    public FinalPanelModel getF() {
        return f;
    }

    @Override
    public void eliminate() {
        super.eliminate();
        f.eliminate();

    }

    @Override
    public boolean isCircular() {
        return false;
    }




    @Override
    public void onCollision(Collidable other, Point2D intersection) {

    }

    @Override
    public void onCollision(Collidable other, Point2D coll1, Point2D coll2) {

    }
}
