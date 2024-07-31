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


    @Override
    public void draw(Graphics g, String panelID){
        Graphics2D g2d = (Graphics2D) g;
        if (locations.get(panelID) == null) return;
        int x = (int) locations.get(panelID).getX();
        int y = (int) locations.get(panelID).getY();

        g2d.setColor(Color.red);
        g2d.drawOval((int) (x - radius/2), (int) (y - radius/2), (int) (2 * radius), (int) (2 * radius));

    }
}
