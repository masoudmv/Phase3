package model.charactersModel.smiley;

import controller.Game;
import model.MyPolygon;
import model.charactersModel.EpsilonModel;
import model.charactersModel.GeoShapeModel;
import model.entities.AttackTypes;

import java.awt.geom.Point2D;
import java.util.Random;

import static controller.UserInterfaceController.*;
import static controller.Utils.findDistance;
import static controller.constants.EntityConstants.*;

public class SmileyAOE extends GeoShapeModel {

    private static double birthTime = Double.MAX_VALUE;
    private static double lastDamageTime = 0;

    private static SmileyAOE a1;
    private static SmileyAOE a2;
    private static SmileyAOE a3;
    private static boolean isActivated;


    public SmileyAOE(Point2D anchor) {
        super();
        this.anchor = anchor;
        this.radius = SMILEY_AOE_RADIUS.getValue();

        setDummyPolygon();
        createSmileyAOEView(id);
        damageSize.put(AttackTypes.AOE, 5);
    }

    public static void updateAll(){
        if (birthTime == Double.MAX_VALUE) return;
        double now = Game.ELAPSED_TIME;
        if (now - birthTime > SMILEY_AOE_ACTIVATION_TIME.getValue()) isActivated = true;
        if (now - birthTime > SMILEY_AOE_ACTIVATED_LIFETIME.getValue()) {
            eliminateAll();
            birthTime = Double.MAX_VALUE;
        }

        if (isActivated && isEpsilonInside()) {
            // TODO damage epsilon
            EpsilonModel.getINSTANCE().health -= 5;
        }
    }



    private static void eliminateAll(){
        a1.eliminate();
        a2.eliminate();
        a3.eliminate();
    }


    @Override
    public void eliminate(){
        super.eliminate();
    }




    public static void createVomitAOEs(){
        if (birthTime != Double.MAX_VALUE) return;

        birthTime = Game.ELAPSED_TIME;
        isActivated = false;

        Point2D epsilonAnchor = EpsilonModel.getINSTANCE().getAnchor();
        double xMin = epsilonAnchor.getX() - SMILEY_AOE_RECTANGLE_LENGTH.getValue();
        double xMax = epsilonAnchor.getX() + SMILEY_AOE_RECTANGLE_LENGTH.getValue();

        double yMin = epsilonAnchor.getY() - SMILEY_AOE_RECTANGLE_LENGTH.getValue();
        double yMax = epsilonAnchor.getY() + SMILEY_AOE_RECTANGLE_LENGTH.getValue();

        Random random = new Random();

        double x1 = random.nextDouble(xMin, xMax);
        double x2 = random.nextDouble(xMin, xMax);
        double x3 = random.nextDouble(xMin, xMax);

        double y1 = random.nextDouble(yMin, yMax);
        double y2 = random.nextDouble(yMin, yMax);
        double y3 = random.nextDouble(yMin, yMax);


        a1 = new SmileyAOE(new Point2D.Double(x1, y1));
        a2 = new SmileyAOE(new Point2D.Double(x2, y2));
        a3 = new SmileyAOE(new Point2D.Double(x3, y3));
    }


    private void setDummyPolygon(){
        double[] x = {0,0,0};
        double[] y = {0,0,0};
        myPolygon = new MyPolygon(x, y, 3);
    }

    @Override
    public void setMyPolygon(MyPolygon myPolygon) {

    }

    public static boolean isEpsilonInside(){
        double now = Game.ELAPSED_TIME;
        if (now - lastDamageTime < SMILEY_AOE_COOLDOWN.getValue()) return false;

        Point2D epsilon = EpsilonModel.getINSTANCE().getAnchor();
        Point2D first = a1.getAnchor();
        Point2D second = a2.getAnchor();
        Point2D third = a3.getAnchor();

        double d1 = findDistance(epsilon, first);
        double d2 = findDistance(epsilon, second);
        double d3 = findDistance(epsilon, third);

        double distance = SMILEY_AOE_RADIUS.getValue() - EpsilonModel.getINSTANCE().getRadius();

        boolean inside1 = d1 < distance;
        boolean inside2 = d2 < distance;
        boolean inside3 = d3 < distance;

        if (inside1 || inside2 || inside3) {
            lastDamageTime = now;
            return true;
        }

        return false;

    }
}
