package view.charactersView;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import static controller.constants.Constants.BULLET_RADIUS;
import static controller.constants.EntityConstants.COLLECTABLE_RADIUS;

public class CollectibleView extends GeoShapeView {
    String id;
    private double radius;
    public static ArrayList<CollectibleView> collectibleViews = new ArrayList<>();

    public CollectibleView(String id) {
        super(id);


        this.radius = COLLECTABLE_RADIUS.getValue();
//        collectibleViews.add(this);
//        this.currentLocation = currentLocation;
//        drawables.add(this);
    }


    @Override
    public void eliminate(){
        super.eliminate();
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.pink);
        Point2D location = this.getCurrentLocation();
        g.fillOval((int) (location.getX() - radius), (int) (location.getY()-radius), (int) (2 *radius), (int) (2*radius));
//        g.drawOval((int) (location.getX()-BULLET_RADIUS), (int) (location.getY()-BULLET_RADIUS), (int) (2 *BULLET_RADIUS), (int) (2*BULLET_RADIUS));

    }
}
