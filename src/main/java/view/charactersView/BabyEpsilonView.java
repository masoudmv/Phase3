package view.charactersView;

import view.FinalPanelView;

import java.awt.*;

import static controller.constants.EntityConstants.BABY_EPSILON_RADIUS;

public class BabyEpsilonView extends GeoShapeView {
    private double radius;
//    String id;
    public BabyEpsilonView(String id) {
        super(id);
        this.radius = BABY_EPSILON_RADIUS.getValue();
    }


    public void eliminate(){
        super.eliminate();
//        geoShapeViews.remove(this);
//        laserViews.remove(this);
    }


    @Override
    public void draw(Graphics g) {
        g.setColor(Color.white);

        g.drawOval((int) (currentLocation.getX()-radius), (int) (currentLocation.getY()-radius), (int) (2*radius), (int) (2*radius));
    }





}
