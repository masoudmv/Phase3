package model.charactersModel;

import controller.Game;
import controller.Utils;
import model.MyPolygon;
//import view.MainPanel;
import view.charactersView.NecropickView;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import static controller.Controller.createNecropickView;
import static controller.Controller.findNecropickView;
import static controller.constants.EntityConstants.*;
import static model.imagetools.ToolBox.getBufferedImage;

public class NecropickModel extends GeoShapeModel{
    static BufferedImage image;
    public static ArrayList<NecropickModel> necropickModels = new ArrayList<>();
//    public Polygon polygon;
    private boolean isHovering; // equals isUnderGround!
    private double stateChangeTime = 0; // the last Time necropick changed its state
    private Point2D nextAnchor; // precomputed next location
    private boolean isNextLocationCalculated = false; // flag to check if next location is calculated
    private static final Random random = new Random();

    public NecropickModel(Point2D anchor, MyPolygon myPolygon) {
        super(anchor, image, myPolygon);
        necropickModels.add(this);
        stateChangeTime = Game.ELAPSED_TIME; // Initialize state change time
        isHovering = true; // Start in hovering state
        createNecropickView(id, image);
    }

    public static BufferedImage loadImage() {
        Image img = new ImageIcon("./src/archmire.png").getImage();
        NecropickModel.image = getBufferedImage(img);
        return NecropickModel.image;
    }


    private void hideUnderGround(){
        isNextLocationCalculated = false;
        setAnchor(new Point2D.Double(-100, -100)); //:))))
        isHovering = true;
    }

    private void returnToGroundSurface(){
        if (nextAnchor != null) {
            setAnchor(nextAnchor); // Set the precomputed next location
        }
        isHovering = false;

    }

    public static Point2D getRandomPoint(Point2D pivot, double minRadius, double maxRadius) {
        double angle = 2 * Math.PI * random.nextDouble(); // Random angle between 0 and 2π
        double radius = minRadius + (maxRadius - minRadius) * random.nextDouble(); // Random radius between minRadius and maxRadius

        // Convert polar coordinates to Cartesian coordinates
        double x = pivot.getX() + radius * Math.cos(angle);
        double y = pivot.getY() + radius * Math.sin(angle);

        return new Point2D.Double(x, y);
    }

    public void update() {
        double elapsedTime = Game.ELAPSED_TIME;
        if (!isHovering && (elapsedTime - stateChangeTime) >= HOVER_DURATION) {
            hideUnderGround();
            stateChangeTime = elapsedTime;
        }

        if (isHovering && (elapsedTime - stateChangeTime) >= NON_HOVER_DURATION/2 && !isNextLocationCalculated) {
            updateView();
        }

        if (isHovering && (elapsedTime - stateChangeTime) >= NON_HOVER_DURATION) {
            returnToGroundSurface();
            stateChangeTime = elapsedTime;
            updateView();
        }
    }



    @Override
    public void setMyPolygon(MyPolygon myPolygon) {

    }

    @Override
    public void eliminate() {

    }

    private MyPolygon set(Point2D movement){
        double[] xpoints = new double[myPolygon.npoints];
        double[] ypoints = new double[myPolygon.npoints];
        for (int i = 0; i < myPolygon.npoints; i++) {
            xpoints[i] = this.myPolygon.xpoints[i] + movement.getX() - MainPanel.getINSTANCE().getX();
            ypoints[i] = this.myPolygon.ypoints[i] + movement.getY() - MainPanel.getINSTANCE().getY();
        }
        return new MyPolygon(xpoints, ypoints, myPolygon.npoints);
    }

    private void updateView() {
        NecropickView n = findNecropickView(id);
        // todo this shit is ugly. clean it:
        // TODO also you have to update nextLoc continuously !
        if (isHovering) {
            isNextLocationCalculated = true;
            Point2D dest = EpsilonModel.getINSTANCE().getAnchor();
            nextAnchor = getRandomPoint(dest, NECROPICK_MIN_RADIUS, NECROPICK_MAX_RADIUS);

            Point2D move = Utils.relativeLocation(nextAnchor, this.anchor);
            MyPolygon pol = set(move);
            n.setNextPolygon(pol);
            n.setNextLocation(new Point2D.Double(nextAnchor.getX()-MainPanel.getINSTANCE().getX(), nextAnchor.getY()-MainPanel.getINSTANCE().getY()));


            n.showNextLocation = true;
        } else {
            n.showNextLocation = false;
        }
    }
}
