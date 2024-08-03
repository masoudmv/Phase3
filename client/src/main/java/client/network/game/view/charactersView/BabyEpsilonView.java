package client.network.game.view.charactersView;



import client.network.game.controller.constants.EntityConstants;


import java.awt.*;

public class BabyEpsilonView extends GeoShapeView {
    private double radius;

    public BabyEpsilonView(String id) {
        super(id);
        this.radius = EntityConstants.BABY_EPSILON_RADIUS.getValue();
    }

    public void eliminate(){
        super.eliminate();
    }

    @Override
    public void draw(Graphics g, String panelID){
        Graphics2D g2d = (Graphics2D) g;
        if (locations.get(panelID) == null) return;

        float thickness = 2.0f; // Thickness of the edges
        g2d.setStroke(new BasicStroke(thickness));

        g2d.setColor(Color.white);

        int x = (int) locations.get(panelID).getX();
        int y = (int) locations.get(panelID).getY();
        g2d.drawOval((int) (x - radius/2), (int) (y - radius/2), (int) (2 * radius), (int) (2 * radius));
    }


}
