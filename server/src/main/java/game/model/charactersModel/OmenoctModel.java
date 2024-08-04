package game.model.charactersModel;

import game.controller.Game;
import game.controller.PolygonUtils;
import game.controller.UserInterfaceController;
import game.controller.Utils;
import shared.constants.Constants;
import shared.constants.EntityConstants;
import shared.constants.SmileyConstants;
import game.example.GraphicalObject;
import game.model.FinalPanelModel;
import shared.Model.MyPolygon;
import game.model.collision.Collidable;
import game.model.movement.Direction;
//import view.MainPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import static game.controller.UserInterfaceController.createOmenoctView;
import static game.controller.Utils.*;
import static shared.Model.imagetools.ToolBox.getBufferedImage;

public class OmenoctModel extends GeoShapeModel implements Collidable {
    public boolean isOnEpsilonPanel = false;
    protected static MyPolygon pol;
    static BufferedImage image;
    private int omenoctEdgeIndex = -1;
    private int destinationEdgeIndex = -1;
    public static ArrayList<OmenoctModel> omenoctModels = new ArrayList<>();
    private double lastShotBullet = 0;

    public OmenoctModel(Point2D anchor){
        super(anchor, image, pol, false);
        omenoctModels.add(this);
        collidables.add(this);
        this.health = EntityConstants.OMENOCT_HEALTH.getValue();
        initVertices();
        createOmenoctView(id);
    }

    private void initVertices() {
        double edgeLength = 20;
        double angle = Math.PI / 4; // 45 degrees in radians
        double radius = edgeLength / (2 * Math.sin(angle / 2));

        // Create an array to hold the x and y coordinates of the vertices
        double[] xPoints = new double[8];
        double[] yPoints = new double[8];

        for (int i = 0; i < 8; i++) {
            double theta = Math.PI / 8 + angle * i;
            xPoints[i] = anchor.getX() + radius * Math.cos(theta);
            yPoints[i] = anchor.getY() + radius * Math.sin(theta);
        }

        // Initialize the MyPolygon instance with the calculated vertices
        pol = new MyPolygon(xPoints, yPoints, 8);
        setMyPolygon(pol);
    }


    private void shootNonRigidBullet(){
        double now = Game.ELAPSED_TIME;
        if (now - lastShotBullet < EntityConstants.OMENOCT_SHOT_DELAY) return;
        BufferedImage ba = SmileyBullet.loadImage();
        GraphicalObject bos = new GraphicalObject(ba);
        MyPolygon pl = bos.getMyBoundingPolygon();
        Point2D startPos = getAnchor();
        new SmileyBullet(startPos).setDirection(findBulletDirection(startPos));
        lastShotBullet = now;
    }

    private Direction findBulletDirection(Point2D startPos){
        Point2D dest = EpsilonModel.getINSTANCE().getAnchor();
        Point2D vec = Utils.relativeLocation(dest, startPos);
        Direction direction = new Direction(vec);
        direction.setMagnitude(SmileyConstants.BULLET_SPEED);
        return direction;
    }

    @Override
    public void eliminate() {
        super.eliminate();
        collidables.remove(this);
        omenoctModels.remove(this);

        CollectibleModel.dropCollectible(
                getAnchor(), EntityConstants.OMENOCT_NUM_OF_COLLECTIBLES.getValue(), EntityConstants.OMENOCT_COLLECTIBLES_XP.getValue()
        );
    }

    public static BufferedImage loadImage() {
        Image img = new ImageIcon("./client/src/omenoct.png").getImage();
        OmenoctModel.image = getBufferedImage(img);

        GraphicalObject bowser = new GraphicalObject(image);
        bowser.refine();
        pol = bowser.getMyBoundingPolygon();

        return OmenoctModel.image;
    }

    @Override
    public void setMyPolygon(MyPolygon myPolygon) {
        this.myPolygon = myPolygon;
    }

    void move(Direction direction) {
        shootNonRigidBullet();
        Point2D movement = Utils.multiplyVector(direction.getNormalizedDirectionVector(), direction.getMagnitude());
        movePolygon(movement);
    }

    public void move() {
        move(direction);
    }

