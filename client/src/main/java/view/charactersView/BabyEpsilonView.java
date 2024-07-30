package view.charactersView;



import java.awt.*;
import static controller.constants.EntityConstants.BABY_EPSILON_RADIUS;

public class BabyEpsilonView extends GeoShapeView {
    private double radius;

    public BabyEpsilonView(String id) {
        super(id);
        this.radius = BABY_EPSILON_RADIUS.getValue();
    }

    public void eliminate(){
        super.eliminate();
    }

//    @Override
//    public void draw(Graphics g) {
//        g.setColor(Color.white);
//
//        g.drawOval((int) (currentLocation.getX()-radius), (int) (currentLocation.getY()-radius), (int) (2*radius), (int) (2*radius));
//    }





}
