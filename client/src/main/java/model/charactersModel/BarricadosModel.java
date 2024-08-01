package model.charactersModel;

import model.FinalPanelModel;
import model.MyPolygon;
import model.collision.Collidable;
import org.example.GraphicalObject;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static model.imagetools.ToolBox.getBufferedImage;

public class BarricadosModel extends GeoShapeModel implements Collidable {
    static BufferedImage image;

    protected static MyPolygon pol;

    private final static Dimension panelSize = new Dimension(400, 400);

    public static ArrayList<BarricadosModel> barricados = new ArrayList<>();

    public BarricadosModel(Point2D anchor) {
        super(anchor, image, pol, 1);
        Point2D location = new Point2D.Double(anchor.getX()-200, anchor.getY()-200);
        FinalPanelModel f = new FinalPanelModel(location, panelSize);
        f.setRigid(false);
        f.setIsometric(true);
        setMyPolygon();

        updateMyPolygon();
        this.health = Integer.MAX_VALUE;

        barricados.add(this);
        collidables.add(this);
    }

    private void updateMyPolygon(){
        double halfEdgeLength = 95;
        double x = anchor.getX();
        double y = anchor.getY();

        Point2D[] vertices = new Point2D[4];
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
    public void eliminate() {

    }

    @Override
    public boolean isCircular() {
        return false;
    }

//    public Point2D[] getVertices(){
//        Point2D[] array = new Point2D[vertices.size()];
//        array = vertices.toArray(array);
//        return array;
//    }

    @Override
    public void onCollision(Collidable other, Point2D intersection) {

    }

    @Override
    public void onCollision(Collidable other, Point2D coll1, Point2D coll2) {

    }
}