    public void updateDirection() { // todo fix shaking with panel shrinkage
        if (EpsilonModel.getINSTANCE().getLocalPanel() == null) return;
        if (!isOnEpsilonPanel) {
            HashMap<Integer, Point2D> res = Utils.closestPointOnEdges(anchor, EpsilonModel.getINSTANCE().getLocalPanel().getEdges());
            int edgeIndex = res.entrySet().iterator().next().getKey();
            Point2D closest = res.get(edgeIndex);

            switch (edgeIndex) {
                case 0 -> {
                    this.omenoctEdgeIndex = edgeIndex;
                    closest = Utils.addVectors(closest, new Point2D.Double(0, (double) image.getHeight() / 2));
                }
                case 1 -> {
                    this.omenoctEdgeIndex = edgeIndex;
                    closest = Utils.addVectors(closest, new Point2D.Double((double) -image.getWidth() / 2, 0));
                }
                case 2 -> {
                    this.omenoctEdgeIndex = edgeIndex;
                    closest = Utils.addVectors(closest, new Point2D.Double(0, (double) -image.getHeight() / 2));
                }
                case 3 -> {
                    this.omenoctEdgeIndex = edgeIndex;
                    closest = Utils.addVectors(closest, new Point2D.Double((double) image.getWidth() / 2, 0));
                }
            }

            this.direction = new Direction(Utils.relativeLocation(closest, anchor));
            direction.setMagnitude(Constants.SPEED);
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
            Point2D destination = Utils.closestPointOnEdges(anchor, panel.getEdges()).entrySet().iterator().next().getValue();
            isOnEpsilonPanel =
                    Utils.findDistance(destination, anchor) < (double) image.getHeight() / 2 + 3
                            &&
                            Utils.findDistance(destination, anchor) > (double) image.getHeight() / 2 - 3;
        }

    }


    @Override
    public void update(){
        if (dontUpdate()) return;
        setOnEpsilonPanel(EpsilonModel.getINSTANCE().getLocalPanel());
        updateDirection();
    }


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
            case 0 -> dir = new Point2D.Double(EntityConstants.OMENOCT_PANEL_SPEED, 0);
            case 1 -> dir = new Point2D.Double(0, EntityConstants.OMENOCT_PANEL_SPEED);
            case 2 -> dir = new Point2D.Double(-EntityConstants.OMENOCT_PANEL_SPEED, 0);
            case 3 -> dir = new Point2D.Double(0, -EntityConstants.OMENOCT_PANEL_SPEED);
        }
        setDirection(new Direction(dir));
    }

    private void counterClockWiseMovement() {
        Point2D dir = null;
        switch (omenoctEdgeIndex) {
            case 0 -> dir = new Point2D.Double(-EntityConstants.OMENOCT_PANEL_SPEED, 0);
            case 1 -> dir = new Point2D.Double(0, -EntityConstants.OMENOCT_PANEL_SPEED);
            case 2 -> dir = new Point2D.Double(EntityConstants.OMENOCT_PANEL_SPEED, 0);
            case 3 -> dir = new Point2D.Double(0, EntityConstants.OMENOCT_PANEL_SPEED);
        }
        setDirection(new Direction(dir));
    }

    public String getRotationDirection() {
        if (EpsilonModel.getINSTANCE().getLocalPanel() == null) return "";
        Point2D[] corners = EpsilonModel.getINSTANCE().getLocalPanel().getVertices();

//        Point2D start = findClosestPointOnPanel(MainPanel.getINSTANCE());
        Point2D start = Utils.findClosestPointOnEdges(getAnchor(), EpsilonModel.getINSTANCE().getLocalPanel().getEdges());
        Point2D destination = findClosestPointToEpsilon(EpsilonModel.getINSTANCE(), EpsilonModel.getINSTANCE().getLocalPanel());

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
    public void onCollision(Collidable other, Point2D coll1, Point2D coll2) {
        if (other instanceof BarricadosModel) {
            BarricadosModel barricados = (BarricadosModel) other;

            PolygonUtils.Result result = PolygonUtils.findSeparationVector(this.myPolygon, barricados.myPolygon);

            if (result.distance > 0) {
                // Move OmenoctModel by the minimum translation vector
                Point2D movement = new Point2D.Double(-result.direction.x * result.distance, -result.direction.y * result.distance);
                movePolygon(movement);
            }
        }
    }

    private Point2D findClosestPointOnPolygon(MyPolygon polygon, Point2D[] vertices) {
        Point2D closestPoint = null;
        double minDistance = Double.MAX_VALUE;

        for (int i = 0; i < vertices.length; i++) {
            int nextIndex = (i + 1) % vertices.length;
            Point2D p1 = vertices[i];
            Point2D p2 = vertices[nextIndex];

            Point2D closest = getClosestPointOnSegment(p1, p2, polygon.getCenter());
            double distance = closest.distance(polygon.getCenter());

            if (distance < minDistance) {
                minDistance = distance;
                closestPoint = closest;
            }
        }
        return closestPoint;
    }

    private Point2D getClosestPointOnSegment(Point2D p1, Point2D p2, Point2D p) {
        double xDelta = p2.getX() - p1.getX();
        double yDelta = p2.getY() - p1.getY();

        if ((xDelta == 0) && (yDelta == 0)) {
            throw new IllegalArgumentException("p1 and p2 cannot be the same point");
        }

        double u = ((p.getX() - p1.getX()) * xDelta + (p.getY() - p1.getY()) * yDelta) / (xDelta * xDelta + yDelta * yDelta);

        final Point2D closestPoint;
        if (u < 0) {
            closestPoint = p1;
        } else if (u > 1) {
            closestPoint = p2;
        } else {
            closestPoint = new Point2D.Double(p1.getX() + u * xDelta, p1.getY() + u * yDelta);
        }

        return closestPoint;
    }




}
