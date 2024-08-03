package game.view.charactersView;

import game.controller.Game;
import game.controller.constants.EntityConstants;

public class SmileyAOE extends GeoShapeView{
    private double radius;
    private double birthTime;
    public SmileyAOE(String id) {
        super(id);
        this.radius = EntityConstants.SMILEY_AOE_RADIUS.getValue();
        birthTime = Game.ELAPSED_TIME;
    }



    public void eliminate(){
        super.eliminate();
//        geoShapeViews.remove(this);
    }

//    @Override
//    public void draw(Graphics g) {
//        g.setColor(Color.white);
//
////        g.drawOval((int) (currentLocation.getX()-radius), (int) (currentLocation.getY()-radius), (int) (2*radius), (int) (2*radius));
//
//
//
//        double now = Game.ELAPSED_TIME;
//        g.setColor(Color.green);
//
//
//
//        if (now - birthTime < SMILEY_AOE_ACTIVATION_TIME.getValue()) {
//            g.drawOval((int) (currentLocation.getX()-radius), (int) (currentLocation.getY()-radius), (int) (2*radius), (int) (2*radius));
//        };
//
//
//        if (now - birthTime > SMILEY_AOE_ACTIVATION_TIME.getValue()) {
//            g.fillOval((int) (currentLocation.getX()-radius), (int) (currentLocation.getY()-radius), (int) (2*radius), (int) (2*radius));
//        };
//
//
//
//
////        if (now - birthTime > SMILEY_AOE_ACTIVATED_LIFETIME.getValue());
//    }
}
