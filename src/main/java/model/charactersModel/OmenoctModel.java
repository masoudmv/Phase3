package model.charactersModel;

import controller.Game;
import model.FinalPanelModel;
import model.MyPolygon;
import model.collision.Collidable;
import model.movement.Direction;
import org.example.GraphicalObject;
//import view.MainPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import static controller.constants.Constants.SPEED;
import static controller.constants.EntityConstants.OMENOCT_PANEL_SPEED;
import static controller.Utils.*;
import static controller.constants.EntityConstants.OMENOCT_SHOT_DELAY;
import static controller.constants.SmileyConstants.BULLET_SPEED;
import static model.imagetools.ToolBox.getBufferedImage;

public class OmenoctModel extends GeoShapeModel implements Collidable {
    public boolean isOnEpsilonPanel = false;
    static BufferedImage image;
    private int omenoctEdgeIndex = -1;
    private int destinationEdgeIndex = -1;
    public static ArrayList<OmenoctModel> omenoctModels = new ArrayList<>();
    private double lastShotBullet = 0;

    public OmenoctModel(Point2D anchor, MyPolygon polygon) {
        super(anchor, image, polygon);
        omenoctModels.add(this);
        collidables.add(this);
    }

    private void shootNonRigidBullet(){
        double now = Game.ELAPSED_TIME;
        if (now - lastShotBullet < OMENOCT_SHOT_DELAY) return;
        BufferedImage ba = SmileyBullet.loadImage();
        GraphicalObject bos = new GraphicalObject(ba);
        MyPolygon pl = bos.getMyBoundingPolygon();
        Point2D startPos = getAnchor();
        new SmileyBullet(startPos, pl).setDirection(findBulletDirection(startPos));
        lastShotBullet = now;
    }

    private Direction findBulletDirection(Point2D startPos){
        Point2D dest = EpsilonModel.getINSTANCE().getAnchor();
        Point2D vec = relativeLocation(dest, startPos);
        Direction direction = new Direction(vec);
        direction.setMagnitude(BULLET_SPEED);
        return direction;
    }

    @Override
    public void eliminate() {
    }

    public static BufferedImage loadImage() {
        Image img = new ImageIcon("./src/omenoct.png").getImage();
        OmenoctModel.image = getBufferedImage(img);
        return OmenoctModel.image;
    }

    @Override
    public void setMyPolygon(MyPolygon myPolygon) {
        this.myPolygon = myPolygon;
    }

    void move(Direction direction) {
        shootNonRigidBullet();
        Point2D movement = multiplyVector(direction.getNormalizedDirectionVector(), direction.getMagnitude());
        movePolygon(movement);
    }

    public void move() {
        move(direction);
    }

    public void updateDirection() { // todo fix shaking with panel shrinkage
        if (EpsilonModel.getINSTANCE().localPanel == null) return;
        if (!isOnEpsilonPanel) {
            HashMap<Integer, Point2D> res = closestPointOnEdges(anchor, EpsilonModel.getINSTANCE().localPanel.getEdges());
            int edgeIndex = res.entrySet().iterator().next().getKey();
            Point2D closest = res.get(edgeIndex);

            switch (edgeIndex) {
                case 0 -> {
                    this.omenoctEdgeIndex = edgeIndex;
                    closest = addVectors(closest, new Point2D.Double(0, (double) image.getHeight() / 2));
                }
                case 1 -> {
                    this.omenoctEdgeIndex = edgeIndex;
                    closest = addVectors(closest, new Point2D.Double((double) -image.getWidth() / 2, 0));
                }
                case 2 -> {
                    this.omenoctEdgeIndex = edgeIndex;
                    closest = addVectors(closest, new Point2D.Double(0, (double) -image.getHeight() / 2));
                }
                case 3 -> {
                    this.omenoctEdgeIndex = edgeIndex;
                    closest = addVectors(closest, new Point2D.Double((double) image.getWidth() / 2, 0));
                }
            }

            this.direction = new Direction(relativeLocation(closest, anchor));
            direction.setMagnitude(SPEED);
            move();
        }

        if (isOnEpsilonPanel) {
            if (getRotationDirection().equals("Clockwise")) {
                clockWiseMovement();
                move();
            }
            if (getRotationDirection().equals("Counterclockwise")) {
                counterClockWiseMovement();
                move();
            }


        }
    }

    public void setOnEpsilonPanel(FinalPanelModel panel) {
        if (panel == null) return;
        if (!isPointInPolygon(anchor, panel.getVertices())) {
            isOnEpsilonPanel = false;
        } else {
            Point2D destination = closestPointOnEdges(anchor, panel.getEdges()).entrySet().iterator().next().getValue();
            isOnEpsilonPanel =
                    findDistance(destination, anchor) < (double) image.getHeight() / 2 + 3
                            &&
                            findDistance(destination, anchor) > (double) image.getHeight() / 2 - 3;
        }

    }
//    private Point2D findClosestPointOnPanel(MainPanel panel) {
//        return closestPointOnPolygon(anchor, panel.getVertices());
//    }

