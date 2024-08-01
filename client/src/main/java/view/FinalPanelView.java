package view;

import view.charactersView.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static controller.UserInterfaceController.*;
//import static controller.UserInterfaceController.calculateViewLocationBullet;
import static controller.Utils.addVectors;
import static view.charactersView.BulletView.bulletViews;

public class FinalPanelView extends JPanel {
    private String id;
    private Point2D Location; // absolute location of the frame
    private Dimension size;
    public static List<FinalPanelView> finalPanelViews = new CopyOnWriteArrayList<>();
//    private JLayeredPane layeredPane;

    public FinalPanelView() {}

    public FinalPanelView(String id, Point2D Location, Dimension size){
        this.id = id;
        this.Location = Location;
        this.size = size;

        setSize(size);
        setVisible(true);
        setBackground(Color.BLACK);
        setLocation((int) Location.getX(), (int) Location.getY());

        MainFrame frame = MainFrame.getINSTANCE();
        frame.setLayout(null); // Absolute layout
        frame.add(this);
        finalPanelViews.add(this);



//        MainFrame frame = MainFrame.getINSTANCE();
//        frame.setLayout(null); // Absolute layout
//        layeredPane = new JLayeredPane();
//        layeredPane.setBounds(0, 0, frame.getWidth(), frame.getHeight());
//        frame.add(layeredPane);
//        layeredPane.add(this, JLayeredPane.DEFAULT_LAYER);
//        finalPanelViews.add(this);
//        repaint();

//        this.setDoubleBuffered(true);

    }

    public String getId() {
        return id;
    }

    private void moveLocation(Point2D movement) {
        this.Location = addVectors(Location, movement);
        updateLocation();
        repaint(); //needed or not?
    }

    private void updateLocation() {
        setLocation((int) this.Location.getX(), (int) this.Location.getY()); //needed or not?
    }


    public void setLocation(Point2D location) {
        this.Location = location;
        updateLocation();
//        repaint();
    }

    public void setDimension(Dimension size) {
        this.size = size;
        setSize(size);
//        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

//        updateGeoShapeViewsLocations(this);

//        for (EpsilonView epsilonView : EpsilonView.epsilonViews) {
//            epsilonView.setCurrentLocation(
//                    calculateViewLocationEpsilon(this, epsilonView.getId())
//            );
//            epsilonView.setVertices(
//                    calculateViewLocationOfEpsilonVertices(this, epsilonView.getId())
//            );
//        }

//        for (BulletView bulletView : bulletViews) {
//            bulletView.setCurrentLocation(calculateViewLocationBullet(this, bulletView.getId()));
//        }

        // Collect all drawable objects
//        ArrayList<GeoShapeView> drawableObjects = new ArrayList<>();
//        GeoShapeView.geoShapeViews.addAll(GeoShapeView.geoShapeVdwiews);
//        GeoShapeView.geoShapeViews.addAll(drawables);

//        for (Drawable drawable : drawables){
//            drawable.draw(g);
//        }


        // Sort the objects by z-order
        GeoShapeView.geoShapeViews.sort(Comparator.comparingInt(GeoShapeView::getZOrder));

        // Draw sorted objects
        for (GeoShapeView obj : GeoShapeView.geoShapeViews) {
            String panelID = getId();
            obj.draw(g, panelID);
        }

//        System.out.println(SwingUtilities.isEventDispatchThread());
    }
}
