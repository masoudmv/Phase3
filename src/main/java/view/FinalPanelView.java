package view;

import controller.Controller;
import model.charactersModel.GeoShapeModel;
import view.charactersView.BulletView;
import view.charactersView.Drawable;
import view.charactersView.EpsilonView;
import view.charactersView.GeoShapeView;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import static controller.Controller.*;
import static controller.Controller.calculateViewLocationBullet;
import static controller.Utils.addVectors;
import static view.charactersView.BulletView.bulletViews;
import static view.charactersView.Drawable.drawables;

public class FinalPanelView extends JPanel {
    private String id;
    private Point2D Location; // absolute location of the frame
    private Dimension size;
    public static ArrayList<FinalPanelView> finalPanelViews = new ArrayList<>();

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
    protected void paintComponent(Graphics g) { // todo layared painting
        super.paintComponent(g);
//        g.setColor(Color.gray);
//        BlackOrb.drawBlackOrb(this, g);



        for (int i = 0; i < GeoShapeModel.entities.size(); i++) {
            if (GeoShapeModel.entities.get(i).isLaser){
                Polygon polygon = Controller.calculateEntityView(this, GeoShapeModel.entities.get(i).myPolygon);
                g.fillPolygon(polygon);
            }
        }

        updateEntitiesLocations(this);

        for (GeoShapeView geoShapeView:GeoShapeView.geoShapeViews){
            g.setColor(Color.blue);
            geoShapeView.draw(g);
        }

        for (EpsilonView epsilonView: EpsilonView.epsilonViews){
            epsilonView.setCurrentLocation(
                    calculateViewLocationEpsilon(this, epsilonView.getId())
            );
            epsilonView.setVertices(
                    calculateViewLocationOfEpsilonVertices(this, epsilonView.getId())
            );
        }

//        updateEntitiesLocations(this);


        for (BulletView bulletView : bulletViews){
            bulletView.setCurrentLocation(calculateViewLocationBullet(this, bulletView.getId()));
        }


        for(Drawable obj: drawables){
            obj.draw(g);
        }

    }
}
