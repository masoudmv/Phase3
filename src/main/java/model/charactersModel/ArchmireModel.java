package model.charactersModel;

import controller.Game;
import model.MyPolygon;
import model.TimedLocation;
import model.movement.Direction;
import util.ThreadPoolManager;
//import view.MainPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import static controller.UserInterfaceController.calculateEntityView;
import static controller.constants.EntityConstants.ARCHMIRE_SPEED;
import static controller.Utils.*;
import static model.imagetools.ToolBox.getBufferedImage;

public class ArchmireModel extends GeoShapeModel {
    static BufferedImage image;
    public static ArrayList<ArchmireModel> archmireModels = new ArrayList<>();
    public LinkedList<TimedLocation> locationHistory = new LinkedList<>();
//    public LinkedList<MyPolygon> recentLocations = new LinkedList<>();
    public Polygon polygon;

    public ArchmireModel(Point2D anchor, MyPolygon myPolygon) {
        super(anchor, image, myPolygon);
        archmireModels.add(this);
        updateDirection();

        // Schedule heavy calculations to run 60 times per second
        ThreadPoolManager.getInstance().scheduleTask(this::performHeavyCalculations, 0, 17, TimeUnit.MILLISECONDS);
    }

    private void updateDirection(){
        Point2D destination = EpsilonModel.getINSTANCE().getAnchor();
        Point2D newDirection = relativeLocation(destination, getAnchor());
        this.direction = new Direction(newDirection);
        this.direction.setMagnitude(ARCHMIRE_SPEED);
    }

    public static BufferedImage loadImage() {
        Image img = new ImageIcon("./src/archmire.png").getImage();
        ArchmireModel.image = getBufferedImage(img);
        return ArchmireModel.image;
    }

    @Override
    public void setMyPolygon(MyPolygon myPolygon) {
        // Implementation needed
    }

    @Override
    public void eliminate() {
        // Implementation needed
    }

    public void updateLocation() {
//        System.out.println(SwingUtilities.isEventDispatchThread());
        updateDirection();
        double currentTime = Game.ELAPSED_TIME;
        locationHistory.addLast(new TimedLocation(myPolygon, currentTime));
//        MainPanel.getINSTANCE().locationHistory.addLast(new TimedLocation(Controller.calculateEntityView(MainPanel.getINSTANCE(), myPolygon), currentTime));
        removeOldLocations(currentTime);
    }

    private void removeOldLocations(double currentTime) {
        while (!locationHistory.isEmpty() && (currentTime - locationHistory.getFirst().getTimestamp() > 5)) {
            locationHistory.removeFirst();
//            MainPanel.getINSTANCE().locationHistory.removeFirst();
        }
    }

    public LinkedList<MyPolygon> getLocationHistory() {
        LinkedList<MyPolygon> recentLocations = new LinkedList<>();
        for (TimedLocation timedLocation : locationHistory) {
            recentLocations.add(timedLocation.getMyPolygon());
        }
        return recentLocations;
    }

    public void performHeavyCalculations() {
//        System.out.println(SwingUtilities.isEventDispatchThread());

//        updateLocation();
//        for (MyPolygon p : getLocationHistory()){
//            MainPanel.recentLocations.add(calculatePolygonOfPolygonalEnemy(MainPanel.getINSTANCE(), p));
//        }

//        SwingUtilities.invokeLater(() -> {
//            updateGUI();
//
//        });
    }

    void move(Direction direction) {
        Point2D movement = multiplyVector(direction.getNormalizedDirectionVector(), direction.getMagnitude());
        movePolygon(movement);
    }

    public void move() {
        move(direction);
    }

    private void updateGUI() {
        // Update the Archmire position or other UI components based on the calculation result
        // Example: Move the Archmire to the new calculated position
//        this.anchor.setLocation(newResultPosition);
//        updateLocation();
        // Repaint or refresh the UI components if needed
//        MainPanel mainPanel = MainPanel.getINSTANCE();
//        mainPanel.repaint();
    }

}

