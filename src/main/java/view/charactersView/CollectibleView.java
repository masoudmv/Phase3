package view.charactersView;

import java.awt.*;
import java.awt.geom.Point2D;
import static controller.constants.EntityConstants.COLLECTABLE_RADIUS;

public class CollectibleView extends GeoShapeView {
    private final double radius;

    public CollectibleView(String id) {
        super(id);
        this.radius = COLLECTABLE_RADIUS.getValue();
    }


    @Override
    public void eliminate(){
        super.eliminate();
    }

//    @Override
//    public void draw(Graphics g) {
//        g.setColor(Color.pink);
//        Point2D location = this.getCurrentLocation();
//        g.drawOval((int) (location.getX() - radius), (int) (location.getY()-radius), (int) (2 *radius), (int) (2*radius));
//    }
}