    private Point2D findClosestPointToEpsilon(EpsilonModel epsilon, FinalPanelModel panel) {
        double minDistance = Double.MAX_VALUE;
        Point2D closest = null;
        int index = -1;
        Point2D[] vertices = panel.getVertices();
        for (int i=0;i<vertices.length;i++){
            Point2D temp = getClosestPointOnSegment(vertices[i],vertices[(i+1)%vertices.length], epsilon.getAnchor());
            double distance = temp.distance(epsilon.getAnchor());
            if (distance < minDistance) {
                index = i;
                minDistance = distance;
                closest = temp;
            }
        }
        destinationEdgeIndex = index;
        return closest;
    }

//    private void setDirection(Direction direction) {
//        this.direction = direction;
//    }

    private void clockWiseMovement() {
        System.out.println(omenoctEdgeIndex);
        Point2D dir = null;
        switch (omenoctEdgeIndex) {
            case 0 -> dir = new Point2D.Double(OMENOCT_PANEL_SPEED, 0);
            case 1 -> dir = new Point2D.Double(0, OMENOCT_PANEL_SPEED);
            case 2 -> dir = new Point2D.Double(-OMENOCT_PANEL_SPEED, 0);
            case 3 -> dir = new Point2D.Double(0, -OMENOCT_PANEL_SPEED);
        }
        setDirection(new Direction(dir));
    }

    private void counterClockWiseMovement() {
        Point2D dir = null;
        switch (omenoctEdgeIndex) {
            case 0 -> dir = new Point2D.Double(-OMENOCT_PANEL_SPEED, 0);
            case 1 -> dir = new Point2D.Double(0, -OMENOCT_PANEL_SPEED);
            case 2 -> dir = new Point2D.Double(OMENOCT_PANEL_SPEED, 0);
            case 3 -> dir = new Point2D.Double(0, OMENOCT_PANEL_SPEED);
        }
        setDirection(new Direction(dir));
    }

    public String getRotationDirection() {
        if (EpsilonModel.getINSTANCE().localPanel == null) return "";
        Point2D[] corners = EpsilonModel.getINSTANCE().localPanel.getVertices();

//        Point2D start = findClosestPointOnPanel(MainPanel.getINSTANCE());
        Point2D start = findClosestPointOnEdges(getAnchor(), EpsilonModel.getINSTANCE().localPanel.getEdges());
        Point2D destination = findClosestPointToEpsilon(EpsilonModel.getINSTANCE(), EpsilonModel.getINSTANCE().localPanel);

        int startSide = omenoctEdgeIndex;
        int destSide = destinationEdgeIndex;

        // If start and destination are on the same side, determine direction based on relative position
        if (startSide == destSide) {
            switch (startSide) {
                case 0:
                    return start.getX() < destination.getX() ? "Clockwise" : "Counterclockwise";
                case 1:
                    return start.getY() < destination.getY() ? "Clockwise" : "Counterclockwise";
                case 2:
                    return start.getX() > destination.getX() ? "Clockwise" : "Counterclockwise";
                case 3:
                    return start.getY() > destination.getY() ? "Clockwise" : "Counterclockwise";
            }
        }

        double clockwiseDistance = 0;
        Point2D currentPoint = start;
        int i = startSide;
        do {
            Point2D nextPoint = corners[(i + 1) % 4];
            if (i == destSide) {
                clockwiseDistance += distance(currentPoint, destination);
                break;
            } else {
                clockwiseDistance += sideDistance(currentPoint, nextPoint);
            }
            currentPoint = nextPoint;
            i = (i + 1) % 4;
        } while (i != startSide);

        double counterClockwiseDistance = 0;
        currentPoint = start;
        i = startSide;
        do {
            Point2D nextPoint = corners[(i - 1 + 4) % 4];
            if (i == destSide) {
                counterClockwiseDistance += distance(currentPoint, destination);
                break;
            } else {
                counterClockwiseDistance += sideDistance(currentPoint, nextPoint);
            }
            currentPoint = nextPoint;
            i = (i - 1 + 4) % 4;
        } while (i != startSide);

        return clockwiseDistance < counterClockwiseDistance ? "Clockwise" : "Counterclockwise";
    }

    private double distance(Point2D p1, Point2D p2) {
        return p1.distance(p2);
    }

    private double sideDistance(Point2D point, Point2D nextPoint) {
        return distance(point, nextPoint);
    }

    @Override
    public boolean isCircular() {
        return false;
    }

    @Override
    public void onCollision(Collidable other, Point2D intersection) {

    }

    @Override
    public void onCollision(Collidable other) {

    }
}
