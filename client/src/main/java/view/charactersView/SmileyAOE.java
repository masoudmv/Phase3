package view.charactersView;

import controller.Game;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

import static controller.constants.EntityConstants.*;

public class SmileyAOE extends GeoShapeView{
    private double radius;
    private double birthTime;
    public SmileyAOE(String id) {
        super(id);
        this.radius = SMILEY_AOE_RADIUS.getValue();
        birthTime = Game.elapsedTime;
        zOrder = 1;
    }



    public void eliminate(){
        super.eliminate();
//        geoShapeViews.remove(this);
    }

    @Override
    public void draw(Graphics g, String panelID) {
        g.setColor(Color.white);
        if (locations.get(panelID) == null) return;


        double now = Game.elapsedTime;
        g.setColor(Color.green);

        Point2D currentLocation = locations.get(panelID);

        Color baseColor = Color.GREEN;
        int baseAlpha = 70;

        Map<Integer, Color> alphaColorCache = new HashMap<>();


        // Calculate the maximum and minimum alpha values
        int maxAlpha = baseAlpha;
        int minAlpha = 0;
        int alpha = Math.max(minAlpha, Math.min(maxAlpha, baseAlpha));


        // Get or create the dimmer color with the cached alpha value
        Color dimmerColor = alphaColorCache.computeIfAbsent(alpha, a ->
                new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), a)
        );

        g.setColor(dimmerColor);


        if (now - birthTime < SMILEY_AOE_ACTIVATION_TIME.getValue()) {
            g.drawOval((int) (currentLocation.getX()-radius), (int) (currentLocation.getY()-radius), (int) (2*radius), (int) (2*radius));
        };


        if (now - birthTime > SMILEY_AOE_ACTIVATION_TIME.getValue()) {
            g.fillOval((int) (currentLocation.getX()-radius), (int) (currentLocation.getY()-radius), (int) (2*radius), (int) (2*radius));
        };



    }
}
