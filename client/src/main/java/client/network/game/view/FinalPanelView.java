package client.network.game.view;

import client.network.game.controller.Utils;
import client.network.game.view.charactersView.GeoShapeView;


import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;



public class FinalPanelView extends JPanel {
    private String id;
    private Point2D Location; // absolute location of the frame
    private Dimension size;
    public static List<FinalPanelView> finalPanelViews = new CopyOnWriteArrayList<>();

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


    }

    public FinalPanelView() {

    }

    public String getId() {
        return id;
    }

    private void moveLocation(Point2D movement) {
        this.Location = Utils.addVectors(Location, movement);
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
    }



    public void eliminate(){
        MainFrame frame = MainFrame.getINSTANCE();
        frame.remove(this);
        finalPanelViews.remove(this);
        System.out.println("ELIMINATED ... ");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Sort the objects by z-order
        GeoShapeView.geoShapeViews.sort(Comparator.comparingInt(GeoShapeView::getZOrder));

        // Draw sorted objects
        for (GeoShapeView obj : GeoShapeView.geoShapeViews) {
            String panelID = getId();
            obj.draw(g, panelID);
        }
    }
}
